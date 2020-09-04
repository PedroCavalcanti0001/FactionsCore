package me.zkingofkill.factionscore.tabcompleter

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.util.StringUtil

class TabCompleter : TabCompleter {
    override fun onTabComplete(sender: CommandSender, command: Command?, alias: String, args: Array<out String>): MutableList<String> {
        val commands = arrayListOf("faction", "f", "fac")
        var completions = arrayListOf("create", "who", "tag", "join", "list", "invite", "perm", "fly", "kick", "disband", "sethome")
        completions = StringUtil.copyPartialMatches(args[0], commands, completions)
        completions.sort()
        return completions
    }


}