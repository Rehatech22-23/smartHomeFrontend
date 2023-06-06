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

class FunctionPlayer(
    context: Context,
    attrs: AttributeSet? = null,
    functionDTO: FunctionDTO,
    deviceDTO: DeviceDTO
) : LinearLayout(context, attrs) {

    var functionDTO: FunctionDTO
    var deviceDTO: DeviceDTO

    var isPlaying = false

    init {
        LayoutInflater.from(context).inflate(R.layout.function_player, this, true)
        orientation = VERTICAL

        this.functionDTO = functionDTO
        this.deviceDTO = deviceDTO

        isPlaying = functionDTO.outputValue == "PLAY"

        initPlayButton()
        initPrevNextButton()
    }

    private fun initPlayButton() {
        val playButton: TextView = findViewById(R.id.play_pause_button)
        playButton.text = getPlayText()

        playButton.setOnClickListener {/* play is: 0F pause is 3F */
            ServiceProvider.functionService
                .triggerFunction(deviceDTO.deviceId, functionDTO.functionId, if (!isPlaying) 0F else 3F)
            isPlaying = !isPlaying
            playButton.text = getPlayText()
        }
    }

    private fun initPrevNextButton() {
        val prevButton: LinearLayout = findViewById(R.id.prev_button)
        val nextButton: LinearLayout = findViewById(R.id.next_button)

        prevButton.setOnClickListener { /* prev is value: 2F */
            ServiceProvider.functionService
                .triggerFunction(deviceDTO.deviceId, functionDTO.functionId, 2F)
        }
        nextButton.setOnClickListener {/* next is value: 1F */
            ServiceProvider.functionService
                .triggerFunction(deviceDTO.deviceId, functionDTO.functionId, 1F)
        }
    }

    private fun getPlayText(): String {
        return if(isPlaying) "Pause" else "Start"
    }
}