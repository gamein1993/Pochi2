package com.kiprogram.pochi2.character.monster

import com.kiprogram.pochi2.character.egg.EggType
import java.io.Serializable
import kotlin.reflect.KClass

abstract class MonsterType : Serializable {
    abstract val name: String
    abstract val level: Level
    abstract val leftImageId: Int
    abstract val rightImageId: Int
    abstract val sizeX: Float
    abstract val sizeY: Float
    abstract val timeForMonsterToEvolve: Long
    abstract val timeToDie: Long
    abstract val stuffed: Long
    abstract val tooMuchExercise: Long
    abstract fun getEvolveMonster(): KClass<out MonsterType>?
    abstract fun getReincarnateEgg(): KClass<out EggType>?

}