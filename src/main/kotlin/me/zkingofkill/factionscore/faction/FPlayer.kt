package me.zkingofkill.factionscore.faction

abstract class FPlayer {
    abstract val player:String
    abstract val fRank:FRank
    abstract fun isInFactionChunk(): Boolean
}