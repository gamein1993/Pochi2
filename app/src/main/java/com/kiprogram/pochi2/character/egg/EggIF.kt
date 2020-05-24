package com.kiprogram.pochi2.character.egg

import com.kiprogram.pochi2.character.monster.MonsterIF

/**
 * # Eggクラスのインターフェース
 */
interface EggIF {
    val name: String
    val image01: Int
    val image02: Int
    val image03: Int
    val image04: Int
    val sizeX: Int
    val sizeY: Int
    val timeToBeBorn: Long

    /**
     * 産まれたモンスターの情報([MonsterIF])を取得します。
     *
     */
    fun getBornMonster(touchCount: Int): MonsterIF
}