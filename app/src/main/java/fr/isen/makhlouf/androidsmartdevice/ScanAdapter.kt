package fr.isen.makhlouf.androidsmartdevice

import android.bluetooth.le.ScanResult
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView



class ScanAdapter(private val devicesList: ArrayList<String>) :
    RecyclerView.Adapter<ScanAdapter.ScanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.scan_item, parent, false)
        return ScanViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScanViewHolder, position: Int) {
        holder.deviceName.text = devicesList[position]

    }


    override fun getItemCount(): Int {
        return devicesList.size
    }

    inner class ScanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deviceName: TextView = itemView.findViewById<TextView>(R.id.deviceName)

    }

    fun addDevice(device: ScanResult) {
       if(!devicesList.contains(device.toString())) {
           devicesList.add(device.toString())
           notifyDataSetChanged()
       }
    }

    companion object {
        fun addDevice(result: ScanResult) {
            TODO("Not yet implemented")

        }
    }
}