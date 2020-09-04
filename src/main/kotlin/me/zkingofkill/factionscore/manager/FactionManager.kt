package me.zkingofkill.factionscore.manager

import me.zkingofkill.factionscore.Main
import me.zkingofkill.factionscore.faction.FPlayer
import me.zkingofkill.factionscore.faction.Faction
import org.bukkit.entity.Player

class FactionManager {
    val factions = arrayListOf<Faction>()

    fun nextId(): Int {
        return if (factions.isNotEmpty()) factions.maxBy { it.id }!!.id + 1 else 0
    }

    fun getFPlayer(player: Player):FPlayer?{
        return factions.find { it.isMember(player) }?.getMember(player)
    }
}