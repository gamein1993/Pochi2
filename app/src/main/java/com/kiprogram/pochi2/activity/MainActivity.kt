package com.kiprogram.pochi2.activity

import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.kiprogram.pochi2.R
import com.kiprogram.pochi2.character.egg.Egg
import com.kiprogram.pochi2.character.egg.EggType
import com.kiprogram.pochi2.character.egg.WhiteEgg
import com.kiprogram.pochi2.character.monster.Monster
import com.kiprogram.pochi2.character.monster.MonsterType
import com.kiprogram.pochi2.sp.KiSharedPreferences
import com.kiprogram.pochi2.sp.KiSpKey
import com.kiprogram.pochi2.util.KiUtil
import java.util.*
import kotlin.reflect.KClass

class MainActivity : AppCompatActivity(), Egg.OnBornListener, Monster.OnEvolveListener, Monster.OnDieListener {
    companion object {
        const val PERIOD: Long = 1000 // 1秒
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

        // コンポーネント取得
        _ivCharacter = findViewById(R.id.ivCharacter)

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
                _egg.crack(PERIOD)
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
        _monster!!.save()

        _timer.schedule(MonsterTimerTask(_monster!!), 0, PERIOD)
    }

    private class MonsterTimerTask(monster: Monster) : TimerTask() {
        private val _monster: Monster = monster
        private val _handler: Handler = Handler()
        override fun run() {
            _handler.post {
                _monster.grow(PERIOD)
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

    }
}
