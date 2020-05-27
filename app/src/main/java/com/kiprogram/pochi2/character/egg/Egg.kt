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
    private val _status: Status
    private val _eggType: EggType
    private val _movingDistance: Int

    init {
        _status = if (eggTypeClass != null) {
            // タイプ指定がある場合は新規作成
            Status(eggTypeClass.qualifiedName!!)
        } else {
            // タイプ指定がない場合は保存済みデータを使用
            _sp.getAny(KiSpKey.EGG_STATUS)!!
        }

        _eggType = Class.forName(_status!!.eggTypeClassName).kotlin.objectInstance as EggType

        setImageResource()
        setImageSize()

        _movingDistance = KiUtil.convertDpToPixel(_context, 20F)
    }



    fun crack(period: Long) {
        _status.elapsedTime += period

        setImageResource()

        if (_status.elapsedTime > _eggType.timeForEggToCrack) {
            _onBornListener.onBornListener(_eggType.getBornMonster(_status.touchCount))
        }
    }

    fun save() {
        _sp.setAny(KiSpKey.EGG_STATUS, _status)
        _sp.apply()
    }

    fun isTouched() {
        _status.touchCount += 1
    }


    fun jump() {
        _iv.y -= _movingDistance
        Handler().postDelayed(Runnable {
            _iv.y += _movingDistance
        }, MainActivity.PERIOD)
    }


    private fun setImageResource() {
        val rate = _status.elapsedTime.toDouble() / _eggType.timeForEggToCrack.toDouble()
        when {
            rate < 0.25 -> {
                _iv.setImageResource(_eggType.imageId01)
            }
            rate < 0.5 -> {
                _iv.setImageResource(_eggType.imageId02)
            }
            rate < 0.75 -> {
                _iv.setImageResource(_eggType.imageId03)
            }
            else -> {
                _iv.setImageResource(_eggType.imageId04)
            }
        }
    }

    private fun setImageSize() {
        val sizeX = KiUtil.convertDpToPixel(_context, _eggType.sizeX)
        val sizeY = KiUtil.convertDpToPixel(_context, _eggType.sizeY)
        _iv.layoutParams.width = sizeX
        _iv.layoutParams.height = sizeY
    }

    data class Status(
        val eggTypeClassName: String
    ) {
        var elapsedTime: Long = 0
        var touchCount: Int = 0
    }

    interface OnBornListener {
        fun onBornListener(monsterTypeClass: KClass<out MonsterType>)
    }

}