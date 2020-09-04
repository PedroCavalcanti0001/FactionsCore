package me.zkingofkill.factionscore

import me.zkingofkill.factionscore.command.FactionCommand
import me.zkingofkill.factionscore.database.Mysql
import me.zkingofkill.factionscore.listener.Listeners
import me.zkingofkill.factionscore.manager.DatabaseManager
import me.zkingofkill.factionscore.manager.FChunkManager
import me.zkingofkill.factionscore.manager.FactionManager
import me.zkingofkill.factionscore.manager.InviteManager
import me.zkingofkill.factionscore.tabcompleter.TabCompleter
import org.bukkit.plugin.java.JavaPlugin
import utils.ConfigurationFile
import utils.inventory.InventoryManager
import java.io.File


class Main : JavaPlugin() {

    companion object {
        lateinit var singleton: Main
    }

    lateinit var inventoryManager: InventoryManager
    lateinit var mysql: Mysql

    lateinit var factionManager: FactionManager
    lateinit var chunkManager: FChunkManager
    lateinit var inviteManager: InviteManager
    lateinit var messages:ConfigurationFile

    override fun onEnable() {

        singleton = this
        config.options().copyHeader(true)
        if (!File(dataFolder, "config.yml").exists()) {
            saveDefaultConfig()
            reloadConfig()
        }
        messages = ConfigurationFile(this, "messages.yml","messages.yml")
        messages.saveIfNotExists()

        val command = getCommand("faction")
        command.executor = FactionCommand()
        command.tabCompleter = TabCompleter()

        inventoryManager = InventoryManager(this)
        inventoryManager.init()

        factionManager = FactionManager()
        chunkManager = FChunkManager()
        inviteManager = InviteManager()

        DatabaseManager().startTasks()

        server.pluginManager.registerEvents(Listeners(), this)

        mysql = Mysql()
        mysql.init()
    }

    override fun onDisable() {
    }

}