package com.kiprogram.pochi2.util

import android.content.Context
import android.widget.ImageView
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
    fun convertDpToIntPixel(context: Context, dp: Float): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        val px = dp * (metrics.densityDpi / 160f)
        return px.toInt()
    }

    /**
     * [dp]をピクセルに変換します。
     */
    fun convertDpToFloatPixel(context: Context, dp: Float): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return dp * (metrics.densityDpi / 160f)
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

    fun putInHorizontalCenter(context: Context, iv: ImageView) {
        iv.x = ((context.resources.displayMetrics.widthPixels - iv.layoutParams.width) / 2).toFloat()
    }
}