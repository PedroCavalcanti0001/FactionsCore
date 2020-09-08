package me.zkingofkill.factionscore.faction

import fr.minuskube.netherboard.Netherboard
import me.zkingofkill.factionscore.Main
import org.bukkit.Bukkit


abstract class FPlayer {
    abstract val player: String
    abstract val fRank: FRank
    abstract fun isInFactionChunk(): Boolean

    fun updateScoreBoard() {
        val p = Bukkit.getPlayer(player) ?: return
        val faction = Main.singleton.factionManager.factionByPlayer(p)!!
        val board = Netherboard.instance().getBoard(p)
        board.clear()
        Main.singleton.config.getStringList("scoreboard.withFaction")
                .withIndex()
                .forEach { (index, it) ->
                    board.set(it
                            .replace("currentFaction", faction.name)
                            .replace("currentRank", fRank.name), index)
                }
    }
}