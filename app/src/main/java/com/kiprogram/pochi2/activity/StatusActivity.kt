package com.kiprogram.pochi2.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kiprogram.pochi2.R
import com.kiprogram.pochi2.character.monster.MonsterType
import com.kiprogram.pochi2.util.KiUtil

class StatusActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_FIELD_MONSTER_TYPE: String = "monster_type"
        const val EXTRA_FIELD_NAME: String = "name"
        const val EXTRA_FIELD_HUNGRY: String = "hungry"
        const val EXTRA_FIELD_EXERCISE: String = "exercise"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)
        // EXTRAデータ取得
        val monsterType = intent.getSerializableExtra(EXTRA_FIELD_MONSTER_TYPE)
        val name = intent.getStringExtra(EXTRA_FIELD_NAME)
        val hungry = intent.getLongExtra(EXTRA_FIELD_HUNGRY, 0)
        val exercise = intent.getLongExtra(EXTRA_FIELD_EXERCISE, 0)

        // コンポーネント取得
        val tvTypeName = findViewById<TextView>(R.id.tvTypeName)
        val ivCharacter = findViewById<ImageView>(R.id.ivCharacter)
        val tvName = findViewById<TextView>(R.id.tvName)
        val tvHungry = findViewById<TextView>(R.id.tvHungry)
        val tvExercise = findViewById<TextView>(R.id.tvExercise)

        // 設定
        monsterType as MonsterType
        tvTypeName.text = monsterType.name
        ivCharacter.setImageResource(monsterType.leftImageId)
        ivCharacter.layoutParams.width = KiUtil.convertDpToIntPixel(this, monsterType.sizeX)
        ivCharacter.layoutParams.height = KiUtil.convertDpToIntPixel(this, monsterType.sizeY)
        tvName.text = name
        val hungryPercentage = hungry.toDouble().div(monsterType.stuffed) * 100
        tvHungry.text = "${hungryPercentage.toInt()}%"
        val exercisePercentage = exercise.toDouble().div(monsterType.tooMuchExercise) * 100
        tvExercise.text = "${exercisePercentage.toInt()}%"


    }
}