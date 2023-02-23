package de.rehatech2223.lgg_frontend.ui.deviceDetail

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import de.rehatech2223.datamodel.DeviceDTO
import de.rehatech2223.datamodel.FunctionDTO
import de.rehatech2223.lgg_frontend.R

class FunctionRange(context: Context, attrs: AttributeSet? = null, functionDTO: FunctionDTO, deviceDTO: DeviceDTO): LinearLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.function_range, this, true)
        orientation = VERTICAL
    }
}