package me.zkingofkill.factionscore.faction.impl

import me.zkingofkill.factionscore.faction.FRank
import me.zkingofkill.factionscore.faction.FRankPermission

class FRankImpl(override val id: Int,
                override val name: String,
                override val permissions: ArrayList<FRankPermission>) : FRank() {

}