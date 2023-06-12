package de.rehatech2223.lgg_frontend.ui.deviceDetail

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import de.rehatech2223.datamodel.DeviceDTO
import de.rehatech2223.datamodel.FunctionDTO
import de.rehatech2223.lgg_frontend.R
import de.rehatech2223.lgg_frontend.services.ServiceProvider

/**
 * LinearLayout subclass that acts as the UI-Element for Trigger type functions in the DeviceDetail Activity.
 *
 * @author Fynn Debus
 */
class FunctionTrigger(context: Context, attrs: AttributeSet? = null, functionDTO: FunctionDTO, deviceDTO: DeviceDTO): LinearLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.function_trigger, this, true)
        orientation = VERTICAL

        val functionNameText: TextView = findViewById(R.id.functionNameText)
        val triggerButton: Button = findViewById(R.id.triggerButton)

        functionNameText.text = functionDTO.functionName
        triggerButton.setOnClickListener {
            ServiceProvider.functionService
                .triggerFunction(deviceDTO.deviceId, functionDTO.functionId, 1f)
        }
    }
}