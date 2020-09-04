package me.zkingofkill.factionscore.faction

import org.bukkit.entity.Player

abstract class Faction {
    abstract val id:Int
    abstract val owner: String
    abstract val name:String
    abstract val foundationDate:Long
    abstract val tag:String
    abstract val power:Int
    abstract val members:List<FPlayer>

    fun getMember(player: Player):FPlayer?{
        return members.find { it.player == player.name }
    }

    fun isMember(player: Player):Boolean{
        return getMember(player) != null
    }

}