package me.zkingofkill.factionscore.faction.impl

import me.zkingofkill.factionscore.Main
import me.zkingofkill.factionscore.faction.FPlayer
import me.zkingofkill.factionscore.faction.Faction

class FactionImpl(override val id: Int = Main.singleton.factionManager.nextId(),
                  override val owner: String,
                  override val name: String,
                  override val foundationDate: Long,
                  override val tag: String,
                  override val power: Int,
                  override val members: List<FPlayer>) : Faction {


}