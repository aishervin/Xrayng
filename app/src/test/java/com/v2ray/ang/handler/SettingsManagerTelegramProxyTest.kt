package com.v2ray.ang.handler

import com.v2ray.ang.AppConfig
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SettingsManagerTelegramProxyTest {
    @Test
    fun identifiesTelegramProxySubscriptionByNameOrUrl() {
        assertTrue(SettingsManager.isTelegramProxySource(" Telegram proxy ", ""))
        assertTrue(SettingsManager.isTelegramProxySource("Custom name", AppConfig.BUILTIN_SUB_TGPROXY_URL))
        assertFalse(SettingsManager.isTelegramProxySource("Xray", AppConfig.BUILTIN_SUB_XRAY_URL))
        assertFalse(SettingsManager.isTelegramProxySource(null, null))
    }
}
