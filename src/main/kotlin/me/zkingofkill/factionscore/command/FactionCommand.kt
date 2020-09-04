package me.zkingofkill.factionscore.command

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class FactionCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        // var completions = arrayListOf("create", "who", "tag", "join", "list", "invite", "perm", "fly", "kick", "disband", "sethome")
        return false
    }
}