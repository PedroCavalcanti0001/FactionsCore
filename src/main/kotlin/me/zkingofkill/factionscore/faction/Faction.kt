package me.zkingofkill.factionscore.faction

import me.zkingofkill.factionscore.Main
import org.bukkit.Bukkit
import org.bukkit.entity.Player

abstract class Faction {
    abstract val id:Int
    abstract val owner: String
    abstract val name:String
    abstract val foundationDate:Long
    abstract val tag:String
    abstract val power:Int
    abstract val members:MutableList<FPlayer>
    abstract var deleted:Boolean

    fun getMember(player: Player):FPlayer?{
        return members.find { it.player == player.name }
    }

    fun isMember(player: Player): Boolean {
        return getMember(player) != null
    }

    fun getMember(player: String): FPlayer? {
        return members.find { it.player == player }
    }

    fun getAllMembers(): MutableList<FPlayer> {
        val list = members
        val fOwner = Main.singleton.factionManager.getFPlayer(Bukkit.getPlayer(owner))
        if (fOwner != null) {
            list.add(fOwner)
        }
        return list
    }

    fun isMember(player: String): Boolean {
        return getMember(player) != null
    }

}