package com.kiprogram.pochi2.character.monster

import com.kiprogram.pochi2.R
import com.kiprogram.pochi2.character.egg.EggType
import kotlin.reflect.KClass

object Puppy: MonsterType() {
    override val name: String = "子犬"
    override val level: Level = Level.LEVEL1
    override val leftImageId: Int = R.drawable.dog_puppy_01
    override val rightImageId: Int = R.drawable.dog_puppy_02
    override val sizeX: Float = 100F
    override val sizeY: Float = 100F
    override val timeForMonsterToEvolve: Long = 1_800_000 // 30分
    override val timeToDie: Long = 2_700_000 // 45分 1.5倍
    override val stuffed: Long = 600_000 // 10分 1/3倍
    override val tooMuchExercise: Long = 900_000 // 15分 1/2倍

    override fun getEvolveMonster(): KClass<out MonsterType> {
        return ChihuahuaChocolateTan::class
    }

    override fun getReincarnateEgg(): KClass<out EggType>? {
        return null
    }

}