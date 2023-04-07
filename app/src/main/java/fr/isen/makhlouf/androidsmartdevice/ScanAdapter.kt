package fr.isen.makhlouf.androidsmartdevice

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.bluetooth.le.ScanResult


class ScanAdapter(private val deviceList: ArrayList<ScanResult>) :
    RecyclerView.Adapter<ScanAdapter.DeviceViewHolder>() {

    class DeviceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val deviceName: TextView = view.findViewById(R.id.nameDevice)
        val deviceAddress: TextView = view.findViewById(R.id.addressDevice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.scan_cell, parent, false)
        return DeviceViewHolder(view)
    }

    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val device = deviceList[position].device
        holder.deviceAddress.text = device.address
        holder.deviceName.text = device.name

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, BluetoothConnect::class.java)
            intent.putExtra("device_name", device.name)
            intent.putExtra("device_address", device.address)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = deviceList.size

    fun addDevice(device: ScanResult) {
        if (!deviceList.contains(device)) {
            deviceList.add(device)
            notifyDataSetChanged()
        }
    }

    fun clearDevices() {
        deviceList.clear()
        notifyDataSetChanged()
    }
}



/*  @SuppressLint("MissingPermission")
      fun addDevice(device: BluetoothDevice, rssi: Int) {
          if (!device.name.isNullOrBlank()) {
              if (!MAC.contains(device.address)) {
                  device_name.add(device.name)
                  MAC.add(device.address)
                  distance.add(rssi)
                  size++
                  Log.d("ScanAdapter", "Device added: ${device.name}")
              }
          }

      }
  }
  */