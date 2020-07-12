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

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class WebSocketClientCommand : CommandExecutor, TabCompleter {
    private val instance = WebSocketClientPlugin.INSTANCE

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.count() == 1) {
            instance.run {
                when (args[0].toLowerCase()) {
                    "retry" -> when (client) {
                        null -> createWebSocket()
                        else -> sender.sendMessage("Client already exists")
                    }
                    "connect" -> when {
                        client == null -> sender.sendMessage("Client does not exist. Please retry using /$label retry")
                        client?.connected == true -> sender.sendMessage("Client already connected")
                        else -> client?.connect()
                    }
                    "disconnect" -> when {
                        client == null -> sender.sendMessage("Client does not exist. Please retry using /$label retry")
                        client?.connected == false -> sender.sendMessage("Client not connected")
                        else -> client?.disconnect()
                    }
                    else -> sender.sendMessage("/$label retry connect disconnect")
                }
            }
        } else {
            sender.sendMessage("/$label retry connect disconnect")
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): List<String> {
        return if (args.count() == 1) {
            setOf("retry", "connect", "disconnect").filter { it.startsWith(args[0].toLowerCase()) }
        } else {
            emptyList()
        }
    }
}