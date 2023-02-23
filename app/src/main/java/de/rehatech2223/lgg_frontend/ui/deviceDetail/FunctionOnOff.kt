package de.rehatech2223.lgg_frontend.ui.deviceDetail

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import de.rehatech2223.datamodel.DeviceDTO
import de.rehatech2223.datamodel.FunctionDTO
import de.rehatech2223.lgg_frontend.R
import de.rehatech2223.lgg_frontend.services.ServiceProvider

class FunctionOnOff(context: Context, attrs: AttributeSet? = null, functionDTO: FunctionDTO, deviceDTO: DeviceDTO): LinearLayout(context, attrs) {

    private val functionDTO: FunctionDTO
    private val deviceDTO: DeviceDTO
    private val switchCompat: SwitchCompat
    private val switch_state_text: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.function_on_off, this, true)
        orientation = VERTICAL

        this.functionDTO = functionDTO
        this.deviceDTO = deviceDTO
        switchCompat = findViewById(R.id.switch1)
        switch_state_text = findViewById(R.id.switch_state_text)

        initSwitch()
    }

    private fun initSwitch(){
        switchCompat.text = functionDTO.functionName
        switchCompat.isChecked = functionDTO.onOff!!
        switch_state_text.text = if (functionDTO.onOff!!) "(an)" else "(aus)"
        switchCompat.setOnCheckedChangeListener { _, c ->
            val functionValue: Float = if (c) 1f else 0f
            switch_state_text.text = if (c) "(an)" else "(aus)"
            ServiceProvider.functionService
                .triggerFunction(deviceDTO.deviceId, functionDTO.functionId, functionValue)
        }
    }
}