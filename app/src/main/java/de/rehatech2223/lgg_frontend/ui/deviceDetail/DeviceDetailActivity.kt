package de.rehatech2223.lgg_frontend.ui.deviceDetail

import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import de.rehatech2223.datamodel.DeviceDTO
import de.rehatech2223.datamodel.FunctionDTO
import de.rehatech2223.lgg_frontend.DynamicThemeActivity
import de.rehatech2223.lgg_frontend.R
import de.rehatech2223.lgg_frontend.services.ServiceProvider
import de.rehatech2223.lgg_frontend.util.DeviceNameDTO
import de.rehatech2223.lgg_frontend.util.TileImageUtil

/**
 * Activity that starts when the user clicks on a device.
 * It loads further information on the requested device, like the included functions.
 *
 * @author Fynn Debus
 */
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

    private fun initMainViewElements() {
        val overViewName: TextView = findViewById(R.id.overview_name)
        val overViewImage: ImageView = findViewById(R.id.overview_image)
        val overViewDescription: TextView = findViewById(R.id.overview_description)

        val deviceNameDTO = DeviceNameDTO.deserialize(deviceDTO.deviceName)
        overViewName.text = deviceNameDTO.name
        overViewImage.setImageResource(TileImageUtil.getDeviceImageResource(deviceNameDTO.icon))
        if (deviceNameDTO.description != "") {
            overViewDescription.visibility = VISIBLE
            overViewDescription.text = deviceNameDTO.description
        }

        if (deviceDTO.functionIds.isEmpty()) {
            val functionText: TextView = findViewById(R.id.function_text)
            functionText.visibility = GONE
        }

        findViewById<LinearLayout>(R.id.back_text).setOnClickListener {
            finish()
        }
        findViewById<Button>(R.id.refresh_button).setOnClickListener {
            findViewById<LinearLayout>(R.id.scroll_layout).removeAllViews()
            recreate()
        }
    }

    private fun initFunctions() {
        val scrollLayout = findViewById<LinearLayout>(R.id.scroll_layout)
        val requestedFunctionList = ArrayList<FunctionDTO>()
        for (functionId in deviceDTO.functionIds) {
            val requestedFunctionDTO: FunctionDTO = ServiceProvider.functionService
                .getFunctionInfo(functionId) ?: continue
            requestedFunctionList.add(requestedFunctionDTO)
        }
        if (requestedFunctionList.isEmpty()) return
        for (functionDTO in requestedFunctionList) {
            val functionView = if (functionDTO.isPlayer) {
                FunctionPlayer(this, null, functionDTO, deviceDTO)
            } else if (functionDTO.rangeDTO != null) {
                FunctionRange(this, null, functionDTO, deviceDTO)
            } else if (functionDTO.onOff != null) {
                FunctionOnOff(this, null, functionDTO, deviceDTO)
            } else if (functionDTO.outputTrigger != null) {
                FunctionTrigger(this, null, functionDTO, deviceDTO)
            } else if (functionDTO.outputValue != null) {
                FunctionOutput(this, null, functionDTO, deviceDTO)
            } else continue
            scrollLayout.addView(functionView)
        }
    }
}