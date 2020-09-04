package me.zkingofkill.factionscore.database

import com.google.gson.reflect.TypeToken
import me.zkingofkill.factionscore.faction.FChunk
import me.zkingofkill.factionscore.faction.impl.FChunkImpl
import me.zkingofkill.factionscore.faction.impl.FPlayerImpl


class ChunkTable {

    fun init() {
        createTableChunks()
    }

    private fun createTableChunks() {
        try {
            val con = Mysql.openCon()
            var query = "CREATE TABLE IF NOT EXISTS factionscore_chunks" +
                    "(`id` INTEGER, " +
                    "`x` INTEGER, " +
                    "`z` INTEGER, " +
                    "`world` VARCHAR(100), " +
                    "`ownerId` INTEGER, " +
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
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun loadAll(): ArrayList<FChunk> {
        val con = Mysql.openCon()
        val results = arrayListOf<FChunk>()
        val query = "SELECT * FROM factionscore_chunks;"

        val preparedStatement = con?.prepareStatement(query)
        val resultSet = preparedStatement?.executeQuery()

        assert(resultSet != null) {
            println("ocorreu um problema ao carregar as facções!")
        }

        while (resultSet!!.next()) {
            val id = resultSet.getInt("id")
            val x = resultSet.getInt("x")
            val z = resultSet.getInt("z")
            val ownerId = resultSet.getInt("ownerId")
            val world = resultSet.getString("world")
            val fChunk = FChunkImpl(id, x,z,ownerId, world)
            results.add(fChunk)
        }
        preparedStatement.close()
        con.close()
        return results
    }

    fun upsert(fchunk: FChunk) {
        try {
            val type = object : TypeToken<ArrayList<FPlayerImpl>>() {}.type
            val con = Mysql.openCon()
            val query = "INSERT INTO factionscore_chunks(id,x,z,ownerId)" +
                    " VALUES (?,?,?,?,?,?) " +
                    "ON DUPLICATE KEY UPDATE id =?,x = ?,z = ?,ownerId = ?;"
            val insert = con!!.prepareStatement(query)
            insert.setInt(1, fchunk.id)
            insert.setInt(2, fchunk.x)
            insert.setInt(3, fchunk.z)
            insert.setInt(4, fchunk.ownerId)
            insert.execute()
            insert.close()
            con.close()
        } catch (e: Exception) {
            e.printStackTrace()

        }
    }
}