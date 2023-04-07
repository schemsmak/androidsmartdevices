package fr.isen.makhlouf.androidsmartdevice
import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import fr.isen.makhlouf.androidsmartdevice.databinding.ActivityBluetoothConnectBinding
import java.util.*

class BluetoothConnect : AppCompatActivity() {

    private lateinit var binding: ActivityBluetoothConnectBinding
    var bluetoothGatt: BluetoothGatt? = null
    var serviceUUID = UUID.fromString("0000feed-cc7a-482a-984a-7f2ed5b3e58f")
    var characteristicUUID = UUID.fromString("0000abcd-8e22-4541-9d4c-21edae82ed19")

    private var brightblue = false
    private var brightred = false
    private var brightgreen = false


    @SuppressLint("MissingPermission")
    private val bluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                // successfully connected to the GATT Server
                show()
                bluetoothGatt?.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                // disconnected from the GATT Server
                runOnUiThread {
                    binding.group.visibility = View.GONE
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                val service = gatt?.getService(serviceUUID)
                val characteristic = service?.getCharacteristic(characteristicUUID)
                gatt?.readCharacteristic(characteristic)
            }
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                characteristic?.let { char ->
                    if (char.uuid == characteristicUUID) {
                        val properties = char.properties
                        // Use the properties as needed
                    }
                }
            }
        }

    }

    fun show() {
        runOnUiThread {
            binding.group.visibility = View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_connect)

        binding = ActivityBluetoothConnectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter

        binding.group.visibility = View.GONE

        val deviceName = intent.getStringExtra("devicename")
        val deviceAddress = intent.getStringExtra("device_address")
        val device = bluetoothAdapter.getRemoteDevice(deviceAddress)

        binding.name.text = deviceName

        @SuppressLint("MissingPermission")
        bluetoothGatt = device.connectGatt(this, false, bluetoothGattCallback)
        binding.led1.setOnClickListener {
            if (!brightblue) {
                val values = byteArrayOf(0x01)
                sendValueToSTM(values)
                binding.led1.setImageResource(R.drawable.ledbleu)
                binding.led1.postDelayed({
                    binding.led1.setImageResource(R.drawable.led)
                }, 500) // Change back to original image after 500ms delay
                brightblue = true
            }
            else{
                val values = byteArrayOf(0x00)
                sendValueToSTM(values)
                binding.led1.setImageResource(R.drawable.ledbleu)
                binding.led1.postDelayed({
                    binding.led1.setImageResource(R.drawable.led)
                }, 500) // Change back to original image after 500ms delay
                brightblue = false
            }
        }

        binding.led2.setOnClickListener {
            if (!brightred) {
                val values = byteArrayOf(0x03)
                sendValueToSTM(values)
                binding.led2.setImageResource(R.drawable.ledrouge)
                binding.led2.postDelayed({
                    binding.led2.setImageResource(R.drawable.led)
                }, 500) // Change back to original image after 500ms delay
                brightred = true
            }
            else{
                val values = byteArrayOf(0x00)
                sendValueToSTM(values)
                binding.led2.setImageResource(R.drawable.ledrouge)
                binding.led2.postDelayed({
                    binding.led2.setImageResource(R.drawable.led)
                }, 500) // Change back to original image after 500ms delay
                brightred = false
            }
        }

        binding.led3.setOnClickListener {
            if (!brightgreen) {
                val values = byteArrayOf(0x02)
                sendValueToSTM(values)
                binding.led3.setImageResource(R.drawable.ledvert)
                binding.led3.postDelayed({
                    binding.led3.setImageResource(R.drawable.led)
                }, 500) // Change back to original image after 500ms delay
                brightgreen = true
            }
            else{
                val values = byteArrayOf(0x00)
                sendValueToSTM(values)
                binding.led3.setImageResource(R.drawable.ledrouge)
                binding.led3.postDelayed({
                    binding.led3.setImageResource(R.drawable.led)
                }, 500) // Change back to original image after 500ms delay
                brightgreen = false
            }
        }

    }

    @SuppressLint("MissingPermission")
    fun sendValueToSTM(values: ByteArray) {
        val characteristic = bluetoothGatt?.getService(serviceUUID)?.getCharacteristic(characteristicUUID)
        characteristic?.value = values
        bluetoothGatt?.writeCharacteristic(characteristic)
    }
}