package fr.isen.makhlouf.androidsmartdevice
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.makhlouf.androidsmartdevice.databinding.ActivityScanBinding
//import java.util.jar.Manifest


class ScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanBinding
    private var isScanning: Boolean = false
    private lateinit var deviceListAdapter: ScanAdapter

    @RequiresApi(Build.VERSION_CODES.S)
    private val enableBtActivityResult =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val permissionGranted = permissions.all { it.value }
            val scanBlue = android.Manifest.permission.BLUETOOTH_SCAN
            if (ContextCompat.checkSelfPermission(
                    this,
                    scanBlue
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Log.e("Scan", "en cours")
                startScan()
            } else {
                Log.e("Scan", "arreté")
            }
        }


    private fun getAllPermissions(): Array<String> {
        val listOfPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                android.Manifest.permission.BLUETOOTH,
                android.Manifest.permission.BLUETOOTH_CONNECT,
                android.Manifest.permission.BLUETOOTH_ADMIN,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            arrayOf(
                android.Manifest.permission.BLUETOOTH,
                android.Manifest.permission.BLUETOOTH_ADMIN,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        }

        return listOfPermission
    }


    //@SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.INVISIBLE
        deviceListAdapter = ScanAdapter(ArrayList())
        binding.listDevices.adapter = deviceListAdapter
        binding.listDevices.layoutManager = LinearLayoutManager(this)

        binding.scanButton.setOnClickListener {
            val bluetoothManager =
                getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            val bluetoothAdapter = bluetoothManager.adapter
            if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                //start activity de enableBtIntent
                Toast.makeText(this, "Bluetooth activation required", Toast.LENGTH_SHORT).show()
            } else {
                if (permissionGranted()) {
                    if (!isScanning) {
                        binding.scanTitle.text= getString(R.string.ble_scan_title_pause)
                        binding.progressBar.isIndeterminate=true
                        startScan()
                        binding.scanButton.setImageResource(R.drawable.baseline_pause_circle_outline_24 )
                        isScanning = true
                    } else {
                        binding.progressBar.isIndeterminate=false
                        stopScan()
                        binding.scanButton.setImageResource(R.drawable.baseline_play_circle_outline_24)
                        isScanning = false
                    }
                } else {
                    enableBtActivityResult.launch(getAllPermissions())
                }
            }
        }
    }

    private fun permissionGranted(): Boolean {
        val permG = getAllPermissions()
        return permG.all { it
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }


    @SuppressLint("MissingPermission")
    private fun startScan() {
        // Démarrer le scan des appareils Bluetooth
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        val scanner = bluetoothAdapter.bluetoothLeScanner
        scanner.startScan(scanCallback)
    }

    @SuppressLint("MissingPermission")
    private fun stopScan() {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        val scanner = bluetoothAdapter.bluetoothLeScanner
        scanner.stopScan(scanCallback)
    }

    private val scanCallback = object : ScanCallback() {

        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            // Ajouter le nouvel appareil détecté à la liste
            Log.d("ScanActivity", "Success $result")
            if(!result.device.name.isNullOrBlank())
                deviceListAdapter.addDevice(result)
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Log.e("ScanActivity", "Error while scanning for BLE devices: $errorCode")
        }
    }
}