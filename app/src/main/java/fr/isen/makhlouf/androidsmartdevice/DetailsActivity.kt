package fr.isen.makhlouf.androidsmartdevice

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothProfile
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import fr.isen.makhlouf.androidsmartdevice.databinding.ActivityDetailsBinding
import fr.isen.makhlouf.androidsmartdevice.databinding.ScanCellBinding
class DetailsActivity : AppCompatActivity() {

    private lateinit var loader: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        loader = findViewById(R.id.loader)

        // Récupérer le BluetoothDevice sélectionné
        val device: BluetoothDevice? = intent.getParcelableExtra("device")
        if (device != null) {
            connectToDevice(device)
        } else {
            Toast.makeText(this, "Device non trouvé", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    @SuppressLint("MissingPermission")
    private fun connectToDevice(device: BluetoothDevice) {
        // Connexion au device
        val gattCallback = object : BluetoothGattCallback() {
            // Gestion de la connexion
            override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
                super.onConnectionStateChange(gatt, status, newState)
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    // Le device est connecté
                    runOnUiThread {
                        loader.visibility = View.GONE
                        Toast.makeText(this@DetailsActivity, "Connecté", Toast.LENGTH_SHORT).show()
                    }
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    // La connexion est perdue
                    runOnUiThread {
                        Toast.makeText(this@DetailsActivity, "Déconnecté", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
        val bluetoothGatt = device.connectGatt(this, false, gattCallback)
        loader.visibility = View.VISIBLE
    }
}

/*

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val bluetoothDevice: BluetoothDevice? = intent.getParcelableExtra("device")
        /*   val bluetoothGatt = bluetoothDevice?.connectGatt(this, false, BluetoothGattCallback)
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
    private fun displayContentConnected (){
    binding.detailTitleLoader.text = getString(R.string.device_led_text)
    binding.detailLoader.isVisible=false
    binding.led1.isVisible=true
}

    }
}
*/
    }
}
*/
