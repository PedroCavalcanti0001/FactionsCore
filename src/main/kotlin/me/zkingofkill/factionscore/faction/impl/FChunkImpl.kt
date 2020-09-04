package me.zkingofkill.factionscore.faction.impl

import me.zkingofkill.factionscore.faction.FChunk
import me.zkingofkill.factionscore.faction.FPlayer
import me.zkingofkill.factionscore.faction.FRankPermission
import me.zkingofkill.factionscore.faction.Faction

class FChunkImpl(override val id: Int,
                 override val x: Int,
                 override val z: Int,
                 override var ownerId: Int) : FChunk() {
}