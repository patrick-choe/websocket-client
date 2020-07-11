package com.github.patrick.websocket.client

import com.github.patrick.websocket.event.WebSocketConnectedEvent
import com.github.patrick.websocket.event.WebSocketDisconnectedEvent
import com.github.patrick.websocket.event.WebSocketMessageEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class WebSocketClientListener : Listener {
    private val instance = WebSocketClientPlugin.INSTANCE

    @EventHandler
    fun onConnect(event: WebSocketConnectedEvent) {
        if (event.socket == instance.client?.socket) {
            println("connected to ${instance.url}")
            event.headers.forEach {
                println("${it.key}: ${it.value.joinToString(System.lineSeparator())}}")
            }
        }
    }

    @EventHandler
    fun onDisconnect(event: WebSocketDisconnectedEvent) {
        if (event.socket == instance.client?.socket) {
            println("disconnected from ${instance.url}")
        }
    }

    @EventHandler
    fun onMessage(event: WebSocketMessageEvent) {
        println(event.message)
        if (event.socket == instance.client?.socket) {
            Bukkit.getScheduler().callSyncMethod(instance) {
                Bukkit.dispatchCommand(instance.server.consoleSender, event.message)
            }.get()
        }
    }
}