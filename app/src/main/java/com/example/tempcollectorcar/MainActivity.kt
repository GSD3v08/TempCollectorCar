package com.example.tempcollectorcar

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_dialog_list.*

class MainActivity : AppCompatActivity() {

    private val REQUEST_ENABLE_BT = 1

    private val devicesList = ArrayList<Devices>()
    private val btDevicesList = ArrayList<BluetoothDevice>()

    private val pairedDeviceList = ArrayList<Devices>()
    private lateinit var pairedBtList:Set<BluetoothDevice>

    private lateinit var adapter: DeviceAdapter
    private lateinit var pairedAdapter: DeviceAdapter

    private lateinit var dialog: AlertDialog
    private lateinit var receiver :ReceiverBluetooth
    private lateinit var message:MessageToast

    //private var btAdapter = Connection.btAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        message = MessageToast(this)

        //startVariables()
        verifyDevice()
        regReceiver()

    }


    private var listener = object: OnItemClickListener{
        override fun OnItemClicked() {
            dialog.dismiss()
        }

    }

    /***
     * Verifies if device has Bluetooth
     */
    private fun verifyDevice() {
         Connection.btAdapter = BluetoothAdapter.getDefaultAdapter()

        if (Connection.btAdapter == null) {
            message.msgToast("Dispositivo no soportado")
        }
    }

    /***
     * Button Find Devices
     */
    fun findDevicesBt(view: View) {



        checkEnable()

        Connection.view = view
    }


    private fun regReceiver() {
        receiver = ReceiverBluetooth(this)
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        registerReceiver(receiver, filter)
    }

    /***
     * Check if Bluetooth is enable and start discovery,
     */
    private fun checkEnable() {

        if (Connection.btAdapter?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        } else {
            if (Connection.btAdapter!!.isDiscovering) {
                Connection.btAdapter?.cancelDiscovery()
            }

            startDiscovery()
        }
    }

    /**
     * Check request to enable Bluetooth
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_OK) {
            //message.msgToast("Bluetooth Enabled")
            Connection.showSnackBar("Bluetooth Enabled")

            startDiscovery()

        } else {

            //message.msgToast("The operation was cancelled by the user")
            Connection.showSnackBar("The operation was cancelled by the user")
        }

    }

    /**
     * Start dialog  and show found device
     */
    private fun showDevicesDialog() {

        if (Connection.btAdapter!!.isEnabled) {
            dialog = AlertDialog.Builder(this).create()
            //dialog.setTitle("Devices")
            val textView = TextView(this)
            textView.setText("Select a device")
            textView.textSize = 22F
            textView.setPadding(10, 20, 10, 20)
            textView.setTextColor(Color.WHITE)
            //textView.setBackgroundColor(Color.rgb(11, 150, 214))
            textView.setBackgroundColor(ContextCompat.getColor(this, R.color.color_primary))

            dialog.setCustomTitle(textView)
            val view = LayoutInflater.from(this).inflate(R.layout.recycler_dialog_list, null)
            dialog.setView(view)

            configRecycler(view)

            //dia.setCancelable(false)
            dialog.setCancelable(true)
            dialog.setOnCancelListener {

                if (!dialog.isShowing) {
                    Connection.btAdapter?.cancelDiscovery()
                    dialog.dismiss()
                    message.msgToast("Discovery cancelled and dialog closed")
                }

            }
            dialog.show()
        }
    }

    /**
     * Fetch and config Recycler from[layout], fetch paired devices and discover new devices
     */
    private fun configRecycler(layout: View) {

        val recycler = layout.findViewById<RecyclerView>(R.id.recyclerDevices)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        val pairedRecycler = layout.findViewById<RecyclerView>(R.id.recyclerPairedDevices)
        pairedRecycler.layoutManager = LinearLayoutManager(this)
        pairedRecycler.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        val pairedTitle = layout.findViewById<TextView>(R.id.paired_devices_title)

        if(pairedBtList.isEmpty())
        {
            pairedTitle.visibility = View.GONE
        }
        else
        {
            pairedBtList.forEach{

                pairedDeviceList.add(Devices(it.name, 1, it.address))
            }
            pairedAdapter = DeviceAdapter(pairedDeviceList, this, listener)
            pairedRecycler.adapter = pairedAdapter

            //pairedAdapter.notifyDataSetChanged()
        }

        adapter = DeviceAdapter(devicesList, this, listener)
        recycler.adapter = adapter

    }


    /***
     *  Chech if [device] is in list, if not add it to list
     */
    fun checkDeviceInList(device: BluetoothDevice) {

        if (!btDevicesList.contains(device) && !pairedBtList.contains(device)) {

            btDevicesList.add(device)

            val deviceName = device.name
            val deviceHardwareAddress = device.address // MAC address
            devicesList.add(Devices(deviceName, 1, deviceHardwareAddress))
            //message.msgToast("Device found!")

            adapter.notifyDataSetChanged()

        }

    }

    fun startDiscovery() {

        devicesList.clear()
        btDevicesList.clear()
        pairedDeviceList.clear()

        pairedBtList = Connection.btAdapter!!.bondedDevices
        Toast.makeText(this, "bonded devices: ${pairedBtList.size}", Toast.LENGTH_SHORT).show()
        Connection.btAdapter?.startDiscovery()
        showDevicesDialog()
    }

    override fun onDestroy() {
        super.onDestroy()

        // Don't forget to unregister the ACTION_FOUND receiver.
        Connection.btAdapter?.cancelDiscovery()
        unregisterReceiver(receiver)
    }

    override fun onBackPressed() {

        Connection.btAdapter?.cancelDiscovery()
        Toast.makeText(this, "On main activity", Toast.LENGTH_SHORT).show()
        //msgToast("Discovery Cancelled")
    }

}




