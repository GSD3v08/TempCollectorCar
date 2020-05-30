package com.example.tempcollectorcar

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReceiverBluetooth(maContext:MainActivity): BroadcastReceiver() {

    val actMain = maContext
    override fun onReceive(context: Context, intent: Intent) {

        val action: String? = intent?.action
        when(action) {
            BluetoothDevice.ACTION_FOUND -> {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

                //activityM.msgToast("Receiver Message")
                actMain.checkDeviceInList(device)
            }
            BluetoothAdapter.ACTION_DISCOVERY_FINISHED ->{

                //Toast.makeText(context, "Discovery Finished ${context.toString()}", Toast.LENGTH_SHORT).show()
                //activityM.msgToast("Discovery Finished", context)
                //Snackbar.make(consLayout, "Discovery finished", Snackbar.LENGTH_SHORT).show()

            }
        }
    }


}