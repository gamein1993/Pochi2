package com.kiprogram.pochi2.character.egg

import android.content.Context
import android.os.Handler
import android.widget.ImageView
import com.kiprogram.pochi2.activity.MainActivity
import com.kiprogram.pochi2.character.monster.MonsterType
import com.kiprogram.pochi2.sp.KiSharedPreferences
import com.kiprogram.pochi2.sp.KiSpKey
import com.kiprogram.pochi2.util.KiUtil
import kotlin.reflect.KClass

/**
 * タマゴクラス
 */
class Egg(context: Context, iv: ImageView, onBornListener: OnBornListener, eggTypeClass: KClass<out EggType>? = null) {
    private val _context: Context = context
    private val _sp: KiSharedPreferences = KiSharedPreferences(context)
    private val _iv: ImageView = iv
    private val _onBornListener: OnBornListener = onBornListener
    private val _movingDistance: Int = KiUtil.convertDpToPixel(_context, 20F)

    val eggType: EggType
    var elapsedTime: Long
        private set
    var touchCount: Int
        private set

    init {
        if (eggTypeClass != null) {
            // タイプ指定がある場合は新規作成
            elapsedTime = 0
            touchCount = 0
            eggType = Class.forName(eggTypeClass.qualifiedName!!).kotlin.objectInstance as EggType
        } else {
            // タイプ指定がない場合は保存済みデータを使用
            val status = _sp.getAny<Status>(KiSpKey.EGG_STATUS)!!
            elapsedTime = status.elapsedTime
            touchCount = status.touchCount
            eggType = Class.forName(status!!.eggTypeClassName).kotlin.objectInstance as EggType
        }

        setImageResource()
        setImageSize()

        _iv.setOnClickListener {
            touchCount += 1
        }
    }

    fun crack(period: Long) {
        elapsedTime += period

        setImageResource()

        if (elapsedTime > eggType.timeForEggToCrack) {
            _onBornListener.onBornListener(eggType.getBornMonster(touchCount))
        }
    }

    fun save() {
        val status = Status(
            eggType::class.qualifiedName!!,
            elapsedTime,
            touchCount
        )
        _sp.setAny(KiSpKey.EGG_STATUS, status)
        _sp.apply()
    }

    fun jump() {
        _iv.y -= _movingDistance
        Handler().postDelayed(Runnable {
            _iv.y += _movingDistance
        }, MainActivity.PERIOD)
    }

    private fun setImageResource() {
        val rate = elapsedTime.toDouble() / eggType.timeForEggToCrack.toDouble()
        when {
            rate < 0.25 -> {
                _iv.setImageResource(eggType.imageId01)
            }
            rate < 0.5 -> {
                _iv.setImageResource(eggType.imageId02)
            }
            rate < 0.75 -> {
                _iv.setImageResource(eggType.imageId03)
            }
            else -> {
                _iv.setImageResource(eggType.imageId04)
            }
        }
    }

    private fun setImageSize() {
        val sizeX = KiUtil.convertDpToPixel(_context, eggType.sizeX)
        val sizeY = KiUtil.convertDpToPixel(_context, eggType.sizeY)
        _iv.layoutParams.width = sizeX
        _iv.layoutParams.height = sizeY
    }

    data class Status(
        val eggTypeClassName: String,
        val elapsedTime: Long,
        val touchCount: Int
    )

    interface OnBornListener {
        fun onBornListener(monsterTypeClass: KClass<out MonsterType>)
    }

}