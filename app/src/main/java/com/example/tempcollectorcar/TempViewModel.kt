package com.example.tempcollectorcar

import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class TempViewModel: ViewModel() {

    var temp = MutableLiveData<String>()
    var numAdd = ""

    init {
        temp.value = ""
    }

    fun start()
    {
        val btService = MyBtService()
        btService.start()
    }


    /***
     * Thread that receive data from arduino
     */
    inner class MyBtService: Thread()
    {
        private val mmInStream: InputStream = Connection.mySocket!!.inputStream
        //private val mmOutStream: OutputStream = Connection.mySocket!!.outputStream
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

                    setTemp(message)


                } catch (e: IOException) {
                    Log.d(Connection.TAG, "Input stream was disconnected", e)
                }

            }
        }

        private fun setTemp(msg:String) {
            numAdd += msg
            val endLine = numAdd.indexOf('#')
            if(endLine > 0) {
                //var subIn = numAdd.substring(0, endLine)
                //Toast.makeText(applicationContext, subIn, Toast.LENGTH_SHORT).show();
                if(numAdd.startsWith('~'))
                {
                    var read = numAdd.substring(1, 3) + "°C"

                    temp.postValue(read)

                }
                numAdd = ""
                //subIn = ""
            }
        }

    }
}

