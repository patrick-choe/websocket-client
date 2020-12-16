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
        if (event.socket == instance.client.socket) {
            println("connected to socket")
        }
    }

    @EventHandler
    fun onDisconnect(event: WebSocketDisconnectedEvent) {
        if (event.socket == instance.client.socket) {
            println("disconnected from socket")
        }
    }

    @EventHandler
    fun onMessage(event: WebSocketMessageEvent) {
        println("msg: ${event.message}")
        if (event.socket == instance.client.socket) {
            Bukkit.dispatchCommand(instance.server.consoleSender, event.message)
        }
    }
}