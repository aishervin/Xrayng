with open("app/src/main/java/com/v2ray/ang/handler/AngConfigManager.kt", "r") as f:
    text = f.read()

import re
old1 = '''    fun importBatchConfig(server: String?, subid: String, append: Boolean): Pair<Int, Int> {
        return try {
            var count = parseBatchConfig(Utils.decode(server), subid, append)
            if (count <= 0) {
                count = parseBatchConfig(server, subid, append)
            }
            if (count <= 0) {
                count = parseCustomConfigServer(server, subid, append)
            }'''

new1 = '''    fun importBatchConfig(server: String?, subid: String, append: Boolean): Pair<Int, Int> {
        return try {
            var count = parseCustomConfigServer(server, subid, append)
            if (count <= 0) {
                count = parseBatchConfig(Utils.decode(server), subid, append)
            }
            if (count <= 0) {
                count = parseBatchConfig(server, subid, append)
            }'''

old2 = '''    private fun parseConfigViaSub(server: String?, subid: String, append: Boolean): Int {
        var count = parseBatchConfig(Utils.decode(server), subid, append)
        if (count <= 0) {
            count = parseBatchConfig(server, subid, append)
        }
        if (count <= 0) {
            count = parseCustomConfigServer(server, subid, append)
        }
        return count
    }'''

new2 = '''    private fun parseConfigViaSub(server: String?, subid: String, append: Boolean): Int {
        var count = parseCustomConfigServer(server, subid, append)
        if (count <= 0) {
            count = parseBatchConfig(Utils.decode(server), subid, append)
        }
        if (count <= 0) {
            count = parseBatchConfig(server, subid, append)
        }
        return count
    }'''

text = text.replace(old1, new1)
text = text.replace(old2, new2)

with open("app/src/main/java/com/v2ray/ang/handler/AngConfigManager.kt", "w") as f:
    f.write(text)
