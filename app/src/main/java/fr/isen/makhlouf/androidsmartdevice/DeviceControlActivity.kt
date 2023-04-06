package fr.isen.makhlouf.androidsmartdevice

import android.annotation.SuppressLint
import android.bluetooth.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import java.util.*


    class DeviceControlActivity : AppCompatActivity() {

        private lateinit var gatt: BluetoothGatt
        private lateinit var ledCharacteristic: BluetoothGattCharacteristic
        private lateinit var buttonCharacteristic: BluetoothGattCharacteristic
        private lateinit var thirdButtonCharacteristic: BluetoothGattCharacteristic

        private var ledOn = false
        private var buttonCount = 0
        private var thirdButtonCount = 0

        private lateinit var ledSwitch: Switch
        private lateinit var buttonCountText: TextView
        private lateinit var thirdButtonCountText: TextView

        private var ledServiceUUID: String? = null
        private var ledCharacteristicUUID: String? = null
        private var buttonServiceUUID: String? = null
        private var buttonCharacteristicUUID: String? = null
        private var thirdButtonCharacteristicUUID: String? = null

        private val gattCallback = object : BluetoothGattCallback() {
            @SuppressLint("MissingPermission")
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    gatt.discoverServices()
                }
            }

            @SuppressLint("MissingPermission")
            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    val ledService = gatt.getService(UUID.fromString(ledServiceUUID))
                    ledCharacteristic = ledService.getCharacteristic(UUID.fromString(ledCharacteristicUUID))
                    val buttonService = gatt.getService(UUID.fromString(buttonServiceUUID))
                    buttonCharacteristic = buttonService.getCharacteristic(UUID.fromString(buttonCharacteristicUUID))
                    thirdButtonCharacteristic = buttonService.getCharacteristic(UUID.fromString(thirdButtonCharacteristicUUID))
                    gatt.setCharacteristicNotification(buttonCharacteristic, true)
                    gatt.setCharacteristicNotification(thirdButtonCharacteristic, true)
                }
            }

            override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
                if (characteristic.uuid == UUID.fromString(buttonCharacteristicUUID)) {
                    buttonCount++
                    runOnUiThread {
                        buttonCountText.text = getString(R.string.button_count_text, buttonCount)
                    }
                } else if (characteristic.uuid == UUID.fromString(thirdButtonCharacteristicUUID)) {
                    thirdButtonCount++
                    runOnUiThread {
                        thirdButtonCountText.text = getString(R.string.third_button_count_text, thirdButtonCount)
                    }
                } else if (characteristic.uuid == UUID.fromString(ledCharacteristicUUID)) {
                    val value = characteristic.value
                    if (value.isNotEmpty()) {
                        ledOn = value[0] == 1.toByte()
                        runOnUiThread {
                            ledSwitch.isChecked = ledOn
                        }
                    }
                }
            }
        }

        @SuppressLint("MissingPermission")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_device_control)

            ledSwitch = findViewById(R.id.led_switch)
            buttonCountText = findViewById(R.id.button_count_text)
            thirdButtonCountText = findViewById(R.id.third_button_count_text)

            val device = intent.getParcelableExtra<BluetoothDevice>(DEVICE_KEY) ?: return
            gatt = device.connectGatt(this, false, gattCallback)

            ledSwitch.setOnCheckedChangeListener { _, isChecked ->
                ledOn = isChecked
                ledCharacteristic.value = byteArrayOf(if (ledOn) 1 else 0)
                gatt.writeCharacteristic(ledCharacteristic)
            }

// Récupérer les UUID des services et caractéristiques
            ledServiceUUID = intent.getStringExtra(LED_SERVICE_UUID_KEY)
            ledCharacteristicUUID = intent.getStringExtra(LED_CHARACTERISTIC_UUID_KEY)
            buttonServiceUUID = intent.getStringExtra(BUTTON_SERVICE_UUID_KEY)
            buttonCharacteristicUUID = intent.getStringExtra(BUTTON_CHARACTERISTIC_UUID_KEY)
            thirdButtonCharacteristicUUID = intent.getStringExtra(THIRD_BUTTON_CHARACTERISTIC_UUID_KEY)
        }

        @SuppressLint("MissingPermission")
        override fun onDestroy() {
            super.onDestroy()
            gatt.disconnect()
        }

        companion object {
            const val DEVICE_KEY = "DEVICE_KEY"
            const val LED_SERVICE_UUID_KEY = "LED_SERVICE_UUID_KEY"
            const val LED_CHARACTERISTIC_UUID_KEY = "LED_CHARACTERISTIC_UUID_KEY"
            const val BUTTON_SERVICE_UUID_KEY = "BUTTON_SERVICE_UUID_KEY"
            const val BUTTON_CHARACTERISTIC_UUID_KEY = "BUTTON_CHARACTERISTIC_UUID_KEY"
            const val THIRD_BUTTON_CHARACTERISTIC_UUID_KEY = "THIRD_BUTTON_CHARACTERISTIC_UUID_KEY"
        }
    }
