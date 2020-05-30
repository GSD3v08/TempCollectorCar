package com.example.tempcollectorcar

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class MessageToast(val context: Context) {

    fun msgToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun msgSnack(root: View, snackMessage: String)
    {
        //Snackbar.make(root, "Couldn't connect, check your device and try again.", Snackbar.LENGTH_SHORT).show()
        Snackbar.make(root, snackMessage, Snackbar.LENGTH_SHORT).show()
    }
}