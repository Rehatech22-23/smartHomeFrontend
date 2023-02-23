package de.rehatech2223.lgg_frontend.ui.deviceDetail

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import de.rehatech2223.datamodel.DeviceDTO
import de.rehatech2223.datamodel.FunctionDTO
import de.rehatech2223.lgg_frontend.DynamicThemeActivity
import de.rehatech2223.lgg_frontend.R
import de.rehatech2223.lgg_frontend.services.ServiceProvider
import de.rehatech2223.lgg_frontend.util.TileImageUtil

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

        initMainViewElements()
        initFunctions()
    }

    private fun initMainViewElements(){
        findViewById<TextView>(R.id.back_text).setOnClickListener {
            finish()
        }

        findViewById<TextView>(R.id.overview_name).text = deviceDTO.deviceName
        findViewById<ImageView>(R.id.overview_image)
            .setImageResource(TileImageUtil.getDeviceImageId(deviceDTO, this))
    }

    private fun initFunctions(){
        val scrollLayout = findViewById<LinearLayout>(R.id.scroll_layout)
        for (functionId in deviceDTO.functionIds){
            val functionDTO: FunctionDTO = ServiceProvider.functionService
                .getFunctionInfo(functionId) ?: continue

            val functionView = if (functionDTO.rangeDTO != null){
                FunctionRange(this, null, functionDTO, deviceDTO)
            }else if (functionDTO.onOff != null){
                FunctionOnOff(this, null, functionDTO, deviceDTO)
            }else if (functionDTO.outputValue != null){
                FunctionOutput(this, null, functionDTO, deviceDTO)
            }else if (functionDTO.outputTrigger != null){
                FunctionTrigger(this, null, functionDTO, deviceDTO)
            }else continue

            scrollLayout.addView(functionView)
        }
    }
}