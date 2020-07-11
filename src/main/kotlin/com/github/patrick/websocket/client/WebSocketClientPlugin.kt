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

        @Suppress("UsePropertyAccessSyntax")
        getCommand("socket")?.run {
            setExecutor(WebSocketClientCommand())
            setTabCompleter(WebSocketClientCommand())
        }
        server.scheduler.runTaskTimer(this, WebSocketClientConfigTask(), 0, 1)
        server.pluginManager.registerEvents(WebSocketClientListener(), this)
    }

    internal fun createWebSocket() {
        client = WebSocketAPI.createWebSocket(url, tls = tls, suppress = false)
        if (client == null) {
            println("WARN: Cannot connect to websocket. Please retry using /socket retry")
        }
    }
}