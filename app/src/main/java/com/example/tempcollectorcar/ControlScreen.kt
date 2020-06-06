package com.example.tempcollectorcar

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_control_screen.*


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

        /*handler = MyHandler(this)
        btService = MyBluetoothService(handler)
        btService.start()*/

        val viewModel = ViewModelProvider(this).get(TempViewModel::class.java)
        viewModel.start()
        viewModel.temp.observe(this, object: Observer<String>{
            override fun onChanged(t: String?) {
                circle_progress.setText(viewModel.temp.value)
            }

        })

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


/***
 *  Handler no longer needed by implementing ViewModel Class
 */
/*private class MyHandler(context:ControlScreen):Handler()
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
                    if(numAdd.startsWith('~'))
                    {
                        var read = numAdd.substring(1, 3) + "°C"
                        activity.circle_progress.setText(read)
                    }
                    numAdd = ""
                }
            }
        }
    }
}*/




