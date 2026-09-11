package com.v2ray.ang.service

import android.content.Context
import com.v2ray.ang.core.CoreConfigManager
import com.v2ray.ang.core.CoreNativeManager
import com.v2ray.ang.dto.RealPingEvent
import com.v2ray.ang.enums.EConfigType
import com.v2ray.ang.extension.isComplexType
import com.v2ray.ang.extension.isNotNullEmpty
import com.v2ray.ang.handler.MmkvManager
import com.v2ray.ang.handler.SettingsManager
import com.v2ray.ang.handler.SpeedtestManager
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.isActive
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

internal object RealPingExecutionLimiter {
    private val customConfigMutex = Mutex()

    suspend fun <T> run(configType: EConfigType, block: () -> T): T {
        return if (configType == EConfigType.CUSTOM) {
            customConfigMutex.withLock { block() }
        } else {
            block()
        }
    }
}

/**
 * Worker for a real-ping batch.
 * A stop request is a cooperative pause: already-running native probes are
 * allowed to finish so their results are persisted, while queued probes do not start.
 */
class RealPingWorkerService(
    private val context: Context,
    private val guids: List<String>,
    private val onlyTcp: Boolean = false,
    private val customUrl: String? = null,
    private val onEvent: (RealPingEvent) -> Unit = {}
) {
    private val job = SupervisorJob()
    private val concurrency = SettingsManager.getRealPingConcurrency()
    private val dispatcher = Executors.newFixedThreadPool(if (onlyTcp) concurrency * 2 else concurrency).asCoroutineDispatcher()
    private val scope = CoroutineScope(job + dispatcher + CoroutineName("RealPingBatchWorker"))
    private val stopRequested = AtomicBoolean(false)

    private val runningCount = AtomicInteger(0)
    private val totalCount = AtomicInteger(0)

    fun start() {
        val jobs = guids.map { guid ->
            totalCount.incrementAndGet()
            scope.launch {
                if (stopRequested.get()) {
                    totalCount.decrementAndGet()
                    return@launch
                }

                runningCount.incrementAndGet()
                try {
                    if (stopRequested.get()) return@launch
                    val result = if (onlyTcp) startTcping(guid) else startRealPing(guid)
                    // Do not gate this on scope.isActive: if the native probe completed
                    // after a stop request, its result is exactly what the user asked to keep.
                    onEvent(RealPingEvent.Result(guid, result))
                } catch (_: Throwable) {
                    // ignore individual probe failures
                } finally {
                    val count = totalCount.decrementAndGet()
                    val left = runningCount.decrementAndGet()
                    if (scope.isActive) {
                        onEvent(RealPingEvent.Progress("$left / $count"))
                    }
                }
            }
        }

        scope.launch {
            joinAll(*jobs.toTypedArray())
            if (isActive) {
                onEvent(RealPingEvent.Finish(if (stopRequested.get()) "PAUSED" else "0"))
            }
            close()
        }
    }

    fun requestStop() {
        stopRequested.set(true)
    }

    fun cancel() {
        requestStop()
    }

    private fun close() {
        try {
            dispatcher.close()
        } catch (_: Throwable) {
            // ignore
        }
    }

    private suspend fun startRealPing(guid: String): Long {
        if (stopRequested.get()) return -2L
        val retFailure = -1L

        val config = MmkvManager.decodeServerConfig(guid) ?: return retFailure
        if (!config.configType.isComplexType()
            && config.configType != EConfigType.HYSTERIA2
            && config.configType != EConfigType.WIREGUARD
            && config.alpn?.startsWith("h3") != true
            && config.server.isNotNullEmpty()
            && config.serverPort?.toIntOrNull() != null
        ) {
            if (stopRequested.get()) return -2L
            val url = config.server.orEmpty()
            val port = config.serverPort.orEmpty().toInt()
            val tcpTime = SpeedtestManager.socketConnectTime(url, port, 1000)
            if (tcpTime <= -1L) {
                return retFailure
            }
        }

        if (stopRequested.get()) return -2L
        val configResult = CoreConfigManager.getV2rayConfig4Speedtest(context, guid)
        if (!configResult.status) {
            return retFailure
        }
        if (stopRequested.get()) return -2L
        return RealPingExecutionLimiter.run(config.configType) {
            CoreNativeManager.measureOutboundDelay(configResult.content, customUrl ?: SettingsManager.getDelayTestUrl())
        }
    }

    private fun startTcping(guid: String): Long {
        if (stopRequested.get()) return -2L
        val retFailure = -1L

        val config = MmkvManager.decodeServerConfig(guid) ?: return retFailure
        if (!config.configType.isComplexType()
            && config.configType != EConfigType.HYSTERIA2
            && config.configType != EConfigType.WIREGUARD
            && config.alpn?.split(',')?.all { it.trim().startsWith("h3") } != true
            && config.server.isNotNullEmpty()
            && config.serverPort?.toIntOrNull() != null
        ) {
            if (stopRequested.get()) return -2L
            val url = config.server.orEmpty()
            val port = config.serverPort.orEmpty().toInt()
            return SpeedtestManager.socketConnectTime(url, port, 1000)
        }

        return retFailure
    }
}
