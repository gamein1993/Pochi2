package com.kiprogram.pochi2.character.monster

import com.kiprogram.pochi2.R
import com.kiprogram.pochi2.character.egg.EggType
import kotlin.reflect.KClass

object ChihuahuaChocolateTan : MonsterType() {
    override val name: String = "チワワ (チョコタン)"
    override val level: Level = Level.LEVEL2
    override val leftImageId: Int = R.drawable.dog_chihuahua_choco_tan_01
    override val rightImageId: Int = R.drawable.dog_chihuahua_choco_tan_02
    override val sizeX: Float = 150F
    override val sizeY: Float = 150F
    override val timeForMonsterToEvolve: Long = 86_400_000 // 1日
    override val timeToDie: Long = 129_600_000 // 1.5日 1.5倍
    override val stuffed: Long = 28_800_000 // 8時間 1/3倍
    override val tooMuchExercise: Long = 43_200_000 // 12時間 1/2倍

    override fun getEvolveMonster(): KClass<out MonsterType> {
        return Kitten::class
    }

    override fun getReincarnateEgg(): KClass<out EggType>? {
        return null
    }
}