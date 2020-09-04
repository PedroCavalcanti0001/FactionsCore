package me.zkingofkill.factionscore.database

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import me.zkingofkill.factionscore.faction.FPlayer
import me.zkingofkill.factionscore.faction.Faction
import me.zkingofkill.factionscore.faction.impl.FPlayerImpl
import me.zkingofkill.factionscore.faction.impl.FactionImpl

class FactionsTable {

    fun init() {
        createTableFactions()
    }

    private fun createTableFactions() {
        try {
            val con = Mysql.openCon()
            var query = "CREATE TABLE IF NOT EXISTS factionscore_factions" +
                    "(`id` INTEGER, " +
                    "`name` VARCHAR(25), " +
                    "`owner` VARCHAR(25), " +
                    "`foundationDate` BIGINT, " +
                    "`power` INTEGER, " +
                    "`members` LONGTEXT NOT NULL," +
                    "PRIMARY KEY (`id`));"
            val preparedStatement = con?.prepareStatement(query)
            preparedStatement?.execute()
            preparedStatement?.close()
            con?.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    fun delete(id: Int) {

        try {
            val con = Mysql.openCon()
            val query = "DELETE FROM factionscore_chunks WHERE id = $id;"
            val preparedStatement = con?.prepareStatement(query)
            preparedStatement?.execute()
            preparedStatement?.close()
            con?.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun upsert(faction: Faction) {
        try {
            val type = object : TypeToken<ArrayList<FPlayerImpl>>() {}.type
            val con = Mysql.openCon()
            val query = "INSERT INTO factionscore_factions(id,name,owner,foundationDate,power,members)" +
                    " VALUES (?,?,?,?,?,?) " +
                    "ON DUPLICATE KEY UPDATE id =?,name = ?, owner = ?,foundationDate = ?,power = ?,members = ?;"
            val insert = con!!.prepareStatement(query)
            insert.setInt(1, faction.id)
            insert.setString(2, faction.name)
            insert.setString(3, faction.owner)
            insert.setLong(4, faction.foundationDate)
            insert.setInt(5, faction.power)
            insert.setString(6, Gson().toJson(faction.members, type))

            insert.setInt(7, faction.id)
            insert.setString(8, faction.name)
            insert.setString(9, faction.owner)
            insert.setLong(10, faction.foundationDate)
            insert.setInt(11, faction.power)
            insert.setString(12, Gson().toJson(faction.members, type))
            insert.execute()
            insert.close()
            con.close()
        } catch (e: Exception) {
            e.printStackTrace()

        }
    }

    fun loadAll(): ArrayList<Faction> {
        val con = Mysql.openCon()
        val results = arrayListOf<Faction>()
        val query = "SELECT * FROM factionscore_chunks;"

        val preparedStatement = con?.prepareStatement(query)
        val resultSet = preparedStatement?.executeQuery()
        assert(resultSet != null) {
            println("ocorreu um problema ao carregar as facções!")
        }
        val gson = Gson()
        val type = object : TypeToken<ArrayList<FPlayerImpl>>() {}.type
        while (resultSet!!.next()) {
            val id = resultSet.getInt("id")
            val name = resultSet.getString("name")
            val owner = resultSet.getString("owner")
            val foundationDate = resultSet.getLong("foundationDate")
            val power = resultSet.getInt("power")
            val tag = resultSet.getString("tag")
            val membersStr = resultSet.getString("members")
            val members = gson.fromJson<ArrayList<FPlayer>>(membersStr, type)
            val faction = FactionImpl(id, owner, name, foundationDate, tag, power, members)
            results.add(faction)
        }
        preparedStatement.close()
        con.close()
        return results
    }
}