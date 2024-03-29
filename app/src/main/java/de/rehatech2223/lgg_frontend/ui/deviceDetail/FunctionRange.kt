package de.rehatech2223.lgg_frontend.ui.deviceDetail

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import de.rehatech2223.datamodel.DeviceDTO
import de.rehatech2223.datamodel.FunctionDTO
import de.rehatech2223.datamodel.util.RangeDTO
import de.rehatech2223.lgg_frontend.R
import de.rehatech2223.lgg_frontend.services.ServiceProvider
import de.rehatech2223.lgg_frontend.util.DeviceNameDTO
import kotlin.math.round

/**
 * LinearLayout subclass that acts as the UI-Element for Range type functions in the DeviceDetail Activity.
 *
 * @author Fynn Debus
 */
class FunctionRange(context: Context, attrs: AttributeSet? = null, functionDTO: FunctionDTO, deviceDTO: DeviceDTO): LinearLayout(context, attrs) {

    private val functionDTO: FunctionDTO
    private val deviceDTO: DeviceDTO
    private val rangeDTO: RangeDTO
    private val minText: TextView
    private val maxText: TextView
    private val currentText: TextView
    private val functionNameText: TextView
    private val seekBar: SeekBar
    private val functionValueTextField: TextView
    private val sliderContainer: LinearLayout

    init {
        LayoutInflater.from(context).inflate(R.layout.function_range, this, true)
        orientation = VERTICAL

        this.functionDTO = functionDTO
        this.deviceDTO = deviceDTO
        rangeDTO = functionDTO.rangeDTO ?: RangeDTO(0.0, 1.0, 0.5)
        minText = findViewById(R.id.minText)
        maxText = findViewById(R.id.maxText)
        currentText = findViewById(R.id.currentText)
        functionNameText = findViewById(R.id.functionNameText)
        seekBar = findViewById(R.id.seekBar)
        functionValueTextField = findViewById(R.id.functionRangeValueTextView)
        sliderContainer = findViewById(R.id.functionRangeSliderContainer)

        initTexts()
        initSeekBar()
        initExtraButtons()
        initState()
    }

    private fun initState() {
        if(rangeDTO.minValue == 0.0 && rangeDTO.maxValue == 0.0) {
            sliderContainer.visibility = GONE
            functionValueTextField.visibility = VISIBLE
            functionValueTextField.text = rangeDTO.currentValue.toString()
            return
        }
        sliderContainer.visibility = VISIBLE
        functionValueTextField.visibility = GONE
    }

    private fun initTexts() {
        functionNameText.text = functionDTO.functionName
        minText.text = rangeDTO.minValue.toString()
        maxText.text = rangeDTO.maxValue.toString()
        currentText.text = rangeDTO.currentValue.toString()
    }

    private fun initExtraButtons(){
        val minButton: Button = findViewById(R.id.minButton)
        val maxButton: Button = findViewById(R.id.maxButton)
        val buttonContainer: ConstraintLayout = findViewById(R.id.buttonContainer)
        val deviceNameDTO = DeviceNameDTO.deserialize(deviceDTO.deviceName)

        if(!deviceNameDTO.rangeWithButtons){
            buttonContainer.visibility = GONE
            return
        }
        buttonContainer.visibility = VISIBLE
        minButton.text = deviceNameDTO.rangeMinText
        maxButton.text = deviceNameDTO.rangeMaxText

        minButton.setOnClickListener {
            seekBar.progress = seekBar.min
            ServiceProvider.functionService.triggerFunction(deviceDTO.deviceId,
                functionDTO.functionId, progressToCurrentValue(seekBar.progress).toFloat())
        }

        maxButton.setOnClickListener {
            seekBar.progress = seekBar.max
            ServiceProvider.functionService.triggerFunction(deviceDTO.deviceId,
                functionDTO.functionId, progressToCurrentValue(seekBar.progress).toFloat())
        }
    }

    private fun progressToCurrentValue(progress: Int): Double {
        var value: Double = (progress.toDouble() / 100) + rangeDTO.minValue
        value = round(value * 100) / 100
        return value
    }

    private fun currentValueToCurrentValueText(value: Double): String {
        var currentValue: String = value.toString()
        currentValue = if (currentValue.split('.')[1].length == 1) (currentValue + "0") else currentValue
        currentValue = "Aktuell: $currentValue"
        return currentValue
    }

    private fun initSeekBar() {
        seekBar.max = ((rangeDTO.maxValue - rangeDTO.minValue) * 100).toInt()
        seekBar.progress = ((rangeDTO.currentValue - rangeDTO.minValue) * 100).toInt()
        currentText.text = currentValueToCurrentValueText(progressToCurrentValue(seekBar.progress))

        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                currentText.text = currentValueToCurrentValueText(progressToCurrentValue(progress))
            }

            override fun onStartTrackingTouch(seek: SeekBar) { /* null */ }

            override fun onStopTrackingTouch(seek: SeekBar) {
                ServiceProvider.functionService.triggerFunction(deviceDTO.deviceId,
                    functionDTO.functionId, progressToCurrentValue(seek.progress).toFloat())
            }
        })
    }
}