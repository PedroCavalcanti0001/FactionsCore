package me.zkingofkill.factionscore.faction

abstract class FRank {
    abstract val id:Int
    abstract val name:String
    abstract val permissions:ArrayList<FRankPermission>

    fun hasPermission(fRankPermission: FRankPermission): Boolean {
        return permissions.contains(fRankPermission)
    }
}