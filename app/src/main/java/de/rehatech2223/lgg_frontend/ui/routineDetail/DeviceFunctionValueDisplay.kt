package de.rehatech2223.lgg_frontend.ui.routineDetail

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import de.rehatech2223.datamodel.DeviceDTO
import de.rehatech2223.lgg_frontend.R
import de.rehatech2223.lgg_frontend.services.ServiceProvider
import de.rehatech2223.lgg_frontend.util.DeviceNameDTO

/**
* LinearLayout subclass that acts as the UI-Element for displaying function values in the RoutineDetailActivity.
*
* @author Fynn Debus
*/
class DeviceFunctionValueDisplay(context: Context, attrs: AttributeSet? = null, deviceId: String, functionList: ArrayList<RoutineDetailActivity.FunctionTuple>): LinearLayout(context, attrs) {

    private val deviceDTO: DeviceDTO
    private val functionList: ArrayList<RoutineDetailActivity.FunctionTuple>

    init {
        LayoutInflater.from(context).inflate(R.layout.device_function_value_display, this, true)
        orientation = VERTICAL

        deviceDTO = ServiceProvider.devicesService.getDeviceInfo(deviceId) ?: DeviceDTO("Error Device", "", ArrayList())
        this.functionList = functionList

        initDeviceName()
        initFunctionList()
    }

    private fun initDeviceName(){
        val deviceName: TextView = findViewById(R.id.device_name)
        val text = DeviceNameDTO.deserialize(deviceDTO.deviceName).name
        deviceName.text = text
    }

    private fun initFunctionList(){
        val linearLayout: LinearLayout = findViewById(R.id.linear_layout)
        for (functionTuple in functionList){
            linearLayout.addView(FunctionValueDisplay(context, null, functionTuple))
        }
    }
}