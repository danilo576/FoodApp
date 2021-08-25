package com.example.foodappsyncit.utils

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.Button
import com.example.foodappsyncit.R
import com.example.foodappsyncit.fragments.UserInfo
import kotlinx.android.synthetic.main.custom_dialog.view.*

class ProfileDialog(
    context: Context?,
    heading: UserInfo,
    userInfo: String?
) {

    private val dialog: AlertDialog
    var view: View = View.inflate(context, R.layout.custom_dialog, null)
    val btnSaveDialog: Button

    init {
        view.tvHeadingDialog.text = setHeadingDialog(heading)
        view.etHeadingDialog.setText(userInfo)
        btnSaveDialog = view.btnSaveDialog
        dialog = createDialog(context)
    }

    private fun createDialog(context: Context?): AlertDialog {
        return AlertDialog.Builder(context).setView(view).create()
    }

    fun showDialog() {
        dialog.show()
    }

    fun dismissDialog() {
        dialog.dismiss()
    }

    fun getUserResult(): String {
        return view.etHeadingDialog.text.toString()
    }

    private fun setHeadingDialog(heading: UserInfo): String {
        return when (heading) {
            UserInfo.FIRST_NAME -> "First Name"
            UserInfo.LAST_NAME -> "Last Name"
            else -> "Phone Number"
        }
    }
}