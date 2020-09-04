package me.zkingofkill.factionscore.command

import me.zkingofkill.factionscore.Main
import me.zkingofkill.factionscore.faction.FPlayer
import me.zkingofkill.factionscore.faction.FRankPermission
import me.zkingofkill.factionscore.faction.impl.FPlayerImpl
import me.zkingofkill.factionscore.faction.impl.FactionImpl
import org.apache.commons.lang.StringUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class FactionCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("§cEsse comando é apenas para jogadores!")
            return false
        }
        if (args.isEmpty()) {
            sender.sendMessage("§c/$label create <tag> <name>")
            sender.sendMessage("§c/$label who <tag>")
            sender.sendMessage("§c/$label invite <player>")
            sender.sendMessage("§c/$label fly")
            sender.sendMessage("§c/$label kick <player>")
            sender.sendMessage("§c/$label accept <tag>")
            sender.sendMessage("§c/$label deny <tag>")
            sender.sendMessage("§c/$label disband")
            sender.sendMessage("§c/$label sethome")
            sender.sendMessage("§c/$label list")
            if (sender.hasPermission("factionscore.admin")) {
                sender.sendMessage("§c/$label join <tag>")
            }
        } else {
            val fManager = Main.singleton.factionManager
            when (args[0]) {
                "join" -> {
                    val faction = fManager.factionByTag(args[1])
                    if (faction != null) {
                        val fPlayer = fManager.getFPlayer(sender) as FPlayer
                        if (fPlayer.fRank.hasPermission(FRankPermission.KICK)) {
                            if (args.size >= 2) {
                                if (faction.isMember(args[1])) {
                                    val fRank = Main.singleton.factionManager.ranks[0]
                                    val fPlayer = FPlayerImpl(sender.name, fRank)
                                    faction.members.add(fPlayer)
                                    sender.sendMessage(Main.singleton.messages.getString("joined").replace("&", "§"))
                                } else {
                                    sender.sendMessage(Main.singleton.messages.getString("memberNotFound").replace("&", "§"))
                                }
                            } else {
                                sender.sendMessage("§c/$label join <player>")
                            }
                        } else {
                            sender.sendMessage(Main.singleton.messages.getString("withoutPermissionToKick").replace("&", "§"))
                        }
                    }
                    return true
                }
                "create" -> {
                    val factionByPlayer = fManager.factionByPlayer(sender)
                    if (factionByPlayer == null) {
                        val tag = args[1]
                        val name = StringUtils.join(args, " ", 1, args.size)
                        val faction = FactionImpl(owner = sender.name, tag = tag, name = name)
                        Main.singleton.factionManager.createFaction(faction)
                        sender.sendMessage(Main.singleton.messages.getString("created").replace("&", "§"))
                    } else {
                        sender.sendMessage(Main.singleton.messages.getString("youAlreadyHaveAFaction").replace("&", "§"))
                    }
                    return true
                }
                "disband" -> {
                    val factionByPlayer = fManager.factionByPlayer(sender)
                    if (factionByPlayer != null) {
                        val fPlayer = fManager.getFPlayer(sender) as FPlayer
                        if (fPlayer.fRank.hasPermission(FRankPermission.DISBAND)) {
                            fManager.removeFaction(factionByPlayer)
                            sender.sendMessage(Main.singleton.messages.getString("deleted").replace("&", "§"))
                        } else {
                            sender.sendMessage(Main.singleton.messages.getString("withoutPermissionToDisband").replace("&", "§"))
                        }
                    } else {
                        sender.sendMessage(Main.singleton.messages.getString("youHaveNoFaction").replace("&", "§"))
                    }

                    return true
                }
                "fly" -> {
                    val factionByPlayer = fManager.factionByPlayer(sender)
                    if (factionByPlayer != null) {
                        val fPlayer = fManager.getFPlayer(sender) as FPlayer
                        if (fPlayer.fRank.hasPermission(FRankPermission.FLY)) {
                            sender.allowFlight = true
                            sender.sendMessage(Main.singleton.messages.getString("flyActivated").replace("&", "§"))
                        } else {
                            sender.sendMessage(Main.singleton.messages.getString("withoutPermissionToFly").replace("&", "§"))
                        }
                    } else {
                        sender.sendMessage(Main.singleton.messages.getString("youHaveNoFaction").replace("&", "§"))
                    }
                    return true
                }

                "list" -> {
                    fManager.factions
                            .map { it.name }
                            .withIndex()
                            .forEach { (index, it) ->
                                run {
                                    sender.sendMessage("${index + 1}º $it")
                                }
                            }
                    return true
                }
                "kick" -> {
                    val factionByPlayer = fManager.factionByPlayer(sender)
                    if (factionByPlayer != null) {
                        val fPlayer = fManager.getFPlayer(sender) as FPlayer
                        if (fPlayer.fRank.hasPermission(FRankPermission.KICK)) {
                            if (args.size >= 2) {
                                if (factionByPlayer.isMember(args[1])) {
                                    val member = factionByPlayer.getMember(args[1]) as FPlayer
                                    factionByPlayer.members.remove(member)
                                    sender.sendMessage(Main.singleton.messages.getString("kickedMember").replace("&", "§"))
                                } else {
                                    sender.sendMessage(Main.singleton.messages.getString("memberNotFound").replace("&", "§"))
                                }
                            } else {
                                sender.sendMessage("§c/$label kick <player>")
                            }
                        } else {
                            sender.sendMessage(Main.singleton.messages.getString("withoutPermissionToKick").replace("&", "§"))
                        }
                    } else {
                        sender.sendMessage(Main.singleton.messages.getString("youHaveNoFaction").replace("&", "§"))
                    }
                    return true
                }

                "accept" -> {
                    if (args.size >= 2) {
                        val invite = Main.singleton.inviteManager.byInvited(sender, args[1])
                        if (invite != null) {
                            Main.singleton.inviteManager.accept(invite)
                            sender.sendMessage(Main.singleton.messages.getString("invitationAccepted").replace("&", "§"))
                        } else {
                            sender.sendMessage(Main.singleton.messages.getString("inviteNotFound").replace("&", "§"))
                        }
                    } else {
                        sender.sendMessage("§c/$label accept <tag>")
                    }
                    return true
                }
                "who" -> {
                    if (args.size >= 2) {
                        val faction = fManager.factionByTag(args[1])
                        if (faction != null) {
                            sender.sendMessage("§aName: §7${faction.name}")
                            sender.sendMessage("§atag: §7${faction.tag}")
                            sender.sendMessage("§aMembers:")
                            sender.sendMessage(faction.members.joinToString("§7, §a", prefix = "§a"))

                        } else {
                            sender.sendMessage(Main.singleton.messages.getString("factionNotFound").replace("&", "§"))
                        }
                    } else {
                        sender.sendMessage("§c/$label who <tag>")
                    }
                    return true
                }

                "deny" -> {
                    if (args.size >= 2) {
                        val invite = Main.singleton.inviteManager.byInvited(sender, args[1])
                        if (invite != null) {
                            Main.singleton.inviteManager.deny(invite)
                            sender.sendMessage(Main.singleton.messages.getString("invitationDenied").replace("&", "§"))
                        } else {
                            sender.sendMessage(Main.singleton.messages.getString("inviteNotFound").replace("&", "§"))
                        }
                    } else {
                        sender.sendMessage("§c/$label deny <tag>")
                    }
                    return true
                }
            }
        }

        return false
    }
}