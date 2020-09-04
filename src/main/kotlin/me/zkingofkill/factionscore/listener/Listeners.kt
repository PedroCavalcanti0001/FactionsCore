package me.zkingofkill.factionscore.listener

import me.zkingofkill.factionscore.Main
import me.zkingofkill.factionscore.faction.FRankPermission
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerMoveEvent

class Listeners : Listener {

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player
        val chunk = event.block.chunk
        Main.singleton.chunkManager.getFactionChunk(chunk) ?: return
        val fPlayer = Main.singleton.factionManager.getFPlayer(player)
        if (fPlayer == null || !fPlayer.fRank.hasPermission(FRankPermission.BREAK)) {
            player.sendMessage(Main.singleton.messages.getString("withoutPermissionToDestroy")
                    .replace("&", "§"))
            event.isCancelled = true
            return
        }
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        val player = event.player
        val chunk = event.block.chunk
        Main.singleton.chunkManager.getFactionChunk(chunk) ?: return
        val fPlayer = Main.singleton.factionManager.getFPlayer(player)
        if (fPlayer == null || !fPlayer.fRank.hasPermission(FRankPermission.BUILD)) {
            player.sendMessage(Main.singleton.messages.getString("withoutPermissionToBuild")
                    .replace("&", "§"))
            event.isCancelled = true
            return
        }
    }

    @EventHandler
    fun onPlayerMove(event: BlockPlaceEvent) {
        val player = event.player
        val chunk = event.block.chunk
        Main.singleton.chunkManager.getFactionChunk(chunk) ?: return
        val fPlayer = Main.singleton.factionManager.getFPlayer(player)
        if (fPlayer == null || !fPlayer.fRank.hasPermission(FRankPermission.BUILD)) {
            player.sendMessage(Main.singleton.messages.getString("withoutPermissionToBuild")
                    .replace("&", "§"))
            event.isCancelled = true
            return
        }
    }

    @EventHandler
    fun onFly(event: PlayerMoveEvent) {
        val player = event.player
        if(!player.isFlying) return
        val chunk = player.location.chunk
        val fChunk = Main.singleton.chunkManager.getFactionChunk(chunk)
        val fPlayer = Main.singleton.factionManager.getFPlayer(player)
        if(fChunk != null || fPlayer == null) {

            if (fPlayer == null || !fPlayer.fRank.hasPermission(FRankPermission.BUILD)) {
                player.sendMessage(Main.singleton.messages.getString("withoutPermissionToBuild")
                        .replace("&", "§"))
                event.isCancelled = true
                return
            }
        }else{
            player.allowFlight = false
            player.sendMessage(Main.singleton.messages.getString("onlyInYourFaction")
                    .replace("&", "§"))
        }
    }
}