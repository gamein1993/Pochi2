package com.kiprogram.pochi2.character.monster

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.widget.ImageView
import androidx.core.app.NotificationCompat
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
    private val _movingDistance: Int = KiUtil.convertDpToIntPixel(_context, 20F)

    val monsterType: MonsterType
    var name: String
    var elapsedTime: Long
        private set
    var damage: Long
        private set
    var hungry: Long
        private set
    private var isHungry: Boolean
    var exercise: Long
        private set
    private var isBoring: Boolean
    var friendly: Long
        private set

    init {
        if (monsterTypeClass != null) {
            // タイプ指定がある場合は新規作成
            monsterType = Class.forName(monsterTypeClass.qualifiedName!!).kotlin.objectInstance as MonsterType
            name = "名無し"
            elapsedTime = 0
            damage = 0
            hungry = 0
            isHungry = true
            exercise = 0
            isBoring = true
            friendly = 0

            val status = _sp.getAny<Status>(KiSpKey.MONSTER_STATUS)
            status?.let {
                name = it.name
                hungry = it.hungry
                isHungry = it.isHungry
                exercise = it.exercise
                isBoring = it.isBoring
                friendly = it.friendly
            }

        } else {
            // タイプ指定がない場合は保存済みデータを使用
            val status = _sp.getAny<Status>(KiSpKey.MONSTER_STATUS)!!
            monsterType = Class.forName(status!!.monsterTypeClassName).kotlin.objectInstance as MonsterType
            name = status.name
            elapsedTime = status.elapsedTime
            damage = status.damage
            hungry = status.hungry
            isHungry = status.isHungry
            exercise = status.exercise
            isBoring = status.isBoring
            friendly = status.friendly
        }

        _iv.setImageResource(monsterType.leftImageId)
        setImageSize()

        _iv.setOnClickListener(null)
    }

    fun grow(isLooking: Boolean, period: Long = 1000): Boolean {
        elapsedTime += period
        damage += period
        hungry -= period
        exercise -= period

        if (isLooking) {
            friendly += period
        }

        if (hungry <= 0) {
            damage += period
            if (!isHungry) {
                isHungry = true
                makeSound(MainActivity.NOTIFY_ID_MONSTER_HUNGRY, "お腹すいた", Importance.ALERT)
            } else {
                if (KiUtil.random(180) == 0) makeSound(MainActivity.NOTIFY_ID_MONSTER_HUNGRY, "お腹すいた", Importance.ALERT)
            }
        }

        if (exercise <= 0) {
            damage += period

            if (!isBoring) {
                isBoring = true
                makeSound(MainActivity.NOTIFY_ID_MONSTER_BORING, "遊んでほしい", Importance.ALERT)
            } else {
                if (KiUtil.random(180) == 0) makeSound(MainActivity.NOTIFY_ID_MONSTER_BORING, "遊んでほしい", Importance.ALERT)
            }
        }

        if (damage > monsterType.timeToDie) {
            _onDieListener.onDieListener(monsterType.getReincarnateEgg())
            return true
        }

        if (elapsedTime > monsterType.timeForMonsterToEvolve) {
            _onEvolveListener?.onEvolveListener(monsterType.getEvolveMonster()!!)
            return true
        }

        return false
    }

    fun move() {
        val random: Int = KiUtil.random(10)
        if (random == 0) left()
        if (random == 1) right()
        if (random == 2) jump()
    }

    fun right() {

        _iv.setImageResource(monsterType.rightImageId)

        val maxWidth = (_context.resources.displayMetrics.widthPixels - KiUtil.convertDpToIntPixel(_context, monsterType.sizeX)).toFloat()
        if (_iv.x >= maxWidth) {
            _iv.x = maxWidth
        } else {
            _iv.x += _movingDistance
        }
    }

    fun left() {
        _iv.setImageResource(monsterType.leftImageId)

        if (_iv.x <= 0F) {
            _iv.x = 0F
        } else {
            _iv.x -= _movingDistance
        }
    }

    fun jump() {
        _iv.y -= _movingDistance
        Handler().postDelayed(Runnable {
            _iv.y += _movingDistance
        }, MainActivity.PERIOD)
    }

    fun save() {
        val status = Status(
            monsterType::class.qualifiedName!!,
            name,
            elapsedTime,
            damage,
            hungry,
            isHungry,
            exercise,
            isBoring,
            friendly
        )
        _sp.setAny(KiSpKey.MONSTER_STATUS, status)
        _sp.apply()
    }

    private fun setImageSize() {
        _iv.layoutParams.width = KiUtil.convertDpToIntPixel(_context, monsterType.sizeX)
        _iv.layoutParams.height = KiUtil.convertDpToIntPixel(_context, monsterType.sizeY)
    }

    private enum class Importance(val level: Int){
        INFO(android.R.drawable.ic_dialog_info),
        ALERT(android.R.drawable.ic_dialog_alert)
    }
    private fun makeSound(id: Int, feeling: String, importance: Importance) {
        val builder = NotificationCompat.Builder(_context, MainActivity.NOTIFICATION_CHANNEL_ID)
        builder.setContentTitle(name)
        builder.setContentText(feeling)
        builder.setSmallIcon(importance.level)

        val intent = Intent(_context, MainActivity::class.java)
        builder.setContentIntent(PendingIntent.getActivity(_context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT))
        builder.setAutoCancel(true)

        val notification = builder.build()
        val notificationManager = _context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(id, notification)
    }

    data class Status(
        val monsterTypeClassName: String,
        val name: String,
        val elapsedTime: Long,
        val damage: Long,
        val hungry: Long,
        val isHungry: Boolean,
        val exercise: Long,
        val isBoring: Boolean,
        val friendly: Long
    )

    interface OnEvolveListener {
        fun onEvolveListener(monsterTypeClass: KClass<out MonsterType>)
    }
    interface OnDieListener {
        fun onDieListener(eggTypeClass: KClass<out EggType>? = null)
    }
}