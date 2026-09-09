package com.v2ray.ang.fmt

import com.v2ray.ang.AppConfig
import com.v2ray.ang.dto.entities.ProfileItem
import com.v2ray.ang.enums.EConfigType
import com.v2ray.ang.util.Utils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class HttpAndSocksFmtTest {

    // ================= SOCKS Parsing Tests =================

    @Test
    fun test_parseSocks_standardUrl() {
        val url = "socks5://user123:pass456@192.168.1.1:1080#MySocks"
        val config = SocksFmt.parse(url)

        assertNotNull(config)
        assertEquals(EConfigType.SOCKS, config?.configType)
        assertEquals("192.168.1.1", config?.server)
        assertEquals("1080", config?.serverPort)
        assertEquals("user123", config?.username)
        assertEquals("pass456", config?.password)
        assertEquals("MySocks", config?.remarks)
    }

    @Test
    fun test_parseSocks_withoutAuth() {
        val url = "socks://proxy.domain.com:1080#FreeSocks"
        val config = SocksFmt.parse(url)

        assertNotNull(config)
        assertEquals(EConfigType.SOCKS, config?.configType)
        assertEquals("proxy.domain.com", config?.server)
        assertEquals("1080", config?.serverPort)
        assertNull(config?.username)
        assertEquals("FreeSocks", config?.remarks)
    }

    @Test
    fun test_parseSocks_telegramFormat() {
        val url = "tg://socks?server=10.0.0.1&port=1080&user=tguser&pass=tgpass#TelegramProxy"
        val config = SocksFmt.parse(url)

        assertNotNull(config)
        assertEquals(EConfigType.SOCKS, config?.configType)
        assertEquals("10.0.0.1", config?.server)
        assertEquals("1080", config?.serverPort)
        assertEquals("tguser", config?.username)
        assertEquals("tgpass", config?.password)
    }

    @Test
    fun test_parseSocks_colonSeparated() {
        val url = "socks5://192.168.1.1:1080:myuser:mypass#ColonSocks"
        val config = SocksFmt.parse(url)

        assertNotNull(config)
        assertEquals(EConfigType.SOCKS, config?.configType)
        assertEquals("192.168.1.1", config?.server)
        assertEquals("1080", config?.serverPort)
        assertEquals("myuser", config?.username)
        assertEquals("mypass", config?.password)
    }

    @Test
    fun test_parseSocks_base64Authority() {
        // base64 of "user:pass@1.2.3.4:1080"
        val raw = "user:pass@1.2.3.4:1080"
        val b64 = Utils.encode(raw)
        val url = "socks://$b64#B64Socks"
        val config = SocksFmt.parse(url)

        assertNotNull(config)
        assertEquals("1.2.3.4", config?.server)
        assertEquals("1080", config?.serverPort)
        assertEquals("user", config?.username)
        assertEquals("pass", config?.password)
    }

    // ================= HTTP / HTTPS Parsing Tests =================

    @Test
    fun test_parseHttp_standardUrl() {
        val url = "http://admin:secret@10.0.0.2:8080#HttpProxy"
        val config = HttpFmt.parse(url)

        assertNotNull(config)
        assertEquals(EConfigType.HTTP, config?.configType)
        assertEquals("10.0.0.2", config?.server)
        assertEquals("8080", config?.serverPort)
        assertEquals("admin", config?.username)
        assertEquals("secret", config?.password)
        assertEquals("HttpProxy", config?.remarks)
    }

    @Test
    fun test_parseHttps_withTls() {
        val url = "https://admin:secret@secure-proxy.com:8443#HttpsProxy"
        val config = HttpFmt.parse(url)

        assertNotNull(config)
        assertEquals(EConfigType.HTTP, config?.configType)
        assertEquals("secure-proxy.com", config?.server)
        assertEquals("8443", config?.serverPort)
        assertEquals(AppConfig.TLS, config?.security)
        assertEquals("secure-proxy.com", config?.sni)
    }

    @Test
    fun test_parseHttp_telegramFormat() {
        val url = "tg://http?server=1.1.1.1&port=8080&user=u&pass=p"
        val config = HttpFmt.parse(url)

        assertNotNull(config)
        assertEquals(EConfigType.HTTP, config?.configType)
        assertEquals("1.1.1.1", config?.server)
        assertEquals("8080", config?.serverPort)
        assertEquals("u", config?.username)
        assertEquals("p", config?.password)
    }

    @Test
    fun test_parseHttp_colonSeparated() {
        val url = "http://10.0.0.2:8080:myuser:mypass#ColonHttp"
        val config = HttpFmt.parse(url)

        assertNotNull(config)
        assertEquals(EConfigType.HTTP, config?.configType)
        assertEquals("10.0.0.2", config?.server)
        assertEquals("8080", config?.serverPort)
        assertEquals("myuser", config?.username)
        assertEquals("mypass", config?.password)
    }

    @Test
    fun test_parseHttp_base64Authority() {
        val raw = "myuser:mypass@10.0.0.5:3128"
        val b64 = Utils.encode(raw)
        val url = "http://$b64#B64Http"
        val config = HttpFmt.parse(url)

        assertNotNull(config)
        assertEquals("10.0.0.5", config?.server)
        assertEquals("3128", config?.serverPort)
        assertEquals("myuser", config?.username)
        assertEquals("mypass", config?.password)
    }

    @Test
    fun test_toUri_socksAndHttp() {
        val socks = ProfileItem.create(EConfigType.SOCKS).apply {
            server = "1.2.3.4"
            serverPort = "1080"
            username = "u"
            password = "p"
            remarks = "SocksNode"
        }
        val socksUri = SocksFmt.toUri(socks)
        assertTrue(socksUri.contains("1.2.3.4:1080"))

        val http = ProfileItem.create(EConfigType.HTTP).apply {
            server = "5.6.7.8"
            serverPort = "8080"
            username = "admin"
            password = "pass"
            remarks = "HttpNode"
        }
        val httpUri = HttpFmt.toUri(http)
        assertTrue(httpUri.contains("5.6.7.8:8080"))
    }
}
