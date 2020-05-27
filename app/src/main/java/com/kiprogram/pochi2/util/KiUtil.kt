package com.kiprogram.pochi2.util

import android.content.Context
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

    /**
     * [dp]をピクセルに変換します。
     */
    fun convertDpToPixel(context: Context, dp: Float): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        val px = dp * (metrics.densityDpi / 160f)
        return px.toInt()
    }

    /**
     * [px]をdpに変換します。
     */
    fun convertPixelsToDp(context: Context, px: Float): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        val dp = px / (metrics.densityDpi / 160f)
        return dp.toInt()
    }
}