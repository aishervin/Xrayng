with open("app/src/main/java/com/v2ray/ang/handler/AngConfigManager.kt", "r") as f:
    text = f.read()

import re

old_import_batch = '''    fun importBatchConfig(server: String?, subid: String, append: Boolean): Pair<Int, Int> {
        return try {
            var count = parseCustomConfigServer(server, subid, append)
            if (count <= 0) {
                count = parseBatchConfig(Utils.decode(server), subid, append)
            }
            if (count <= 0) {
                count = parseBatchConfig(server, subid, append)
            }'''

new_import_batch = '''    fun importBatchConfig(server: String?, subid: String, append: Boolean): Pair<Int, Int> {
        return try {
            val decoded = Utils.decode(server)
            var count = parseCustomConfigServer(server, subid, append)
            if (count <= 0) {
                count = parseCustomConfigServer(decoded, subid, append)
            }
            if (count <= 0) {
                count = parseBatchConfig(decoded, subid, append)
            }
            if (count <= 0) {
                count = parseBatchConfig(server, subid, append)
            }'''

text = text.replace(old_import_batch, new_import_batch)

old_parse_via_sub = '''    private fun parseConfigViaSub(server: String?, subid: String, append: Boolean): Int {
        var count = parseBatchConfig(Utils.decode(server), subid, append)
        if (count <= 0) {
            count = parseBatchConfig(server, subid, append)
        }
        if (count <= 0) {
            count = parseCustomConfigServer(server, subid, append)
        }
        return count
    }'''

new_parse_via_sub = '''    private fun parseConfigViaSub(server: String?, subid: String, append: Boolean): Int {
        val decoded = Utils.decode(server)
        var count = parseCustomConfigServer(server, subid, append)
        if (count <= 0) {
            count = parseCustomConfigServer(decoded, subid, append)
        }
        if (count <= 0) {
            count = parseBatchConfig(decoded, subid, append)
        }
        if (count <= 0) {
            count = parseBatchConfig(server, subid, append)
        }
        return count
    }'''

text = text.replace(old_parse_via_sub, new_parse_via_sub)

with open("app/src/main/java/com/v2ray/ang/handler/AngConfigManager.kt", "w") as f:
    f.write(text)
