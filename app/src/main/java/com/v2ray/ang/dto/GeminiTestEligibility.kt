package com.v2ray.ang.dto

import com.v2ray.ang.dto.entities.ProfileItem
import com.v2ray.ang.extension.isComplexType

internal fun isGeminiBaseConfig(
    profile: ProfileItem,
    subscriptionRemarks: String?
): Boolean {
    if (profile.configType.isComplexType()) return false

    return sequenceOf(profile.remarks, subscriptionRemarks.orEmpty()).none { remarks ->
        remarks.contains("telegram", ignoreCase = true) ||
                remarks.contains("mtproto", ignoreCase = true) ||
                remarks.startsWith("TG-", ignoreCase = true) ||
                remarks.startsWith("TG ", ignoreCase = true)
    }
}

internal fun isGeminiTestEligible(
    profile: ProfileItem,
    subscriptionRemarks: String?,
    testDelayMillis: Long,
    geminiPassed: Boolean
): Boolean = isGeminiBaseConfig(profile, subscriptionRemarks) &&
        testDelayMillis in 1L..499L &&
        !geminiPassed
