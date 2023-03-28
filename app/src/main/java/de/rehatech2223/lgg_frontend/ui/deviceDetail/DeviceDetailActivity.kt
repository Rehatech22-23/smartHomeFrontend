package de.rehatech2223.lgg_frontend.ui.deviceDetail

import android.os.Bundle
import android.util.Log
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
        val overViewName: TextView = findViewById(R.id.overview_name)
        val overViewImage: ImageView = findViewById(R.id.overview_image)
        val overViewDescription: TextView = findViewById(R.id.overview_description)
        val deviceDTOLabeling: List<String> = deviceDTO.deviceName.split(':')
        if (deviceDTOLabeling.size < 2){
            overViewName.text = "Error Name"
            overViewImage.setImageResource(R.drawable.error_100px)
        }else {
            overViewName.text = deviceDTOLabeling[1].ifEmpty { "Error Name" }
            overViewImage.setImageResource(TileImageUtil.getDeviceImageResource(deviceDTOLabeling[0]))

            if (deviceDTOLabeling.size == 3){
                overViewDescription.text = deviceDTOLabeling[2]
            }
        }

        findViewById<TextView>(R.id.back_text).setOnClickListener {
            finish()
        }
    }

    private fun initFunctions(){
        val scrollLayout = findViewById<LinearLayout>(R.id.scroll_layout)
        for (functionId in deviceDTO.functionIds){
            Log.d("handler", "starting function request")
            val requestedFunctionDTO: FunctionDTO? = ServiceProvider.functionService
                .getFunctionInfo(functionId)
            Log.d("handler", "functionDTO: $requestedFunctionDTO")
            val functionDTO: FunctionDTO = requestedFunctionDTO ?: continue


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