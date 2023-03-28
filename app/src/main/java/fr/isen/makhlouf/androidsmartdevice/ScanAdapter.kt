package fr.isen.makhlouf.androidsmartdevice

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.isen.makhlouf.androidsmartdevice.databinding.ScanCellBinding


class ScanAdapter(var devices: ArrayList<android.bluetooth.BluetoothDevice>, var onDeviceClickListener:(android.bluetooth.BluetoothDevice)->Unit ) :
    RecyclerView.Adapter<ScanAdapter.ScanViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ScanCellBinding.inflate(inflater, parent, false)
        return ScanViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return devices.size
    }


    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: ScanViewHolder, position: Int) {
        holder.deviceName.text = devices[position].name ?: "Inconnu"
        holder.deviceAddress.text = devices[position].address
        holder.itemView.setOnClickListener{
            onDeviceClickListener(devices[position])
        }

    }
    class ScanViewHolder(binding: ScanCellBinding) : RecyclerView.ViewHolder(binding.root) {
        val deviceName=binding.nameDevice
        val deviceAddress=binding.address
    }

    fun addDevice(device: android.bluetooth.BluetoothDevice){
        var shouldAddDevice = true
        devices.forEachIndexed { index, bluetoothDevice ->
            if (bluetoothDevice.address == device.address){
                devices[index]= device
                shouldAddDevice = false
            }
        }
        if (shouldAddDevice){
            devices.add(device)
        }
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
