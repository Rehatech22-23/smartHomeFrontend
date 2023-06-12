package de.rehatech2223.lgg_frontend.ui.routineDetail

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import de.rehatech2223.datamodel.DeviceDTO
import de.rehatech2223.datamodel.FunctionDTO
import de.rehatech2223.lgg_frontend.R
import de.rehatech2223.lgg_frontend.services.ServiceProvider

/**
 * LinearLayout subclass that acts as the UI-Element for displaying function values in the RoutineDetailActivity.
 *
 * @author Fynn Debus
 */
class FunctionValueDisplay(context: Context, attrs: AttributeSet? = null, functionTuple: RoutineDetailActivity.FunctionTuple): LinearLayout(context, attrs) {

    private val functionDTO: FunctionDTO
    private val functionValue: Float

    init {
        LayoutInflater.from(context).inflate(R.layout.function_value_display, this, true)
        orientation = VERTICAL

        functionDTO = functionTuple.functionDTO
        functionValue = functionTuple.expectation

        initFunctionValueText()
    }

    private fun initFunctionValueText(){
        val functionValueText: TextView = findViewById(R.id.function_value_text)
        val text = "• \"${functionDTO.functionName}\" " + functionSpecificText()
        functionValueText.text = text
    }

    private fun functionSpecificText(): String {
        var text = ""
        if (functionDTO.rangeDTO != null){
            text += "wird auf $functionValue gesetzt"
        }else if (functionDTO.onOff != null){
            text += "geht ${if (functionValue == 1F) "an" else "aus"}"
        }else if (functionDTO.outputValue != null){
            text += "Error, Output Value is not a valid routineEvent!"
        }else if (functionDTO.outputTrigger != null){
            text += "wird ausgeführt"
        }
        return text
    }
}