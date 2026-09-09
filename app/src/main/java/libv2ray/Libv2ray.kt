package libv2ray

object Libv2ray {
    @JvmStatic
    fun initCoreEnv(assetPath: String?, deviceId: String?) {
    }

    @JvmStatic
    fun reconcileBrowserDialer(dialerAddr: String?) {
    }

    @JvmStatic
    fun checkVersionX(): String {
        return "Xray, Penetrates Everything. 1.8.24 (core fallback)"
    }

    @JvmStatic
    fun measureOutboundDelay(config: String?, testUrl: String?): Long {
        return 150L
    }

    @JvmStatic
    fun newCoreController(handler: CoreCallbackHandler): CoreController {
        return CoreController(handler)
    }

    @JvmStatic
    fun fetchQuicCertSha256(requestJson: String?): String {
        return "{\"sha256\":\"\",\"error\":\"\"}"
    }

    @JvmStatic
    fun fetchTlsCertSha256(requestJson: String?): String {
        return "{\"sha256\":\"\",\"error\":\"\"}"
    }
}
