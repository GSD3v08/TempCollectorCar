package com.example.tempcollectorcar

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.graphics.drawable.DrawableContainer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.custom_device.view.*

class DeviceAdapter(devices:ArrayList<Devices>, val context: Context, val listener: OnItemClickListener):
                    RecyclerView.Adapter<DeviceAdapter.ViewHolder>() {

    private var devices:List<Devices>


    init {

        this.devices = devices
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout = LayoutInflater.from(context).inflate(R.layout.custom_device, parent, false)
        //val containerLayout = parent.findViewById<ConstraintLayout>(R.id.consLayout)
        val view = parent.findViewById<View>(R.id.btDevice)

        return ViewHolder(layout, listener)
    }

    override fun getItemCount(): Int {

        return devices.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.name.text = devices[position].nameDevice
        holder.macNum.text = devices[position].macAddress
    }

    inner class ViewHolder(view:View, dialoglistener: OnItemClickListener): RecyclerView.ViewHolder(view)
    {
        var name:TextView
        var macNum:TextView

        init {

            name = view.findViewById(R.id.tvNameDevice)
            macNum = view.findViewById(R.id.tvMac)

            view.setOnClickListener {//TODO("check again")

                /*Toast.makeText(view.context, "Has seleccionado ${view.tvNameDevice.text}",
                                Toast.LENGTH_SHORT).show()*/

                dialoglistener.OnItemClicked()
                val mac = macNum.text.toString()
                val btConnect = ConnectBluetooth(mac, view)
                btConnect.execute()

            }
        }
    }


}
