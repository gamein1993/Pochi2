package com.kiprogram.pochi2.character.egg

import com.kiprogram.pochi2.character.monster.MonsterType
import java.io.Serializable
import kotlin.reflect.KClass

abstract class EggType : Serializable {
    abstract val name: String
    abstract val imageId01: Int
    abstract val imageId02: Int
    abstract val imageId03: Int
    abstract val imageId04: Int
    abstract val sizeX: Float
    abstract val sizeY: Float
    abstract val timeForEggToCrack: Long
    abstract fun getBornMonster(touchCount: Int): KClass<out MonsterType>
}