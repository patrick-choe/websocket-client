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