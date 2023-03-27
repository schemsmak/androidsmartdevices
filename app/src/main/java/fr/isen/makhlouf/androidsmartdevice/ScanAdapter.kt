package fr.isen.makhlouf.androidsmartdevice

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
}


/*class ScanAdapter(private val devicesList: ArrayList<ScanResult>) : RecyclerView.Adapter<ScanAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) {
        val view = LayoutInflater.from(parent.context)
        val binding = ActivityMainBinding.inflate(inflater, parent, false)

    }
    override fun getItemCount(): Int = devicesList.size
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }
    holder.deviceName.text = devicesList[position]
    holder.deviceAddress.text = devicesList[position]

}

class ScanViewHolder(binding: ActivityMainBinding) : RecyclerView.ViewHolder(binding.root) {
    val deviceName = binding.deviceName
    val deviceAddress: TextView = binding.deviceAddress
}
}*/