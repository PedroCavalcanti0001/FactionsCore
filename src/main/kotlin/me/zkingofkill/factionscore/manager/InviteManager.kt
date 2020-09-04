package me.zkingofkill.factionscore.manager

import me.zkingofkill.factionscore.Main
import me.zkingofkill.factionscore.faction.Invite
import me.zkingofkill.factionscore.faction.impl.FPlayerImpl
import org.bukkit.entity.Player

class InviteManager {
    private val invites = arrayListOf<Invite>()

    fun invite(invite: Invite) {
        invites.add(invite)
    }

    fun accept(invite: Invite) {
        val fRank = Main.singleton.factionManager.ranks[0]
        val fPlayer = FPlayerImpl(invite.player, fRank)
        invite.faction.members.add(fPlayer)
        invites.remove(invite)
    }

    fun deny(invite: Invite) {
        invites.remove(invite)
    }

    fun byInvited(player: Player, tag:String):Invite?{
        return invites.find { it.player == player.name && it.faction.tag == tag }
    }
}