package com.v2ray.ang.dto

import com.v2ray.ang.dto.entities.ProfileItem
import com.v2ray.ang.enums.EConfigType
import com.v2ray.ang.ui.main.canStartBatchScan
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GeminiTestEligibilityTest {
    @Test
    fun acceptsOnlyHealthyUnpassedBaseProfiles() {
        val profile = ProfileItem.create(EConfigType.VLESS).apply { remarks = "node" }

        assertTrue(isGeminiTestEligible(profile, null, 125L, geminiPassed = false))
        assertFalse(isGeminiTestEligible(profile, null, 0L, geminiPassed = false))
        assertFalse(isGeminiTestEligible(profile, null, 500L, geminiPassed = false))
        assertFalse(isGeminiTestEligible(profile, null, 125L, geminiPassed = true))
    }

    @Test
    fun excludesComplexAndTelegramProfiles() {
        val group = ProfileItem.create(EConfigType.POLICYGROUP)
        val telegramRemark = ProfileItem.create(EConfigType.SOCKS).apply { remarks = "MTProto relay" }
        val base = ProfileItem.create(EConfigType.VMESS).apply { remarks = "ordinary node" }

        assertFalse(isGeminiTestEligible(group, null, 100L, geminiPassed = false))
        assertFalse(isGeminiTestEligible(telegramRemark, null, 100L, geminiPassed = false))
        assertFalse(isGeminiTestEligible(base, "Telegram proxy", 100L, geminiPassed = false))
        assertTrue(isGeminiTestEligible(base, "Personal list", 100L, geminiPassed = false))
    }

    @Test
    fun batchScansAreBlockedWhileConnected() {
        assertTrue(canStartBatchScan(isConnected = false))
        assertFalse(canStartBatchScan(isConnected = true))
    }
}
