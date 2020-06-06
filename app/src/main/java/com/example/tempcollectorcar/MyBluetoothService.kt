package com.example.tempcollectorcar

import android.os.Handler
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/***
 * Manage Bluetooth Connected Device
 */
class MyBluetoothService(val handler: Handler): Thread()
{
    private val mmInStream: InputStream = Connection.mySocket!!.inputStream
    private val mmOutStream: OutputStream = Connection.mySocket!!.outputStream
    private val mmBuffer: ByteArray = ByteArray(256) // mmBuffer store for the stream

    override fun run() {
        Log.d(Connection.TAG, "BT Thread Service Started.")
        var numBytes:Int // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs.
        while (true) {
            // Read from the InputStream.
            try {

                numBytes = mmInStream.read(mmBuffer)

                var message = String(mmBuffer, 0, numBytes)
                handler.obtainMessage(
                    MESSAGE_READ,
                    numBytes, -1, message
                ).sendToTarget()
            } catch (e: IOException) {
                Log.d(Connection.TAG, "Input stream was disconnected", e)
            }

        }
    }

}