package com.kiprogram.pochi2.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class KiOkCancelDialog(
    private val _title: String,
    private val _message: String,
    private val _listener: DialogInterface.OnClickListener

) : DialogFragment() {
    companion object {
        const val POSITIVE_BUTTON_TEXT = "はい"
        const val NEGATIVE_BUTTON_TEXT = "いいえ"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(_title)
        builder.setMessage(_message)
        builder.setPositiveButton(POSITIVE_BUTTON_TEXT, _listener)
        builder.setNegativeButton(NEGATIVE_BUTTON_TEXT, _listener)
        return builder.create()
    }
}