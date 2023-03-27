package fr.isen.makhlouf.androidsmartdevice

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.Manifest
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.makhlouf.androidsmartdevice.databinding.ActivityScanBinding

class ScanActivity : AppCompatActivity(){
    private lateinit var binding: ActivityScanBinding
    private var mScanning = false

    private val bluetoothAdapter: BluetoothAdapter? by
    lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager =
            getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (bluetoothAdapter?.isEnabled == true){ //j'ai le BLE
             } else {
             // je ne l'ai pas
             }
        binding.scanTitle.setOnClickListener{
            togglePlayPauseAction()
        }
        binding.scanButton.setOnClickListener{
            togglePlayPauseAction()
        }
        binding.devicesList.layoutManager = LinearLayoutManager(this)
        binding.devicesList.adapter = ScanAdapter(arrayListOf("Device 1", "Device 2"))
    }
    private fun togglePlayPauseAction(){
        mScanning= !mScanning
        if(mScanning){
            binding.scanTitle.text= getString(R.string.ble_scan_title_pause)
            binding.scanButton.setImageResource(R.drawable.baseline_pause_circle_outline_24 )
            binding.progressBar.isVisible=true }
        else {
                binding.scanTitle.text= getString(R.string.ble_scan_title_play)
            binding.scanButton.setImageResource(R.drawable.baseline_play_circle_outline_24)
            binding.progressBar.isVisible=false
        }
    }
    private fun startScan(bluetoothLeScanner: BluetoothLeScanner){
        if(bluetoothAdapter==null){
         Toast.makeText(this,"Bluetooth indisponible",Toast.LENGTH_SHORT).show()
            return
        }
        if(bluetoothAdapter?.isEnabled==false){
            Toast.makeText(this,"Bluetooth désactivé",Toast.LENGTH_SHORT).show()
            return
        }
        if(bluetoothAdapter?.isEnabled==true){
            binding.scanButton.setOnClickListener(){
                togglePlayPauseAction()
                initdevicesList()
            }

        }
        //TODO
    }
    private fun scanDeviceWithPermissions(){
        if(allPermissionsGranted()){
            startScan(bluetoothAdapter?.bluetoothLeScanner!!)
        }
        else{
            requestPermissions()
        }
        //TODO
    }

    private fun requestPermissions() {
        if(!allPermissionsGranted()){
            startScan(bluetoothAdapter?.bluetoothLeScanner!!)
        }
        else{
            requestPermissions(getAllPermissions(), 1)
        }
    }

    private fun scanBLEDevices(){
       val bluetoothLeScanner = bluetoothAdapter?.bluetoothLeScanner
        if(bluetoothLeScanner!=null){
            startScan(bluetoothLeScanner)
        }
        else{
            scanDeviceWithPermissions()
        }
    }

    private fun allPermissionsGranted(): Boolean {
val allPermissions = getAllPermissions()
        return allPermissions.all {
            checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
        }

    }

    private fun getAllPermissions(): Array<String> {
        return arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.BLUETOOTH)
    }

    //function who initilize the deviceslist
    private fun initdevicesList(){
        binding.devicesList.layoutManager = LinearLayoutManager(this)
        binding.devicesList.adapter = ScanAdapter(arrayListOf("Device 1", "Device 2"))
    }
}


