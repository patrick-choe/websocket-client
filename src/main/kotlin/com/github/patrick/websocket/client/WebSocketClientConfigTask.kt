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

import java.io.File

class WebSocketClientConfigTask : Runnable {
    private var lastModified = 0L

    override fun run() {
        with(WebSocketClientPlugin.INSTANCE) {
            val file = File(dataFolder, "config.yml")
            val last = file.lastModified()
            if (last != lastModified) {
                lastModified = last

                server.scheduler.runTaskAsynchronously(this, Runnable {
                    reloadConfig()

                    val url = requireNotNull(config.getString("url")) { "URL missing from config.yml" }
                    val tls = requireNotNull(config.getBoolean("tls")) { "TLS option missing from config.yml" }
                    connected = createWebSocket(url, tls).connect()
                })
            }
        }
    }
}