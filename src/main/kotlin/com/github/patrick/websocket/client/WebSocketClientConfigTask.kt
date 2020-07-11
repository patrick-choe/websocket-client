package com.github.patrick.websocket.client

import java.io.File

class WebSocketClientConfigTask : Runnable {
    private var lastModified = 0L

    override fun run() {
        WebSocketClientPlugin.INSTANCE.run {
            val file = File(dataFolder, "config.yml")
            val last = file.lastModified()
            if (last != lastModified) {
                reloadConfig()
                url = requireNotNull(config?.getString("url")) { "URL missing from config.yml" }
                tls = requireNotNull(config?.getBoolean("tls")) { "TLS option missing from config.yml" }
                client = null
                lastModified = last
            }
        }
    }
}