package com.kiprogram.pochi2.character.egg

import com.kiprogram.pochi2.R
import com.kiprogram.pochi2.character.monster.Kitten
import com.kiprogram.pochi2.character.monster.MonsterType
import com.kiprogram.pochi2.character.monster.Puppy
import com.kiprogram.pochi2.util.KiUtil
import kotlin.reflect.KClass

/**
 * 白いタマゴ
 */
object WhiteEgg : EggType() {
    override val name: String = "白いタマゴ"
    override val imageId01: Int = R.drawable.egg_white_01
    override val imageId02: Int = R.drawable.egg_white_02
    override val imageId03: Int = R.drawable.egg_white_03
    override val imageId04: Int = R.drawable.egg_white_04
    override val sizeX: Float = 100F
    override val sizeY: Float = 100F
    override val timeForEggToCrack: Long = 60000 // 1分

    /** タマゴから産まれるモンスター */
    enum class BornMonster(val monsterTypeClass: KClass<out MonsterType>) {
        PUPPY(Puppy::class),
        KITTEN(Kitten::class)
    }

    override fun getBornMonster(touchCount: Int): KClass<out MonsterType> {
        // 100回以上タッチしてたら猫確定
        if (touchCount > 100) {
            return BornMonster.KITTEN.monsterTypeClass
        }

        // 90%で犬
        val random = KiUtil.random(10)
        if (random < 9) {
            return BornMonster.PUPPY.monsterTypeClass
        }

        return BornMonster.KITTEN.monsterTypeClass
    }
}

