package com.example.tempcollectorcar

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

private val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

/***
 *  Start a Connection with a device
 */
class ConnectBluetooth(val macDevice:String, val view: View): AsyncTask<Void, Void, String>()
{
    private lateinit var btDevice: BluetoothDevice
    private var connectSuccess: Boolean = true
    lateinit var progress:ProgressDialog

    override fun onPreExecute() {
        progress = ProgressDialog.show(view.context, "Connecting", "Please wait..")
    }
    override fun doInBackground(vararg p0: Void?): String? {

        try {
            Connection.btAdapter!!.cancelDiscovery()
            btDevice = Connection.btAdapter!!.getRemoteDevice(macDevice)
            Connection.mySocket = btDevice.createInsecureRfcommSocketToServiceRecord(MY_UUID)
            Connection.mySocket!!.connect()
        } catch (e: Exception) {
            connectSuccess = false
        }

        return null

    }

    override fun onPostExecute(result: String?) {
        if(!connectSuccess)
        {
            Log.i(Connection.TAG, "Couldn't connect")
            //Toast.makeText(view.context, "Couldn't connect", Toast.LENGTH_SHORT).show()
            //val viewContainer = view.findViewById<ConstraintLayout>(R.id.consLayout)
            //Snackbar.make(test, "Test", Snackbar.LENGTH_SHORT).show()
            Connection.showSnackBar("Couldn't connect, try again.")
        }
        else
        {
            val activity = Intent(view.context, ControlScreen::class.java)
            view.context.startActivity(activity)
        }
        progress.dismiss()
    }

}
/*class ConnectBluetooth(val macDevice:String, val view: View):Thread()
{
    private lateinit var btDevice: BluetoothDevice
    private var btService:MyBluetoothService? = null
    //private var mySocket = Connection.mySocket
    //private var btAdapter = Connection.btAdapter

    override fun run() {
        // Cancel discovery because it otherwise slows down the connection.
        //btAdapter?.cancelDiscovery()
        Connection.btAdapter!!.cancelDiscovery()

        btDevice = Connection.btAdapter!!.getRemoteDevice(macDevice)
        Connection.mySocket = btDevice.createInsecureRfcommSocketToServiceRecord(MY_UUID)

        try {
            Connection.mySocket!!.connect()
        } catch (e: Exception) {
            //Toast.makeText(context, "Cannot be connected", Toast.LENGTH_SHORT).show()
            Log.d(Connection.TAG, "Cannot be Connected")
        }

        if (Connection.mySocket!!.isConnected) {
            val activity = Intent(view.context, ControlScreen::class.java)
            view.context.startActivity(activity)
        } else {
            Log.d(Connection.TAG, "Not Connected")
            Toast.makeText(view.context, "${Connection.mySocket!!.isConnected}", Toast.LENGTH_SHORT).show()
            *//*Toast.makeText(context, "Couldn't connect with ${btDevice.name}, " +
                            "verify your device and try again.", Toast.LENGTH_SHORT ).show()*//*
        }
    }
}*/

