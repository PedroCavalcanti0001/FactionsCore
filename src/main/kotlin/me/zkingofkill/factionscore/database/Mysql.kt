package me.zkingofkill.factionscore.database

import me.zkingofkill.factionscore.Main
import me.zkingofkill.factionscore.Main.Companion.singleton
import java.sql.Connection
import java.sql.DriverManager
import java.util.concurrent.CompletableFuture


class Mysql {
    val chunkTable = ChunkTable()
    val factionsTable = FactionsTable()


    fun init() {
        chunkTable.init()
        factionsTable.init()
    }

    companion object {
        private var con:Connection? = null
        fun openCon(): Connection? {
            if (con != null && !con!!.isClosed) return con
            try {
                val password = singleton.config.getString("mysql.password")
                val user = singleton.config.getString("mysql.user")
                val host = singleton.config.getString("mysql.host")
                val port = singleton.config.getInt("mysql.port")
                val database = singleton.config.getString("mysql.mysql")
                val type = "jdbc:mysql://"
                val url = "$type$host:$port/$database"
                return DriverManager.getConnection(url, user, password)

            } catch (e: Exception) {
                println("  ")
                println("  ")
                println("[FactionsCore] O plugin não se conectou ao mysql por favor verifique sua configuração.")
                println("  ")
                println("  ")
                singleton.pluginLoader.disablePlugin(singleton)
            }

            return null
        }
    }



}