package com.kiprogram.pochi2.character.monster

abstract class MonsterType {
    abstract val name: String
    abstract val level: Level
    abstract val rightImageId: Int
    abstract val leftImageId: Int
    abstract val sizeX: Float
    abstract val sizeY: Float
    abstract val timeForMonsterToEvolution: Long
}