package de.rehatech2223.lgg_frontend

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import de.rehatech2223.datamodel.DeviceDTO
import de.rehatech2223.lgg_frontend.services.ServiceProvider

class DeviceDetailActivity : DynamicThemeActivity() {

    private lateinit var deviceDTO: DeviceDTO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme()
        setContentView(R.layout.device_detail_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val deviceId: String = intent.getStringExtra("deviceId")!!
        val deviceArgument: DeviceDTO? = ServiceProvider.devicesService.getDeviceInfo(deviceId)
        deviceDTO = deviceArgument ?: DeviceDTO("Error Device", "-1", ArrayList())

        Log.d("handler", "hewwow , ${deviceDTO.deviceName}")
        Log.d("handler", "is showing: ${supportActionBar?.isShowing}")

        findViewById<Button>(R.id.button).setOnClickListener {
            Log.d("handler", "clicked button")
            finish()
        }
    }

    // this event will enable the back
    // function to the button on press
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}