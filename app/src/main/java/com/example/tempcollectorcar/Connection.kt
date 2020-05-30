package com.example.tempcollectorcar

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import java.io.IOException

object Connection {
    const val TAG = "MY_APP_DEBUG_TAG"
    var btAdapter: BluetoothAdapter? = null
    var mySocket: BluetoothSocket? = null
    var view: View? = null

    fun cancel() {
        try {
            mySocket?.close()
        } catch (e: IOException) {
            Log.e(TAG, "Could not close the connect socket", e)
        }

        mySocket = null
    }

    fun write(bytes:Int)
    {
        try {
            mySocket!!.outputStream.write((bytes))
        } catch (e: Exception) {
            Log.d(Connection.TAG, "Could not send")
        }
    }

    fun showSnackBar(msg:String)
    {
        var bar:Snackbar = Snackbar.make(view!!, msg, Snackbar.LENGTH_SHORT)
        bar.setBackgroundTint(ContextCompat.getColor(view!!.context,R.color.color_primary_variant)).show()
        //bar.show()

    }
}