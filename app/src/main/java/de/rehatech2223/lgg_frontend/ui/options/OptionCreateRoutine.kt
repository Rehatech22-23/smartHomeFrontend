package de.rehatech2223.lgg_frontend.ui.options

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import de.rehatech2223.datamodel.DeviceDTO
import de.rehatech2223.datamodel.FunctionDTO
import de.rehatech2223.datamodel.RoutineDTO
import de.rehatech2223.datamodel.util.RangeDTO
import de.rehatech2223.datamodel.util.RoutineEventDTO
import de.rehatech2223.datamodel.util.TriggerEventByDeviceDTO
import de.rehatech2223.datamodel.util.TriggerTimeDTO
import de.rehatech2223.lgg_frontend.R
import de.rehatech2223.lgg_frontend.services.PopUpService
import de.rehatech2223.lgg_frontend.services.ServiceProvider
import java.time.LocalTime

class OptionCreateRoutine(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    private var sensorValueConditionEditText: EditText
    private var conditionHelperText: TextView
    private var comparisonTypeSpinner: Spinner
    private var createRoutineExecButton: Button
    private var textFieldRoutineName: EditText
    private var timePicker: TimePicker
    private var repeatSwitch: SwitchCompat
    private val dropdownArrow: ImageView
    private var createRoutineButton: LinearLayout
    private var addConditionButton: Button
    private var radioButtonTime: RadioButton
    private var radioButtonSensor: RadioButton
    private var addActionButton: Button
    private var createRoutineConditionDeviceSpinner: Spinner
    private var createRoutineConditionFunctionSpinner: Spinner
    private var createRoutineActionDeviceSpinner: Spinner
    private var createRoutineActionFunctionSpinner: Spinner
    private var removeAllActionsButton: Button
    private var createRoutineContainer: LinearLayout
    private var selectTriggerTypeRadioGroup: RadioGroup
    private var timeLayout: LinearLayout
    private var sensorLayout: LinearLayout
    private var actionContainerLinearLayout: LinearLayout
    private var valueActionEditText: EditText
    private var logText: TextView

    private var createRoutineOpened: Boolean = false
    private var deviceToFunctionsMap = mutableMapOf<DeviceDTO, List<FunctionDTO>>()
    private var routineEventList = mutableListOf<RoutineEventDTO>()

    init {
        LayoutInflater.from(context).inflate(R.layout.option_create_routine, this, true)
        orientation = LinearLayout.VERTICAL

        sensorValueConditionEditText = findViewById(R.id.sensorValueCondition)
        conditionHelperText = findViewById(R.id.sensorHelpText)
        comparisonTypeSpinner = findViewById(R.id.comparisonTypeSpinner)
        createRoutineExecButton = findViewById(R.id.createRoutineExecButton)
        textFieldRoutineName = findViewById(R.id.textfieldRoutineName)
        timePicker = findViewById(R.id.timePicker)
        repeatSwitch = findViewById(R.id.repeatSwitch)
        dropdownArrow = findViewById(R.id.dropdown_arrow)
        createRoutineButton = findViewById(R.id.createRoutineButton)
        addConditionButton = findViewById(R.id.addConditionButton)
        radioButtonTime = findViewById(R.id.radioButtonTime)
        radioButtonSensor = findViewById(R.id.radioButtonSensor)
        addActionButton = findViewById(R.id.addActionButton)
        removeAllActionsButton = findViewById(R.id.removeAllActionsButton)
        createRoutineContainer = findViewById(R.id.createRoutineContainer)
        selectTriggerTypeRadioGroup = findViewById(R.id.selectTrigger)
        timeLayout = findViewById(R.id.timeLayout)
        sensorLayout = findViewById(R.id.sensorLayout)
        actionContainerLinearLayout = findViewById(R.id.actionContainerLinearLayout)
        createRoutineConditionDeviceSpinner = findViewById(R.id.deviceSelectorCondition)
        createRoutineConditionFunctionSpinner = findViewById(R.id.functionSelectorCondition)
        createRoutineActionDeviceSpinner = findViewById(R.id.deviceActionSpinner)
        createRoutineActionFunctionSpinner = findViewById(R.id.functionActionSpinner)
        valueActionEditText = findViewById(R.id.valueActionInputField)
        logText = findViewById(R.id.logText)

        createRoutineOpened = false
        setCreateRoutineOpen(createRoutineOpened)

        selectTriggerTypeRadioGroup.visibility = GONE
        timeLayout.visibility = GONE
        sensorLayout.visibility = GONE

        initButtons()
    }

    private fun setCreateRoutineOpen(opened: Boolean) {
        this.createRoutineOpened = opened
        if(opened) {
            createRoutineContainer.visibility = VISIBLE
            dropdownArrow.setImageResource(R.drawable.dropdown_arrow_down)
            updateDeviceToFunctionsMap()
            populateDeviceSpinner(createRoutineActionDeviceSpinner, deviceToFunctionsMap)
        } else {
            createRoutineContainer.visibility = GONE
            dropdownArrow.setImageResource(R.drawable.dropdown_arrow_right)
        }
    }

    private fun addConditionButtonOnClick() {
        selectTriggerTypeRadioGroup.visibility = VISIBLE
        addConditionButton.visibility = GONE
    }

    private fun radioButtonTimeOnClick() {
        timeLayout.visibility = VISIBLE
        sensorLayout.visibility = GONE
    }

    private fun radioButtonSensorOnClick() {
        timeLayout.visibility = GONE
        sensorLayout.visibility = VISIBLE
        populateDeviceSpinner(createRoutineConditionDeviceSpinner, deviceToFunctionsMap)
    }

    private fun updateDeviceToFunctionsMap() {
        //Get all device ids
        val deviceIDList = ServiceProvider.devicesService.getDeviceList()
        //get all devices from their ids
        val deviceList = mutableListOf<DeviceDTO>()
        for(id: String in deviceIDList) {
            val d = ServiceProvider.devicesService.getDeviceInfo(id)
            if(d != null) deviceList.add(d)
        }
        //create the mapping from devices to their functions
        for(d in deviceList) {
            val functions = mutableListOf<FunctionDTO>()
            for(fId in d.functionIds) {
                val f = ServiceProvider.functionService.getFunctionInfo(fId)
                if(f != null) functions.add(f)
            }
            deviceToFunctionsMap[d] = functions
        }
    }

    private fun addActionButtonOnClick() {
        if(valueActionEditText.text.isBlank() or valueActionEditText.text.isEmpty()) return
        if(createRoutineActionFunctionSpinner.adapter.count == 0) return
        val deviceName = createRoutineActionDeviceSpinner.selectedItem.toString()
        var deviceId: String = ""
        val functionName = createRoutineActionFunctionSpinner.selectedItem.toString()
        var functionID: Long = 0
        for(kvp in deviceToFunctionsMap) {
            if(kvp.key.deviceName == deviceName) {
                deviceId = kvp.key.deviceId
                for(f in kvp.value) {
                    if(f.functionName == functionName) {
                        functionID = f.functionId
                        break
                    }
                }
                break
            }
        }
        routineEventList.add(RoutineEventDTO(deviceId, functionID, valueActionEditText.text.toString().toFloat()))
        //Create new view and add it to list and layout
        val newView = inflate(context, R.layout.action_tile_view, null)
        newView.findViewById<TextView>(R.id.textViewDevice).text = deviceName
        newView.findViewById<TextView>(R.id.textViewFunction).text = functionName
        newView.findViewById<TextView>(R.id.textViewValue).text = valueActionEditText.text.toString()
        actionContainerLinearLayout.addView(newView)
    }

    private fun removeAllActionsButtonOnClick() {
        actionContainerLinearLayout.removeAllViews()
        routineEventList.clear()
    }

    private fun updateSensorHelperText() {

        var functionDTO: FunctionDTO? = null

        for(kvp in deviceToFunctionsMap) {
            if(kvp.key.deviceName == createRoutineConditionDeviceSpinner.selectedItem.toString()) {
                for(f in kvp.value) {
                    if(f.functionName == createRoutineConditionFunctionSpinner.selectedItem.toString()) {
                        functionDTO = f
                        break
                    }
                }
                break
            }
        }

        comparisonTypeSpinner.visibility = GONE

        if(functionDTO == null) {
            conditionHelperText.text = "Funktion kann nicht gefunden werden"
            return
        }

        if(functionDTO.onOff != null) {
            conditionHelperText.text = "Das Eingabefeld darf nur den Wert 0 oder 1 enthalten."
            return
        }
        if(functionDTO.rangeDTO != null) {
            conditionHelperText.text = "Das Eingabefeld darf nur einen Wert zwischen ${functionDTO.rangeDTO!!.minValue} und ${functionDTO.rangeDTO!!.maxValue} enthalten"
            comparisonTypeSpinner.visibility = VISIBLE
            return
        }
        conditionHelperText.text = "Für diese Funktion kann kein Wert abgefragt werden"
    }

    private fun createRoutineExecButtonOnClick() {

        if(textFieldRoutineName.text.isNullOrBlank() || textFieldRoutineName.text.isEmpty()) {
            logText.text = "Du musst einen Namen für den Ablauf angeben"
            return
        }
        if(timeLayout.visibility == GONE && sensorLayout.visibility == GONE) {
            logText.text = "Du musst einen Auslösehandlung definieren"
            return
        }
        if(sensorLayout.visibility == VISIBLE) {
            if(sensorValueConditionEditText.text.isNullOrBlank() || sensorValueConditionEditText.text.isEmpty()) {
                logText.text = "Du musst einen Wert für den Sensor angeben"
                return
            }
        }
        if(routineEventList.isEmpty()) {
            logText.text = "Du musst mindestens eine Aktion definieren"
            return
        }

        if(radioButtonSensor.isSelected) {
            var deviceId: String = ""
            var function: FunctionDTO?
            for(kvp in deviceToFunctionsMap) {
                if(kvp.key.deviceName == createRoutineConditionDeviceSpinner.selectedItem.toString()) {
                    deviceId = kvp.key.deviceId
                    for(f in kvp.value) {
                        if(f.functionName == createRoutineConditionFunctionSpinner.selectedItem.toString()) {
                            function = FunctionDTO.Builder(
                                f.functionName,
                                f.functionId,
                                if(f.rangeDTO != null) RangeDTO(f.rangeDTO!!.minValue, f.rangeDTO!!.maxValue,
                                    sensorValueConditionEditText.text.toString().toDouble()
                                ) else null,
                                if(f.onOff != null) sensorValueConditionEditText.text.toString().toInt() != 0 else null,
                                f.outputValue,
                                f.outputTrigger
                            ).build()

                            val routineDTO: RoutineDTO = RoutineDTO.Builder(
                                textFieldRoutineName.text.toString(),
                                if(comparisonTypeSpinner.selectedItem.toString() == "<") 0 else if(comparisonTypeSpinner.selectedItem.toString() == "=") 1 else 2,
                                routineEventList as java.util.ArrayList<RoutineEventDTO>,
                                -1,
                                if(radioButtonTime.isSelected) TriggerTimeDTO(LocalTime.of(timePicker.hour, timePicker.minute), repeatSwitch.isChecked) else null,
                                if(radioButtonSensor.isSelected) TriggerEventByDeviceDTO(
                                    deviceId,
                                    function,
                                    null,
                                    null
                                ) else null
                            ).build()
                            logText.text = "Anfrage wird gesendet"
                            val response = ServiceProvider.routineService.createRoutine(routineDTO)
                            if(response == null) logText.text = "Es ist ein Fehler aufgetreten"
                            else logText.text = "Der Ablauf wurde erfolgreich erstellt"
                            return
                        }
                    }
                    break
                }
            }
        }


        val routineDTO: RoutineDTO = RoutineDTO.Builder(
            textFieldRoutineName.text.toString(),
            if(comparisonTypeSpinner.selectedItem.toString() == "<") 0 else if(comparisonTypeSpinner.selectedItem.toString() == "=") 1 else 2,
            routineEventList as java.util.ArrayList<RoutineEventDTO>,
            -1,
            TriggerTimeDTO(LocalTime.of(timePicker.hour, timePicker.minute), repeatSwitch.isChecked),
            null
        ).build()
        ServiceProvider.routineService.createRoutine(routineDTO)
    }

    private fun populateDeviceSpinner(spinner: Spinner, deviceFunctionMap: MutableMap<DeviceDTO, List<FunctionDTO>>) {
        val deviceNames = mutableListOf<String>()
        for(kvp in deviceFunctionMap) {
            deviceNames.add(kvp.key.deviceName)
        }
        ArrayAdapter(context, R.layout.spinner_item, deviceNames.toTypedArray()).also {
                arrayAdapter ->
            arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            spinner.adapter = arrayAdapter
        }

    }

    private fun populateFunctionSpinnerByDevice(spinnerToPopulate: Spinner, deviceSpinner: Spinner, deviceFunctionMap: MutableMap<DeviceDTO, List<FunctionDTO>>) {
        if(deviceSpinner.selectedItem == null) return
        val selectedDevice = deviceSpinner.selectedItem.toString()
        for(kvp in deviceFunctionMap) {
            if(kvp.key.deviceName == selectedDevice) {
                val functionNames = kvp.value.map { f -> f.functionName }
                ArrayAdapter(context, R.layout.spinner_item, functionNames.toTypedArray()).also {
                        arrayAdapter ->
                    arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    spinnerToPopulate.adapter = arrayAdapter
                }
                break
            }
        }
    }

    private fun initButtons() {
        createRoutineButton.setOnClickListener {
            setCreateRoutineOpen(!createRoutineOpened)
        }

        addConditionButton.setOnClickListener {
            addConditionButtonOnClick()
        }
        radioButtonTime.setOnClickListener {
            radioButtonTimeOnClick()
        }
        radioButtonSensor.setOnClickListener {
            radioButtonSensorOnClick()
        }

        addActionButton.setOnClickListener {
            addActionButtonOnClick()
        }

        removeAllActionsButton.setOnClickListener {
            removeAllActionsButtonOnClick()
        }

        createRoutineExecButton.setOnClickListener {
            createRoutineExecButtonOnClick()
        }

        createRoutineConditionDeviceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                populateFunctionSpinnerByDevice(createRoutineConditionFunctionSpinner, createRoutineConditionDeviceSpinner, deviceToFunctionsMap)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) { /*Nothing should happen*/ }
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Nothing should happen here
            }
        }

        createRoutineConditionFunctionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                updateSensorHelperText()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) { /*Nothing should happen*/ }
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Nothing should happen here
            }
        }

        createRoutineActionDeviceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                populateFunctionSpinnerByDevice(createRoutineActionFunctionSpinner, createRoutineActionDeviceSpinner, deviceToFunctionsMap)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) { /*Nothing should happen*/ }
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Nothing should happen here
            }
        }

        val comparisonList = listOf<String>("<", "=", ">")
        ArrayAdapter(context, R.layout.spinner_item, comparisonList.toTypedArray()).also {
                arrayAdapter ->
            arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            comparisonTypeSpinner.adapter = arrayAdapter

        }
    }

}