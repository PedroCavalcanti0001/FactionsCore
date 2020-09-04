package me.zkingofkill.factionscore.manager

import me.zkingofkill.factionscore.Main
import me.zkingofkill.factionscore.faction.FPlayer
import me.zkingofkill.factionscore.faction.FRank
import me.zkingofkill.factionscore.faction.FRankPermission
import me.zkingofkill.factionscore.faction.Faction
import me.zkingofkill.factionscore.faction.impl.FRankImpl
import org.bukkit.entity.Player

class FactionManager {
    val factions = arrayListOf<Faction>()
    val ranks = arrayListOf<FRank>()

    init {
        Main.singleton.config.getConfigurationSection("ranks").getKeys(false).forEach {
            val id = it.toInt()
            val name = Main.singleton.config.getString("ranks.$it.name")
            val permissions = Main.singleton.config.getStringList("ranks.$it.permissions")
                    .map { FRankPermission.valueOf(it.toUpperCase()) }
            ranks.add(FRankImpl(id,name, permissions as ArrayList<FRankPermission>))
        }
    }

    fun nextId(): Int {
        return if (factions.isNotEmpty()) factions.maxBy { it.id }!!.id + 1 else 0
    }

    fun getFPlayer(player: Player): FPlayer? {
        return factions.find { it.isMember(player) && !it.deleted }?.getMember(player)
    }

    fun factionByPlayer(player: Player): Faction? {
        return factions.find { (it.owner == player.name || it.isMember(player)) && !it.deleted }
    }

    fun factionByTag(tag:String): Faction? {
        return factions.find { it.tag == tag && !it.deleted }
    }

    fun factionByOwner(player: Player): Faction? {
        return factions.find { it.owner == player.name && !it.deleted }
    }

    fun createFaction(faction: Faction) {
        factions.add(faction)
    }

    fun removeFaction(faction: Faction) {
        faction.deleted = true
    }
}