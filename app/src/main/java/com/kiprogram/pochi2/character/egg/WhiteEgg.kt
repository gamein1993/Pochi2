package com.kiprogram.pochi2.character.egg

import com.kiprogram.pochi2.R
import com.kiprogram.pochi2.character.monster.Kitten
import com.kiprogram.pochi2.character.monster.MonsterIF
import com.kiprogram.pochi2.character.monster.Puppy
import com.kiprogram.pochi2.util.KiUtil

/**
 * 白いタマゴ
 */
data class WhiteEgg(
    override val name: String = "white",
    override val image01: Int = R.drawable.egg_white_01,
    override val image02: Int = R.drawable.egg_white_02,
    override val image03: Int = R.drawable.egg_white_03,
    override val image04: Int = R.drawable.egg_white_04,
    override val sizeX: Int = 100,
    override val sizeY: Int = 100,
    override val timeToBeBorn: Long = 60000 // 1分
) : EggIF {
    private companion object PrivateStatic {
        /**
         * タマゴから産まれるモンスター
         */
        private enum class BornMonster(val monsterIF: MonsterIF) {
            PUPPY(Puppy()),
            KITTEN(Kitten())
        }
    }

    override fun getBornMonster(touchCount: Int): MonsterIF {
        // 100回以上タッチしてたら猫確定
        if (touchCount > 100) {
            return BornMonster.KITTEN.monsterIF
        }

        // 90%で犬
        val random = KiUtil.random(10)
        if (random < 9) {
            return BornMonster.PUPPY.monsterIF
        }

        return BornMonster.KITTEN.monsterIF
    }
}

