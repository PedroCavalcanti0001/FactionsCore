package me.zkingofkill.factionscore

import inv.InventoryManager
import org.bukkit.plugin.java.JavaPlugin
import java.io.File


class Main : JavaPlugin() {

    companion object {
        lateinit var singleton: Main
    }

    lateinit var inventoryManager: InventoryManager

    override fun onEnable() {
        singleton = this
        config.options().copyHeader(true)
        if (!File(dataFolder, "config.yml").exists()) {
            saveDefaultConfig()
            reloadConfig()
        }

        inventoryManager = InventoryManager(this)
        inventoryManager.init()
    }

    override fun onDisable() {
    }

}