package fr.isen.makhlouf.androidsmartdevice

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothProfile
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.isen.makhlouf.androidsmartdevice.databinding.ActivityDetailsBinding
import fr.isen.makhlouf.androidsmartdevice.databinding.ScanCellBinding

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        /*val bluetoothDevice: BluetoothDevice? = intent.getParcelableExtra("device")
           val bluetoothGatt = bluetoothDevice?.connectGatt(this, false, BluetoothGattCallback)
        bluetoothGatt?.connect()
    }

    override fun onStop() {
        super.onStop()
    bluettoothGatt?.close()
    }
    private val bluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
               runOnUiThread{
               displayContentConnected()
            }
            }
}
    private fun displayContentConnected {
    binding.detailLoaderTitle.text = getString(R.string.device_led_text)
    binding.detailLoader.isVisible=false
    binding.led1.isVisible=true
}

    }
}*/
    }
}