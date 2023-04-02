package fr.isen.makhlouf.androidsmartdevice

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.makhlouf.androidsmartdevice.databinding.ActivityScanBinding

class ScanActivity : AppCompatActivity(){

    private lateinit var binding: ActivityScanBinding
    private var mScanning = false
    private lateinit var adapter: ScanAdapter
    private val bluetoothAdapter: BluetoothAdapter? by
    lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager =
            getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val handler = Handler(Looper.getMainLooper())

    private val REQUIRED_PERMISSIONS = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
    { permissions ->
        if (permissions.all { it.value }) {
            val scanBlue = BLUETOOTH_SCAN
            if(ContextCompat.checkSelfPermission(
                    this,
                    scanBlue
                ) == PackageManager.PERMISSION_GRANTED){
                Log.e("Scan","OK")
                scanLeDevice()
            }else{
                Log.e("Scan","Pas Ok")
            }
        }
    }
    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            Log.d("scan", "result: $result")
            adapter.addDevice(result.device)
            adapter.notifyDataSetChanged()

        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (bluetoothAdapter?.isEnabled == true){
            scanDeviceWithPermissions()
            Toast.makeText(this, "bluetooth activé", Toast.LENGTH_SHORT).show()
        }else{
            handleBLENotAvailable()
            Toast.makeText(this, "bluetooth désactivé", Toast.LENGTH_SHORT).show()

        }
        //binding.InstructionToStart.setOnClickListener{togglePlayPauseAction()}
        adapter = ScanAdapter(arrayListOf()){
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("device",it)
            startActivity(intent)
        }
        binding.devicesList.adapter= adapter

    }


    @SuppressLint("MissingPermission")
    override fun onStop() {
        super.onStop()
        if (bluetoothAdapter?.isEnabled == true && allPermissionsGranted()){
            mScanning=false
            bluetoothAdapter?.bluetoothLeScanner?.stopScan(leScanCallback)
        }
    }
    private fun initToggleActions(){
        binding.scanTitle.setOnClickListener{
            scanLeDevice()
        }
        binding.scanButton.setOnClickListener{
            scanLeDevice()
        }
        binding.devicesList.layoutManager = LinearLayoutManager(this)
        adapter = ScanAdapter(arrayListOf()){
            val intent = Intent(this, ScanAdapter::class.java)
            intent.putExtra("device",it)
            startActivity(intent)
        }
        binding.devicesList.adapter= adapter

    }
    private fun handleBLENotAvailable() {
        binding.scanTitle.text = getString(R.string.ble_scan_title_pause)
    }

    private fun scanDeviceWithPermissions(){
        if(allPermissionsGranted()){
            initToggleActions()
            scanLeDevice()
        }else{
            REQUIRED_PERMISSIONS.launch(getAllPermission())
        }
    }


    @SuppressLint("MissingPermission")
    private fun scanLeDevice() {
        if (!mScanning) { // Stops scanning after a pre-defined scan period.
            handler.postDelayed({
                mScanning = false
                bluetoothAdapter?.bluetoothLeScanner?.stopScan(leScanCallback)
                togglePlayPauseAction()
            }, SCAN_PERIOD)
            mScanning = true
            bluetoothAdapter?.bluetoothLeScanner?.startScan(leScanCallback)
        } else {
            mScanning = false
            bluetoothAdapter?.bluetoothLeScanner?.stopScan(leScanCallback)
        }
        togglePlayPauseAction()
    }
    // Device scan callback.

    private fun allPermissionsGranted(): Boolean {
        val allPermissions = getAllPermission()
        return allPermissions.all { permission ->
            ContextCompat.checkSelfPermission(
                this, permission) == PackageManager.PERMISSION_GRANTED
        } || requestPermissions(allPermissions)
        //true
    }

    private fun requestPermissions(permissions: Array<String>): Boolean {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_BLUETOOTH_PERMISSION)
        return false
    }

    private fun getAllPermission(): Array<String> {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            arrayOf(
                BLUETOOTH_SCAN,
                ACCESS_FINE_LOCATION,
                BLUETOOTH_CONNECT,
                ACCESS_COARSE_LOCATION)
        }
        else{
            arrayOf(
                ACCESS_FINE_LOCATION,
                ACCESS_COARSE_LOCATION)
        }
    }

    private fun togglePlayPauseAction(){

        if(mScanning){
            binding.scanTitle.text= getString(R.string.ble_scan_title_pause)
            binding.scanButton.setImageResource(R.drawable.baseline_pause_circle_outline_24 )
            binding.progressBar.isIndeterminate=true

        } else {
            binding.scanTitle.text= getString(R.string.ble_scan_title_play)
            binding.scanButton.setImageResource(R.drawable.baseline_play_circle_outline_24)
            binding.progressBar.isIndeterminate=false

        }
    }

    private fun startScan(bluetoothLeScanner: BluetoothLeScanner) {
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth non disponible", Toast.LENGTH_SHORT).show()
            return
        }
        if (bluetoothAdapter?.isEnabled == false) {
            Toast.makeText(this, "Bluetooth non activé", Toast.LENGTH_SHORT).show()
            return
        }
        if (bluetoothAdapter?.isEnabled == true) {
            binding.scanButton.setOnClickListener() {
                togglePlayPauseAction()
                scanLeDevice()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_BLUETOOTH_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // L'autorisation Bluetooth a été accordée
                    // Faites ce que vous voulez ici
                } else {
                    // L'autorisation Bluetooth a été refusée
                    // Afficher un message à l'utilisateur pour l'informer que l'application a besoin de cette autorisation pour fonctionner correctement
                    Toast.makeText(this, "Vous avez besoin du bluetooth pour utiliser l'application", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
    companion object {
        private const val REQUEST_BLUETOOTH_PERMISSION=1
        private const val SCAN_PERIOD: Long= 10000
    }

}

