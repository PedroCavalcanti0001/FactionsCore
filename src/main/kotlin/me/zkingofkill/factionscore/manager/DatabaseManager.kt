package me.zkingofkill.factionscore.manager

import me.zkingofkill.factionscore.Main
import me.zkingofkill.factionscore.Main.Companion.singleton
import org.bukkit.Bukkit

class DatabaseManager {

    init {
        singleton.chunkManager.chunks.addAll(singleton.mysql.chunkTable.loadAll())
        singleton.factionManager.factions.addAll(singleton.mysql.factionsTable.loadAll())
    }

    private fun saveAllFactions() {
        singleton.factionManager.factions.forEach {
            singleton.mysql.factionsTable.upsert(it)
        }
    }

    private fun saveAllChunks() {
        singleton.chunkManager.chunks.forEach {
            singleton.mysql.chunkTable.upsert(it)
        }
    }

    fun startTasks() {
        Bukkit.getServer().scheduler.runTaskTimer(Main.singleton, {
            saveAllChunks()
            saveAllFactions()
        }, 20 * 15 * 60, 20 * 15 * 60)
    }
}