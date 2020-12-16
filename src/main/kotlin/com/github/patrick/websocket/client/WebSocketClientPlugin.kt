/*
 * Copyright (C) 2020 PatrickKR
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 * Contact me on <mailpatrickkr@gmail.com>
 */

package com.github.patrick.websocket.client

import com.github.patrick.websocket.WebSocketAPI
import com.github.patrick.websocket.WebSocketClient
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.minecraft.server.v1_16_R3.ArgumentChat
import net.minecraft.server.v1_16_R3.ChatComponentText
import net.minecraft.server.v1_16_R3.CommandListenerWrapper
import org.bukkit.craftbukkit.v1_16_R3.CraftServer
import org.bukkit.craftbukkit.v1_16_R3.command.VanillaCommandWrapper
import org.bukkit.plugin.java.JavaPlugin

class WebSocketClientPlugin : JavaPlugin() {
    companion object {
        lateinit var INSTANCE: WebSocketClientPlugin
    }

    lateinit var client: WebSocketClient
        private set

    internal var connected = false

    override fun onEnable() {
        val server = server as CraftServer

        INSTANCE = this

        saveDefaultConfig()

        server.scheduler.runTaskTimer(this, WebSocketClientConfigTask(), 0, 1)
        server.pluginManager.registerEvents(WebSocketClientListener(), this)

        val commandDispatcher = server.server.commandDispatcher

        val send = LiteralArgumentBuilder.literal<CommandListenerWrapper>("send").requires { wrapper ->
            wrapper.hasPermission(4)
        }.then(
            RequiredArgumentBuilder.argument<CommandListenerWrapper, ArgumentChat.a>("message", ArgumentChat.a())
                .executes { context ->
                    val message = ArgumentChat.a(context, "message").string

                    if (this::client.isInitialized && connected) {
                        client.send(message)

                        1
                    } else {
                        context.source.sendFailureMessage(ChatComponentText("client not available"))

                        0
                    }
                }
        )

        val reconnect = LiteralArgumentBuilder.literal<CommandListenerWrapper>("reconnect").requires { wrapper ->
            wrapper.hasPermission(4)
        }.executes { context ->
            if (this::client.isInitialized && connected) {
                connected = false
                server.scheduler.runTaskAsynchronously(this, Runnable {
                    connected = client.reconnect()
                })

                1
            } else {
                context.source.sendFailureMessage(ChatComponentText("client not available"))

                0
            }
        }

        with(commandDispatcher.a()) {
            register(send)
            register(reconnect)
        }

        with(server.commandMap) {
            register("send", "minecraft", VanillaCommandWrapper(commandDispatcher, send.build()))
            register("reconnect", "minecraft", VanillaCommandWrapper(commandDispatcher, reconnect.build()))
        }
    }

    internal fun createWebSocket(url: String, tls: Boolean): WebSocketClient {
        client = WebSocketAPI.createUnsafeWebSocket(url, tls, false)

        return client
    }
}