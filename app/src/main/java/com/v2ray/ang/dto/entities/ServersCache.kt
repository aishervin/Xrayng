package com.v2ray.ang.dto.entities

data class ServersCache(
    val guid: String,
    val profile: ProfileItem,
    val testDelayMillis: Long = 0L,
    val geminiPassed: Boolean = false,
    val geminiEligible: Boolean = false,
    val telegramPassed: Boolean = false
)
