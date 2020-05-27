package com.kiprogram.pochi2.character.monster

import android.content.Context
import kotlin.reflect.KClass

class Monster(context: Context, onEvolutionListener: OnEvolutionListener, monsterTypeClass: KClass<out MonsterType>? = null) {

    interface OnEvolutionListener {
        fun onEvolutionListener(monsterType: MonsterType)
    }
}