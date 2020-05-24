package com.kiprogram.pochi2.character.egg

import com.kiprogram.pochi2.character.monster.MonsterIF

/**
 * タマゴクラス
 */
class Egg(eggIF: EggIF) {
    private val _eggIF: EggIF = eggIF
    private val _touchCount: Int = 0

    /**
     * 産まれたときの処理
     */
    fun born(): MonsterIF {
        return _eggIF.getBornMonster(_touchCount);
    }
}