package com.kiprogram.pochi2.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kiprogram.pochi2.R
import com.kiprogram.pochi2.character.egg.Egg
import com.kiprogram.pochi2.character.egg.EggType
import com.kiprogram.pochi2.character.egg.WhiteEgg
import com.kiprogram.pochi2.character.monster.Monster
import com.kiprogram.pochi2.character.monster.MonsterType
import com.kiprogram.pochi2.dialog.KiEditOkDialog
import com.kiprogram.pochi2.dialog.KiOkCancelDialog
import com.kiprogram.pochi2.sp.KiSharedPreferences
import com.kiprogram.pochi2.sp.KiSpKey
import com.kiprogram.pochi2.util.KiUtil
import java.util.*
import kotlin.reflect.KClass

class MainActivity : AppCompatActivity(), Egg.OnBornListener, Monster.OnEvolveListener, Monster.OnDieListener {
    companion object {
        const val PERIOD: Long = 10 // 1秒
        const val NOTIFICATION_CHANNEL_ID: String = "pochi2"

        const val DEAD_FLAG: String = "1"

        const val NOTIFY_ID_MONSTER_HUNGRY: Int = 0
        const val NOTIFY_ID_MONSTER_BORING: Int = 1
    }

    private lateinit var _sp: KiSharedPreferences
    private lateinit var _timer: Timer

    private lateinit var _ivCharacter: ImageView

    private var _egg: Egg? = null
    private var _monster: Monster? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // メンバ変数の初期化
        _sp = KiSharedPreferences(this)
        _timer = Timer()

        // 通知チャネル設定
        val notificationChannel = NotificationChannel("pochi2", "ポチ２", NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)

        // コンポーネント取得
        _ivCharacter = findViewById(R.id.ivCharacter)
        KiUtil.putInHorizontalCenter(this, _ivCharacter)

        if (_sp.getString(KiSpKey.MONSTER_IS_DEAD) == DEAD_FLAG) {
            bury()
            return
        }

        if (_sp.getAny<Monster.Status>(KiSpKey.MONSTER_STATUS) != null) {
            _monster = Monster(this, _ivCharacter, this)
            _timer.schedule(MonsterTimerTask(_monster!!), 0, PERIOD)
            return
        }

        if (_sp.getAny<Egg.Status>(KiSpKey.EGG_STATUS) != null) {
            _egg = Egg(this, _ivCharacter, this)
            _timer.schedule(EggTimerTask(_egg!!), 0, PERIOD)
            return
        }

        // 保存データがなければ初期タマゴ
        _egg = Egg(this, _ivCharacter, this, WhiteEgg::class)
        _timer.schedule(EggTimerTask(_egg!!), 0, PERIOD)

    }

    private class EggTimerTask(egg: Egg) : TimerTask() {
        private val _egg: Egg = egg
        private val _handler: Handler = Handler()
        override fun run() {
            _handler.post {
                if (_egg.crack()) return@post
                _egg.save()
                if (KiUtil.random(5) == 0) _egg.jump()
            }

        }
    }

    override fun onBornListener(monsterTypeClass: KClass<out MonsterType>) {
        _timer.cancel()

        _timer = Timer()
        _egg = null
        _monster = Monster(this, _ivCharacter, this, this, monsterTypeClass)

        _sp.remove(KiSpKey.EGG_STATUS)
        _sp.apply()
        _monster!!.save()

        _timer.schedule(MonsterTimerTask(_monster!!), 0, PERIOD)

        name(_monster!!)
    }

    private class MonsterTimerTask(monster: Monster) : TimerTask() {
        private val _monster: Monster = monster
        private val _handler: Handler = Handler()
        override fun run() {
            _handler.post {
                if (_monster.grow()) return@post
                _monster.save()

                _monster.move()
            }
        }
    }

    override fun onEvolveListener(monsterTypeClass: KClass<out MonsterType>) {
        _timer.cancel()

        _timer = Timer()

        _monster = Monster(this, _ivCharacter, this, this, monsterTypeClass)
        _monster!!.save()

        _timer.schedule(MonsterTimerTask(_monster!!), 0, PERIOD)
    }

    override fun onDieListener(eggTypeClass: KClass<out EggType>?) {
        _timer.cancel()

        _monster = null

        _sp.remove(KiSpKey.MONSTER_STATUS)
        _sp.setValue(KiSpKey.MONSTER_IS_DEAD, DEAD_FLAG)
        eggTypeClass?.let {
            _sp.setValue(KiSpKey.REINCARNATION_EGG, it.qualifiedName!!)
        }
        _sp.apply()

        bury()
    }

    private fun name(monster: Monster) {
        val et = EditText(this)
        et.setText(monster.name, TextView.BufferType.NORMAL)
        KiEditOkDialog(
            "名前登録",
            "名前を付けてあげてください。",
            et,
            DialogInterface.OnClickListener { _: DialogInterface, _: Int ->
                val name = et.text
                if (name.isNotEmpty()) {
                    monster.name = et.text.toString()
                }
            }).show(supportFragmentManager, "named")
    }

    private fun bury() {
        _egg = null
        _monster = null
        _ivCharacter.setImageResource(R.drawable.grave_01)
        _ivCharacter.layoutParams.width = KiUtil.convertDpToIntPixel(applicationContext, 150F)
        _ivCharacter.layoutParams.height = KiUtil.convertDpToIntPixel(applicationContext, 150F)
        KiUtil.putInHorizontalCenter(this, _ivCharacter)
        _ivCharacter.setOnClickListener {
            KiOkCancelDialog(
                "また次の命が待っています",
                "卵から育て直しますか？",
                DialogInterface.OnClickListener { _: DialogInterface, which: Int ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            val reincarnationEggClassName = _sp.getString(KiSpKey.REINCARNATION_EGG)
                            _egg = if (reincarnationEggClassName != null) {
                                val clazz = Class.forName(reincarnationEggClassName).kotlin as KClass<out EggType>
                                Egg(this, _ivCharacter, this, clazz)
                            } else {
                                Egg(this, _ivCharacter, this, WhiteEgg::class)
                            }

                            _sp.remove(KiSpKey.MONSTER_IS_DEAD)
                            _sp.remove(KiSpKey.REINCARNATION_EGG)
                            _sp.apply()

                            _timer = Timer()
                            _timer.schedule(EggTimerTask(_egg!!), 0, PERIOD)
                        }
                    }
                }).show(supportFragmentManager, "restart")
        }
    }
}
