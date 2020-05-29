package com.kiprogram.pochi2.character.monster

import android.content.Context
import android.os.Handler
import android.widget.ImageView
import com.kiprogram.pochi2.activity.MainActivity
import com.kiprogram.pochi2.character.egg.EggType
import com.kiprogram.pochi2.sp.KiSharedPreferences
import com.kiprogram.pochi2.sp.KiSpKey
import com.kiprogram.pochi2.util.KiUtil
import kotlin.reflect.KClass

class Monster(context: Context, iv: ImageView, onDieListener: OnDieListener, onEvolveListener: OnEvolveListener? = null, monsterTypeClass: KClass<out MonsterType>? = null) {
    private val _context: Context = context
    private val _sp: KiSharedPreferences = KiSharedPreferences(context)
    private val _iv: ImageView = iv
    private val _onEvolveListener: OnEvolveListener? = onEvolveListener
    private val _onDieListener: OnDieListener = onDieListener
    private val _movingDistance: Int = KiUtil.convertDpToPixel(_context, 20F)

    val monsterType: MonsterType
    var name: String
    var elapsedTime: Long
        private set
    var damage: Long
        private set
    var hungry: Long
        private set
    var exercise: Long
        private set

    init {
        if (monsterTypeClass != null) {
            // タイプ指定がある場合は新規作成
            monsterType = Class.forName(monsterTypeClass.qualifiedName!!).kotlin.objectInstance as MonsterType
            name = "名無し"
            elapsedTime = 0
            damage = 0
            hungry = 0
            exercise = 0

        } else {
            // タイプ指定がない場合は保存済みデータを使用
            val status = _sp.getAny<Status>(KiSpKey.MONSTER_STATUS)!!
            monsterType = Class.forName(status!!.monsterTypeClassName).kotlin.objectInstance as MonsterType
            name = status.name
            elapsedTime = status.elapsedTime
            damage = status.damage
            hungry = status.hungry
            exercise = status.exercise
        }

        _iv.setImageResource(monsterType.leftImageId)
        setImageSize()

        _iv.setOnClickListener(null)
    }

    fun grow(period: Long) {
        elapsedTime += period
        damage += period
        hungry -= period
        exercise -= period

        if (elapsedTime > monsterType.timeForMonsterToEvolve) {
            _onEvolveListener?.onEvolveListener(monsterType.getEvolveMonster()!!)
            return
        }

        if (damage > monsterType.timeToDie) {
            _onDieListener.onDieListener()
            return
        }
    }

    fun move() {
        val random: Int = KiUtil.random(10)
        if (random == 0) left()
        if (random == 1) right()
        if (random == 2) jump()
    }

    fun save() {
        val status = Status(
            monsterType::class.qualifiedName!!,
            name,
            elapsedTime,
            damage,
            hungry,
            exercise
        )
        _sp.setAny(KiSpKey.MONSTER_STATUS, status)
        _sp.apply()
    }

    private fun setImageSize() {
        val sizeX = KiUtil.convertDpToPixel(_context, monsterType.sizeX)
        val sizeY = KiUtil.convertDpToPixel(_context, monsterType.sizeY)
        _iv.layoutParams.width = sizeX
        _iv.layoutParams.height = sizeY
    }

    private fun right() {
        _iv.setImageResource(monsterType.rightImageId)
        _iv.x += _movingDistance
    }

    private fun left() {
        _iv.setImageResource(monsterType.leftImageId)
        _iv.x -= _movingDistance
    }

    private fun jump() {
        _iv.y -= _movingDistance
        Handler().postDelayed(Runnable {
            _iv.y += _movingDistance
        }, MainActivity.PERIOD)
    }

    data class Status(
        val monsterTypeClassName: String,
        val name: String,
        val elapsedTime: Long,
        val damage: Long,
        val hungry: Long,
        val exercise: Long
    )

    interface OnEvolveListener {
        fun onEvolveListener(monsterTypeClass: KClass<out MonsterType>)
    }
    interface OnDieListener {
        fun onDieListener(eggTypeClass: KClass<out EggType>? = null)
    }
}