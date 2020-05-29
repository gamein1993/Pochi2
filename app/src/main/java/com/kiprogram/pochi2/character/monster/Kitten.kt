package com.kiprogram.pochi2.character.monster

import com.kiprogram.pochi2.R
import com.kiprogram.pochi2.character.egg.EggType
import kotlin.reflect.KClass

object Kitten : MonsterType() {
    override val name: String = "子猫"
    override val level: Level = Level.LEVEL1
    override val leftImageId: Int = R.drawable.cat_kitten_01
    override val rightImageId: Int = R.drawable.cat_kitten_02
    override val sizeX: Float = 100F
    override val sizeY: Float = 100F
    override val timeForMonsterToEvolve: Long = 3_600_000 // 1時間
    override val timeToDie: Long = 5_400_000 // 1時間半
    override val stuffed: Long = 1_200_000 // 20分
    override val tooMuchExercise: Long = 1_800_000 // 15分

    override fun getEvolveMonster(): KClass<out MonsterType> {
        return Puppy::class
    }

    override fun getReincarnateEgg(): KClass<out EggType>? {
        return null
    }
}