package me.zkingofkill.factionscore.faction.impl

import me.zkingofkill.factionscore.Main
import me.zkingofkill.factionscore.faction.FPlayer
import me.zkingofkill.factionscore.faction.FRank
import org.bukkit.Bukkit

class FPlayerImpl(override val player: String, override val fRank: FRank) : FPlayer() {

    override fun isInFactionChunk(): Boolean {
        val faction = Main.singleton.factionManager.factionByPlayer(Bukkit.getPlayer(player)) ?: return false
        return Main.singleton.chunkManager.getFactionChunks(faction)
                .find { it.playersInArea().contains(Bukkit.getPlayer(player)) } != null
    }

}