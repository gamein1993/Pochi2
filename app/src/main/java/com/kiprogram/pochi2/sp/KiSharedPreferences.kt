package com.kiprogram.pochi2.sp

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

/**
 * SharedPreferencesを使いやすくしたクラス
 */
class KiSharedPreferences(context: Context) {
    companion object {
        const val SP_NAME: String = "game"
    }
    private val _sp: SharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    private var _editor: SharedPreferences.Editor? = null

    /**
     * [kiSpKey]に[value]を設定します。
     */
    fun setValue(kiSpKey: KiSpKey, value: CharSequence) {
        if (_editor == null) _editor = _sp.edit()
        _editor!!.putString(kiSpKey.text, value.toString())
    }

    /**
     * [kiSpKey]に[value]を設定します。
     */
    fun setValue(kiSpKey: KiSpKey, value: Int) {
        setValue(kiSpKey, value.toString(10))
    }

    /**
     * [kiSpKey]に[any]を設定します。
     */
    fun setAny(kiSpKey: KiSpKey, any: Any) {
        setValue(kiSpKey, Gson().toJson(any))
    }

    /**
     * [kiSpKey]の値を取得します。
     */
    fun getString(kiSpKey: KiSpKey): String? {
        return _sp.getString(kiSpKey.text, null)
    }

    /**
     * [kiSpKey]の値を取得します。
     * 値が設定されていない場合 Int.MIN_VALUE
     */
    fun getInt(kiSpKey: KiSpKey): Int {
        return _sp.getInt(kiSpKey.text, Int.MIN_VALUE)
    }

    /**
     * [kiSpKey]のクラスを取得します。
     */
    inline fun <reified T> getAny(kiSpKey: KiSpKey): T? {
        return Gson().fromJson(getString(kiSpKey), T::class.java)
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