package com.v2ray.ang.fmt

import com.v2ray.ang.AppConfig
import com.v2ray.ang.dto.entities.ProfileItem
import com.v2ray.ang.enums.EConfigType
import com.v2ray.ang.extension.isNotNullEmpty
import com.v2ray.ang.util.Utils
import java.net.URI

object HttpFmt : FmtBase() {
    /**
     * Parses an HTTP / HTTPS URI string or TG proxy into a ProfileItem object.
     *
     * @param str the HTTP URI string to parse
     * @return the parsed ProfileItem object, or null if parsing fails
     */
    fun parse(str: String): ProfileItem? {
        val trimmed = str.trim()
        if (trimmed.isEmpty()) return null

        // Handle Telegram HTTP proxy link: tg://http?server=...&port=...&user=...&pass=...
        // or https://t.me/http?server=...
        if (trimmed.startsWith("tg://http", ignoreCase = true) ||
            trimmed.startsWith("tg://proxy", ignoreCase = true) ||
            trimmed.startsWith("https://t.me/http", ignoreCase = true) ||
            trimmed.startsWith("https://t.me/proxy", ignoreCase = true)
        ) {
            return parseTelegramHttp(trimmed)
        }

        return parseHttpUrl(trimmed)
    }

    private fun parseTelegramHttp(str: String): ProfileItem? {
        try {
            val uri = URI(Utils.fixIllegalUrl(str))
            val queryParam = uri.rawQuery?.split("&")?.associate {
                val parts = it.split("=", limit = 2)
                parts[0].lowercase() to Utils.decodeURIComponent(parts.getOrElse(1) { "" })
            } ?: emptyMap()

            val server = queryParam["server"] ?: return null
            val port = queryParam["port"]?.toIntOrNull() ?: return null
            if (port <= 0 || port > 65535) return null

            val config = ProfileItem.create(EConfigType.HTTP)
            config.server = server
            config.serverPort = port.toString()
            config.username = queryParam["user"] ?: queryParam["username"]
            config.password = queryParam["pass"] ?: queryParam["password"]
            config.remarks = Utils.decodeURIComponent(uri.fragment.orEmpty()).ifEmpty { "http-$server:$port" }
            return config
        } catch (e: Exception) {
            return null
        }
    }

    private fun parseHttpUrl(rawStr: String): ProfileItem? {
        try {
            val isHttps = rawStr.startsWith("https://", ignoreCase = true)
            var str = rawStr

            // Extract fragment / remarks
            var remarks = ""
            val hashIndex = str.indexOf('#')
            if (hashIndex != -1) {
                remarks = Utils.decodeURIComponent(str.substring(hashIndex + 1)).trim()
                str = str.substring(0, hashIndex)
            }

            // Remove scheme prefix if present
            val schemeIndex = str.indexOf("://")
            val body = if (schemeIndex != -1) str.substring(schemeIndex + 3) else str

            // Check if body is base64 encoded
            val decodedBody = if (!body.contains(":") || (!body.contains("@") && body.length % 4 == 0 && !body.contains("/"))) {
                val dec = Utils.decode(body)
                if (dec.isNotEmpty() && dec.contains(":")) dec else body
            } else {
                body
            }

            // Check for query parameters
            var queryUser: String? = null
            var queryPass: String? = null
            var mainPart = decodedBody
            val queryIndex = mainPart.indexOf('?')
            if (queryIndex != -1) {
                val queryStr = mainPart.substring(queryIndex + 1)
                mainPart = mainPart.substring(0, queryIndex)
                val params = queryStr.split("&").associate {
                    val parts = it.split("=", limit = 2)
                    parts[0].lowercase() to Utils.decodeURIComponent(parts.getOrElse(1) { "" })
                }
                queryUser = params["user"] ?: params["username"]
                queryPass = params["pass"] ?: params["password"]
            }

            var host: String
            var port: Int
            var username: String? = queryUser
            var password: String? = queryPass

            if (mainPart.contains("@")) {
                val atParts = mainPart.split("@", limit = 2)
                val userPart = atParts[0]
                val hostPart = atParts[1]

                val decodedUserPart = if (!userPart.contains(":") && userPart.isNotEmpty()) {
                    val dec = Utils.decode(userPart)
                    if (dec.contains(":")) dec else userPart
                } else {
                    userPart
                }

                if (decodedUserPart.contains(":")) {
                    val up = decodedUserPart.split(":", limit = 2)
                    username = up[0]
                    password = up[1]
                } else if (decodedUserPart.isNotEmpty()) {
                    username = decodedUserPart
                }

                val hp = parseHostAndPort(hostPart) ?: return null
                host = hp.first
                port = hp.second
            } else {
                val parts = mainPart.split(":")
                when {
                    parts.size == 2 -> {
                        host = parts[0]
                        port = parts[1].toIntOrNull() ?: return null
                    }
                    parts.size == 4 && parts[1].toIntOrNull() != null -> {
                        host = parts[0]
                        port = parts[1].toInt()
                        username = parts[2]
                        password = parts[3]
                    }
                    parts.size == 4 && parts[3].toIntOrNull() != null -> {
                        username = parts[0]
                        password = parts[1]
                        host = parts[2]
                        port = parts[3].toInt()
                    }
                    else -> {
                        val hp = parseHostAndPort(mainPart) ?: return null
                        host = hp.first
                        port = hp.second
                    }
                }
            }

            if (host.isEmpty() || port <= 0 || port > 65535) return null

            val config = ProfileItem.create(EConfigType.HTTP)
            config.server = host
            config.serverPort = port.toString()
            config.username = username
            config.password = password
            config.remarks = remarks.ifEmpty { "http-$host:$port" }

            if (isHttps) {
                config.security = AppConfig.TLS
                config.sni = host
            }

            return config
        } catch (e: Exception) {
            return null
        }
    }

    private fun parseHostAndPort(str: String): Pair<String, Int>? {
        val trimmed = str.trim().trimEnd('/')
        if (trimmed.isEmpty()) return null
        if (trimmed.startsWith("[")) {
            val endBracket = trimmed.indexOf(']')
            if (endBracket != -1) {
                val host = trimmed.substring(1, endBracket)
                val portStr = trimmed.substring(endBracket + 1).removePrefix(":")
                val port = portStr.toIntOrNull() ?: return null
                return host to port
            }
        }
        val colonIndex = trimmed.lastIndexOf(':')
        if (colonIndex == -1) return null
        val host = trimmed.substring(0, colonIndex).trim()
        val port = trimmed.substring(colonIndex + 1).trim().toIntOrNull() ?: return null
        return host to port
    }

    /**
     * Converts a ProfileItem object to a URI string.
     *
     * @param config the ProfileItem object to convert
     * @return the converted URI string
     */
    fun toUri(config: ProfileItem): String {
        val pw = if (config.username.isNotNullEmpty())
            "${config.username}:${config.password}"
        else
            ""

        val userInfo = if (pw.isNotEmpty()) Utils.encode(pw, true) else null
        return toUri(config, userInfo, null)
    }
}
