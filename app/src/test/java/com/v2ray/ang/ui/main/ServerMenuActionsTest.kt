package com.v2ray.ang.ui.main

import org.junit.Assert.assertEquals
import org.junit.Test

class ServerMenuActionsTest {
    @Test
    fun telegramProxyMenuOnlyOffersCopySendAndDelete() {
        assertEquals(
            listOf(ServerMenuAction.SendTelegram, ServerMenuAction.CopyTelegram, ServerMenuAction.Delete),
            serverMenuActions(
                isComplexProfile = false,
                includeManagementActions = true,
                isTelegramProxy = true
            )
        )
    }

    @Test
    fun telegramProxyShareMenuOmitsManagementActions() {
        assertEquals(
            listOf(ServerMenuAction.SendTelegram, ServerMenuAction.CopyTelegram),
            serverMenuActions(
                isComplexProfile = false,
                includeManagementActions = false,
                isTelegramProxy = true
            )
        )
    }

    @Test
    fun nonTelegramMenuKeepsTheExistingProtectedActions() {
        assertEquals(
            listOf(ServerMenuAction.ShareQRCode, ServerMenuAction.ShareClipboard),
            serverMenuActions(
                isComplexProfile = false,
                includeManagementActions = false,
                isTelegramProxy = false
            )
        )
    }
}
