package de.rehatech2223.lgg_frontend.ui.main

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import de.rehatech2223.datamodel.DeviceDTO
import de.rehatech2223.lgg_frontend.ui.deviceDetail.DeviceDetailActivity
import de.rehatech2223.lgg_frontend.R
import de.rehatech2223.lgg_frontend.util.TileImageUtil

class DeviceTile(context: Context, attrs: AttributeSet? = null, deviceArgument: DeviceDTO? = null): LinearLayout(context, attrs) {

    var deviceDTO: DeviceDTO

    init {
        LayoutInflater.from(context).inflate(R.layout.device_tile, this, true)
        orientation = VERTICAL

        deviceDTO = deviceArgument ?: DeviceDTO("Error DeviceDTO", "-1", arrayListOf())


        initDeviceTile()
        initOnClickListeners()
    }

    private fun initOnClickListeners() {
        this.setOnClickListener {
            val deviceDetailActivity = Intent(context, DeviceDetailActivity::class.java)
            deviceDetailActivity.putExtra("deviceId", deviceDTO.deviceId)
            context.startActivity(deviceDetailActivity)
        }
    }

    private fun initDeviceTile(){
        val name: TextView = findViewById(R.id.name)
        val image: ImageView = findViewById(R.id.image)
        val deviceDTOLabeling: List<String> = deviceDTO.deviceName.split(':')
        if (deviceDTOLabeling.size < 2){
            name.text = "Error Name"
            image.setImageResource(R.drawable.error_100px)
        }else {
            name.text = deviceDTOLabeling[1].ifEmpty { "Error Name" }
            image.setImageResource(TileImageUtil.getDeviceImageResource(deviceDTOLabeling[0]))
        }
    }
}