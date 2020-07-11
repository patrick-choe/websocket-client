package com.github.patrick.websocket.client

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class WebSocketClientCommand : CommandExecutor, TabCompleter {
    private val instance =
        WebSocketClientPlugin.INSTANCE

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
                    else -> {
                        sender.sendMessage("/$label retry connect disconnect")
                    }
                }
            }
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