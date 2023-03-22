package de.rehatech2223.lgg_frontend.ui.routineDetail

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import de.rehatech2223.datamodel.DeviceDTO
import de.rehatech2223.datamodel.FunctionDTO
import de.rehatech2223.datamodel.RoutineDTO
import de.rehatech2223.datamodel.util.RoutineEventDTO
import de.rehatech2223.datamodel.util.TriggerEventByDeviceDTO
import de.rehatech2223.lgg_frontend.DynamicThemeActivity
import de.rehatech2223.lgg_frontend.R
import de.rehatech2223.lgg_frontend.services.ServiceProvider
import de.rehatech2223.lgg_frontend.util.TileImageUtil

class RoutineDetailActivity : DynamicThemeActivity() {

    private lateinit var routineDTO: RoutineDTO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme()
        setContentView(R.layout.routine_detail_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val routineId: Long = intent.getLongExtra("routineId", 1L)
        val routineArgument: RoutineDTO? = ServiceProvider.routineService.getRoutineInfo(routineId)
        routineDTO = routineArgument ?: RoutineDTO.Builder("Error Routine", 0, ArrayList(), -1L).build()

        initMainViewElements()
        initRoutineCondition()
        initRoutineResult()
    }

    private fun initMainViewElements() {
        findViewById<TextView>(R.id.back_text).setOnClickListener {
            finish()
        }
        findViewById<TextView>(R.id.overview_name).text = routineDTO.routineName
        findViewById<ImageView>(R.id.overview_image)
            .setImageResource(TileImageUtil.getRoutineImageId(routineDTO, this))

        findViewById<Button>(R.id.trigger_button).setOnClickListener {
            ServiceProvider.routineService.triggerRoutine(routineDTO.routineId)
        }
    }

    private fun initRoutineCondition() {
        val startTextDyn: TextView = findViewById(R.id.start_text_dyn)
        if (routineDTO.triggerTime != null) {
            val startText = "Der Ablauf startet um ${routineDTO.triggerTime!!.time}"
            startTextDyn.text = startText
        } else {
            startTextDyn.text = functionTriggerText()
        }
    }

    private fun functionTriggerText(): String {
        val triggerDTO: TriggerEventByDeviceDTO =
            routineDTO.triggerEventByDeviceDTO ?: TriggerEventByDeviceDTO(
                "", FunctionDTO
                    .Builder("Error Function", -1L).build()
            )
        val deviceDTO: DeviceDTO =
            ServiceProvider.devicesService.getDeviceInfo(triggerDTO.deviceId) ?: DeviceDTO("Error Device", "", ArrayList())
        val triggerFunction: FunctionDTO = triggerDTO.functionDTOExpectation
        var text = "\"${triggerFunction.functionName}\" von \"${deviceDTO.deviceName}\" "

        if (triggerFunction.rangeDTO != null) {
            val separator = when (routineDTO.comparisonType) {
                0 -> "kleiner als"
                1 -> "größer als"
                2 -> "gleich"
                else -> "error"
            }
            text += "ist $separator ${triggerFunction.rangeDTO!!.currentValue}"
        } else if (triggerFunction.onOff != null) {
            text += "ist ${if (triggerFunction.onOff == true) "an" else "aus"}"
        } else if (triggerFunction.outputValue != null) {
            text += "muss \"${triggerFunction.outputValue}\" sein"
        } else if (triggerFunction.outputTrigger != null) {
            text += "ausgelöst"
        } else text += "Error in Function"
        return text
    }

    private fun initRoutineResult() {
        val resultLayout: LinearLayout = findViewById(R.id.result_layout)
        val deviceToFunctionsMap: Map<String, ArrayList<FunctionTuple>> = initDeviceToFunctionsMap()

        Log.d("handler", "deviceMap: $deviceToFunctionsMap")

        for (key in deviceToFunctionsMap.keys) {
            val tuple: ArrayList<FunctionTuple> = deviceToFunctionsMap[key]!!
            resultLayout.addView(DeviceFunctionValueDisplay(this, null, key, tuple))
        }

    }

    private fun initDeviceToFunctionsMap(): Map<String, ArrayList<FunctionTuple>> {
        val routineEvents: ArrayList<RoutineEventDTO> = routineDTO.routineEventDTO
        val map: MutableMap<String, ArrayList<FunctionTuple>> = HashMap()

        Log.d("handler", "rotine events: $routineEvents")

        for (event in routineEvents) {
            val functionDTO: FunctionDTO =
                ServiceProvider.functionService.getFunctionInfo(event.functionId) ?: FunctionDTO
                    .Builder("Error Function", -1L)
                    .build()
            if (map[event.deviceId] == null) {
                map[event.deviceId] = arrayListOf(FunctionTuple(functionDTO, event.functionValue))
            } else {
                map[event.deviceId]?.add(FunctionTuple(functionDTO, event.functionValue))
            }
        }
        return map
    }

    private fun deviceTextView(name: String): TextView {
        val textView = TextView(this)
        textView.text = name
        val textColorAttr: Int = theme.obtainStyledAttributes(IntArray(1) {
            com.google.android.material.R.attr.colorOnSurface
        }).getIndex(0)
        textView.setTextColor(textColorAttr)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35f)
        textView.setTypeface(textView.typeface, Typeface.BOLD)
        return textView
    }

    data class FunctionTuple(val functionDTO: FunctionDTO, val expectation: Float)
}