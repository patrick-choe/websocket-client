package com.github.patrick.websocket.client

import com.github.patrick.websocket.WebSocketAPI
import com.github.patrick.websocket.WebSocketClient
import org.bukkit.plugin.java.JavaPlugin

class WebSocketClientPlugin : JavaPlugin() {
    companion object {
        lateinit var INSTANCE: WebSocketClientPlugin
    }

    var client: WebSocketClient? = null
        internal set

    lateinit var url: String
        internal set

    var tls = false
        internal set

    override fun onEnable() {
        INSTANCE = this

        saveDefaultConfig()
        url = requireNotNull(config?.getString("url")) { "URL missing from config.yml" }
        tls = requireNotNull(config?.getBoolean("tls")) { "TLS option missing from config.yml" }

        createWebSocket()

        @Suppress("UsePropertyAccessSyntax")
        getCommand("socket")?.run {
            setExecutor(WebSocketClientCommand())
            setTabCompleter(WebSocketClientCommand())
        }
        server.pluginManager.registerEvents(WebSocketClientListener(), this)
        server.scheduler.runTaskTimer(this, WebSocketClientConfigTask(), 0, 1)
    }

    internal fun createWebSocket() {
        client = WebSocketAPI.createWebSocket(url, tls = true, suppress = false)
        if (client == null) {
            println("WARN: Cannot connect to websocket. Please retry using /socket retry")
        }
    }
}