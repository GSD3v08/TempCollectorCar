package com.example.tempcollectorcar

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_control_screen.*
import kotlinx.android.synthetic.main.activity_control_screen.view.*
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset
import java.text.DecimalFormat
import java.util.UUID


// Defines several constants used when transmitting messages between the
// service and the UI.
private const val TAG = "MY_APP_DEBUG_TAG"

const val MESSAGE_READ: Int = 0
const val MESSAGE_WRITE: Int = 1
const val MESSAGE_TOAST: Int = 2

// ... (Add other message types here as needed.)

class ControlScreen : AppCompatActivity() {

    //private lateinit var btAdapter:BluetoothAdapter
    //private var mySocket: BluetoothSocket? = null
    lateinit var handler:Handler
    lateinit var btService:MyBluetoothService
    var numAdd = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control_screen)

        initService()

    }


    private fun initService() {
        circle_progress.setStartPositionInDegrees(90)
        circle_progress.textSize = 40
        //circle_progress.textColor = Color.
        circle_progress.elevation = 5.0f

        handler = MyHandler(this)
        btService = MyBluetoothService(handler)
        btService.start()

    }

    override fun onDestroy() {
        super.onDestroy()

        //Connection.cancel()
        Log.d(TAG, "Socket Disconnected")
    }


    fun ledTest(view: View) {

        Connection.write(48)
        Toast.makeText(this,"Message Led ON", Toast.LENGTH_SHORT).show()
    }

    fun ledTestOff(view: View) {

        Connection.write(49)
        Toast.makeText(this,"Message Led OFF", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        Connection.cancel()
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}

private class MyHandler(context:ControlScreen):Handler()
{
    val activity = context
    var numAdd = activity.numAdd
    override fun handleMessage(msg: Message) {

        when(msg.what) {

            MESSAGE_READ -> {
                val readBuff = msg.obj as String
                numAdd += readBuff
                val endLine = numAdd.indexOf('#')
                if(endLine > 0) {
                    //var subIn = numAdd.substring(0, endLine)
                    //Toast.makeText(applicationContext, subIn, Toast.LENGTH_SHORT).show();
                    if(numAdd.startsWith('~'))
                    {
                        var read = numAdd.substring(1, 3) + "°C"
                        //activity.temp_number.text = read
                        //activity.progressView.setProgress(numAdd.substring(1, 3).toInt())
                        //activity.donut_progress.setDonut_progress(string.toString())
                        //activity.cpv.setText(numAdd.substring(1, 3))
                        activity.circle_progress.setText(read)
                    }
                    numAdd = ""
                    //subIn = ""
                }
            }
        }
    }
}




