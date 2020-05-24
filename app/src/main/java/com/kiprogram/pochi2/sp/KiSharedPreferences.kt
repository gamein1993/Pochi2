package com.kiprogram.pochi2.sp

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

/**
 * SharedPreferencesを使いやすくしたクラス
 */
class KiSharedPreferences(context: Context) {
    private companion object PrivateStatic {
        private const val SP_NAME: String = "game"
    }
    private val _sp: SharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    private var _editor: SharedPreferences.Editor? = null

    /**
     * [spKey]に[value]を設定します。
     */
    fun setValue(kiSpKey: KiSpKey, value: CharSequence) {
        if (_editor == null) _editor = _sp.edit()
        _editor!!.putString(kiSpKey.text, value.toString())
    }

    fun setValue(kiSpKey: KiSpKey, any: Any) {
        val gson = Gson()
        gson.toJson(any)
    }

    /**
     * 設定した値を保存します。
     */
    fun apply() {
        _editor?.apply()
        _editor = null
    }

    /**
     * [kiSpKey]の値を削除します。
     */
    fun remove(kiSpKey: KiSpKey) {
        if (_editor == null) _editor = _sp.edit()
        _editor!!.remove(kiSpKey.text)
    }
}