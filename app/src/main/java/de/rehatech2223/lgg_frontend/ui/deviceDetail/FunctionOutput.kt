package de.rehatech2223.lgg_frontend.ui.deviceDetail

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import de.rehatech2223.datamodel.DeviceDTO
import de.rehatech2223.datamodel.FunctionDTO
import de.rehatech2223.lgg_frontend.R

/**
 * LinearLayout subclass that acts as the UI-Element for Output functions in the DeviceDetail Activity.
 *
 * @author Fynn Debus
 */
class FunctionOutput(context: Context, attrs: AttributeSet? = null, functionDTO: FunctionDTO, deviceDTO: DeviceDTO): LinearLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.function_output, this, true)
        orientation = VERTICAL

        val functionNameText: TextView = findViewById(R.id.functionNameText)
        val outputValueText: TextView = findViewById(R.id.outputValueText)

        val nameText: String = functionDTO.functionName + ":"
        functionNameText.text = nameText
        outputValueText.text = functionDTO.outputValue
    }
}