package me.zkingofkill.factionscore.faction

import me.zkingofkill.factionscore.Main

abstract class FChunk {
    abstract val id: Int
    abstract val x: Int
    abstract val z: Int
    abstract var ownerId: Int

    fun getOwner(): Faction? {
        return Main.singleton.factionManager.factions.find { it.id == ownerId }
    }
}