package de.rehatech2223.lgg_frontend.ui.main

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import de.rehatech2223.datamodel.DeviceDTO
import de.rehatech2223.lgg_frontend.DeviceDetailActivity
import de.rehatech2223.lgg_frontend.R

class DeviceTile(context: Context, attrs: AttributeSet? = null, deviceArgument: DeviceDTO? = null): LinearLayout(context, attrs) {

    var deviceDTO: DeviceDTO

    init {
        LayoutInflater.from(context).inflate(R.layout.device_tile, this, true)
        orientation = VERTICAL

        deviceDTO = deviceArgument ?: DeviceDTO("Error DeviceDTO", "-1", arrayListOf())
        findViewById<TextView>(R.id.name).text = deviceDTO.deviceName

        initOnClickListeners()
    }

    private fun initOnClickListeners() {
        this.setOnClickListener {
            Log.d("handler", "hewwow , starting")
            val deviceDetailActivity = Intent(context, DeviceDetailActivity::class.java)
            deviceDetailActivity.putExtra("deviceId", deviceDTO.deviceId)
            context.startActivity(deviceDetailActivity)
        }
    }
}