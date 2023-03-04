package de.rehatech2223.lgg_frontend.ui.main

import android.content.Context
import android.provider.MediaStore.Audio.Radio
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.preference.PreferenceManager
import de.rehatech2223.datamodel.DeviceDTO
import de.rehatech2223.datamodel.FunctionDTO
import de.rehatech2223.lgg_frontend.DynamicThemeActivity
import de.rehatech2223.lgg_frontend.R
import de.rehatech2223.lgg_frontend.ThemeEnum
import de.rehatech2223.lgg_frontend.services.PopUpService
import de.rehatech2223.lgg_frontend.services.ServiceProvider

const val BUTTON_STATE_KEY = "ButtonState"

class OptionSettings(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    private var deleteRoutineSpinner: Spinner
    private var spinnerMap = mutableMapOf<String, Long>()

    private var openButton: Button
    private var createRoutineButton: Button
    private var deleteRoutineButton: Button
    private var deleteRoutineExecButton: Button
    private var addConditionButton: Button
    private var radioButtonTime: RadioButton
    private var radioButtonSensor: RadioButton
    private var addActionButton: Button
    private var radiobuttonDefault: RadioButton
    private var radiobuttonHCOne: RadioButton
    private var createRoutineConditionDeviceSpinner: Spinner

    private var settingContainer: LinearLayout
    private var createRoutineContainer: LinearLayout
    private var deleteRoutineContainer: LinearLayout
    private var selectTriggerTypeRadioGroup: RadioGroup
    private var timeLayout: LinearLayout
    private var sensorLayout: LinearLayout

    private var settingsOpened: Boolean = false
    private var createRoutineOpened: Boolean = false
    private var deleteRoutineOpened: Boolean = false

    private var deviceToFunctionsMap = mutableMapOf<DeviceDTO, List<FunctionDTO>>()

    init {
        spinnerMap

        LayoutInflater.from(context).inflate(R.layout.option_settings, this, true)
        orientation = VERTICAL

        deleteRoutineSpinner = findViewById(R.id.deleteRoutineSpinner)

        createRoutineButton = findViewById(R.id.createRoutineButton)
        openButton = findViewById(R.id.openButton)
        deleteRoutineButton = findViewById(R.id.deleteRoutineButton)
        deleteRoutineExecButton = findViewById(R.id.deleteRoutineExecButton)
        addConditionButton = findViewById(R.id.addConditionButton)
        radioButtonTime = findViewById(R.id.radioButtonTime)
        radioButtonSensor = findViewById(R.id.radioButtonSensor)
        addActionButton = findViewById(R.id.addActionButton)

        settingContainer = findViewById(R.id.setting_container)
        createRoutineContainer = findViewById(R.id.createRoutineContainer)
        deleteRoutineContainer = findViewById(R.id.deleteRoutineContainer)
        selectTriggerTypeRadioGroup = findViewById(R.id.selectTrigger)
        timeLayout = findViewById(R.id.timeLayout)
        sensorLayout = findViewById(R.id.sensorLayout)

        radiobuttonDefault = findViewById(R.id.radioButton_default)
        radiobuttonHCOne = findViewById(R.id.radioButton_hc1)

        createRoutineConditionDeviceSpinner = findViewById(R.id.deviceSelectorCondition)

        initButtons()
        initColorSchemeButtons()
        loadState()
    }

    private fun loadState() {
        settingsOpened = PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(BUTTON_STATE_KEY, false)
        setSettingsOpen(settingsOpened)

        createRoutineOpened = PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(BUTTON_STATE_KEY, false)
        setCreateRoutineOpen(createRoutineOpened)

        deleteRoutineOpened = PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(BUTTON_STATE_KEY, false)
        setDeleteRoutineOpen(deleteRoutineOpened)

        selectTriggerTypeRadioGroup.visibility = GONE
        timeLayout.visibility = GONE
        sensorLayout.visibility = GONE

        when ((context as DynamicThemeActivity).getCurrentTheme()) {
            ThemeEnum.DEFAULT.theme -> radiobuttonDefault.isChecked = true
            ThemeEnum.HIGH_CONTRAST_ONE.theme -> radiobuttonHCOne.isChecked = true
        }
    }

    private fun setSettingsOpen(opened: Boolean) {
        this.settingsOpened = opened
        if (opened) {
            settingContainer.visibility = VISIBLE
            openButton.text = context.getString(R.string.settings_clicked)
        } else {
            settingContainer.visibility = GONE
            openButton.text = context.getString(R.string.settings_unclicked)
        }
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putBoolean(BUTTON_STATE_KEY, opened).apply()
    }

    private fun setCreateRoutineOpen(opened: Boolean) {
        this.createRoutineOpened = opened
        if(opened) {
            createRoutineContainer.visibility = VISIBLE
            createRoutineButton.text = context.getString(R.string.createRoutine_clicked)
        } else {
            createRoutineContainer.visibility = GONE
            createRoutineButton.text = context.getString(R.string.createRoutine_unclicked)
        }
    }

    private fun setDeleteRoutineOpen(opened: Boolean) {
        this.deleteRoutineOpened = opened
        if(opened) {
            deleteRoutineContainer.visibility = VISIBLE
            deleteRoutineButton.text = context.getString(R.string.deleteRoutine_clicked)
            //Fill up the Spinner
            val routineList = ServiceProvider.routineService.getRoutineList();
            spinnerMap.clear()
            val namesList = ArrayList<String>()
            for (id in routineList) {
                val name = ServiceProvider.routineService.getRoutineInfo(id)?.routineName
                if(name != null) {
                    spinnerMap[name] = id
                    namesList.add(name)
                }
            }
            ArrayAdapter(context, R.layout.spinner_item, namesList.toArray()).also {
                arrayAdapter ->
                    arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    deleteRoutineSpinner.adapter = arrayAdapter
            }
        } else {
            deleteRoutineContainer.visibility = GONE
            deleteRoutineButton.text = context.getString(R.string.deleteRoutine_unclicked)
        }
    }

    private fun deleteRoutineButtonOnClick() {
        val routineId = spinnerMap[deleteRoutineSpinner.selectedItem.toString()]
        if(routineId != null) ServiceProvider.routineService.deleteRoutine(routineId)
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
        populateCreateRoutineConditionDeviceSpinner()
    }

    private fun populateCreateRoutineConditionDeviceSpinner() {
        updateDeviceToFunctionsMap()
        val deviceNames = mutableListOf<String>()
        for(kvp in deviceToFunctionsMap) {
            deviceNames.add(kvp.key.deviceName)
        }
        ArrayAdapter(context, R.layout.spinner_item, deviceNames.toTypedArray()).also {
                arrayAdapter ->
            arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            createRoutineConditionDeviceSpinner.adapter = arrayAdapter
        }
    }

    private fun populateCreateRoutineConditionFunctionSpinner() {
        //TODO
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
        //remove functions and devices that can not be used
        //TODO
    }

    private fun initButtons() {
        openButton.setOnClickListener {
            setSettingsOpen(!settingsOpened)
        }
        createRoutineButton.setOnClickListener {
            setCreateRoutineOpen(!createRoutineOpened)
        }
        deleteRoutineButton.setOnClickListener {
            setDeleteRoutineOpen(!deleteRoutineOpened)
        }
        deleteRoutineExecButton.setOnClickListener() {
            deleteRoutineButtonOnClick()
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
    }

    private fun initColorSchemeButtons() {
        radiobuttonDefault.setOnClickListener { view -> changeColorTheme(view) }
        radiobuttonHCOne.setOnClickListener { view ->
            changeColorTheme(view)
        }
    }

    private fun changeColorTheme(view: View) {
        val activity: DynamicThemeActivity = context as DynamicThemeActivity
        if (view is RadioButton && view.isChecked) {
            when (view.id) {
                R.id.radioButton_default -> activity.changeTheme(ThemeEnum.DEFAULT)
                R.id.radioButton_hc1 -> activity.changeTheme(ThemeEnum.HIGH_CONTRAST_ONE)
            }
        }
    }

}