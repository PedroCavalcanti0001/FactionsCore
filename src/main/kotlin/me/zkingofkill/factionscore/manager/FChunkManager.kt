package me.zkingofkill.factionscore.manager

import me.zkingofkill.factionscore.Main
import me.zkingofkill.factionscore.faction.FChunk
import me.zkingofkill.factionscore.faction.impl.FChunkImpl
import org.bukkit.Chunk

class FChunkManager {
    val chunks: ArrayList<FChunk> = arrayListOf()

    private fun genNextId(): Int {
        return if (chunks.isNotEmpty()) chunks.maxBy { it.id }!!.id + 1 else 0
    }

    fun getFactionChunk(chunk: Chunk): FChunk? {
        return chunks.find { it.x == chunk.x && chunk.z == it.z }
    }

    fun addFactionChunk(chunk: Chunk, factionId: Int) {
        val factionChunk = getFactionChunk(chunk)
        if (factionChunk != null) {
            changeFChunkOwner(factionChunk, factionId)
        } else {
            val fchunk = FChunkImpl(
                    genNextId(),
                    chunk.x,
                    chunk.z,
                    factionId)
            chunks.add(fchunk)
        }
    }

    fun changeFChunkOwner(fChunk: FChunk, ownerId: Int) {
        fChunk.ownerId = ownerId
    }



}