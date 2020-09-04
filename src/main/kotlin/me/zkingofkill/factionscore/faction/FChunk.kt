package me.zkingofkill.factionscore.faction

import me.zkingofkill.factionscore.Main
import org.bukkit.Bukkit
import org.bukkit.entity.Player

abstract class FChunk {
    abstract val id: Int
    abstract val x: Int
    abstract val z: Int
    abstract val world:String
    abstract var ownerId: Int
    abstract var deleted:Boolean

    fun getOwner(): Faction? {
        return Main.singleton.factionManager.factions.find { it.id == ownerId }
    }

    fun playersInArea():List<Player>{
       return Bukkit.getWorld(world).getChunkAt(x,z).entities.filterIsInstance<Player>()
    }
}