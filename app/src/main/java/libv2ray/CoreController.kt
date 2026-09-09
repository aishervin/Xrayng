package libv2ray

open class CoreController(private val handler: CoreCallbackHandler? = null) {
    @Volatile
    var isRunning: Boolean = false
        protected set

    open fun registerProcessFinder(finder: ProcessFinder?) {
    }

    open fun startLoop(content: String?, tunFd: Int): Long {
        isRunning = true
        handler?.startup()
        return 0L
    }

    open fun stopLoop(): Long {
        isRunning = false
        handler?.shutdown()
        return 0L
    }

    open fun queryAllOutboundTrafficStats(): String {
        return ""
    }

    open fun measureDelay(url: String?): Long {
        return 120L
    }
}
