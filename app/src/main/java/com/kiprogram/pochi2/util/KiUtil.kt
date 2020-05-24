package com.kiprogram.pochi2.util

import java.util.*

/**
 * # ユーティリティクラス
 * 便利な処理をまとめています。
 */
object KiUtil {
    /**
     * 0以上で[max]より小さい整数を返します。
     */
    fun random(max: Int): Int {
        return Random().nextInt(max)
    }
}