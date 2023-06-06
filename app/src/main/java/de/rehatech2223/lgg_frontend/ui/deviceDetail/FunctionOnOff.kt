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
    private val switchStateText: TextView
    private var previousState: Boolean?

    init {
        LayoutInflater.from(context).inflate(R.layout.function_on_off, this, true)
        orientation = VERTICAL

        this.functionDTO = functionDTO
        this.deviceDTO = deviceDTO
        switchCompat = findViewById(R.id.switch1)
        switchStateText = findViewById(R.id.switch_state_text)
        previousState = null

        initSwitch()
    }

    private fun initSwitch() {
        findViewById<TextView>(R.id.function_text).text = "${functionDTO.functionName}:"
        switchCompat.isChecked = functionDTO.onOff!!
        switchStateText.text = if (functionDTO.onOff!!) "AN" else "AUS"
        switchCompat.setOnClickListener {
            if (previousState == switchCompat.isChecked) switchCompat.isChecked = !previousState!!
            previousState = switchCompat.isChecked
            val functionValue: Float = if (switchCompat.isChecked) 1f else 0f
            switchStateText.text = if (switchCompat.isChecked) "AN" else "AUS"
            ServiceProvider.functionService
                .triggerFunction(deviceDTO.deviceId, functionDTO.functionId, functionValue)
        }
        switchCompat.setOnCheckedChangeListener { _, _ ->
            if (previousState == null) {
                previousState = functionDTO.onOff!!
                switchCompat.isChecked = previousState!!
            }
        }
    }
}