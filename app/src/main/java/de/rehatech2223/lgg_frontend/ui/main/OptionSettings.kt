package de.rehatech2223.lgg_frontend.ui.main

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Spinner
import androidx.preference.PreferenceManager
import de.rehatech2223.lgg_frontend.DynamicThemeActivity
import de.rehatech2223.lgg_frontend.R
import de.rehatech2223.lgg_frontend.ThemeEnum
import de.rehatech2223.lgg_frontend.services.ServiceProvider

const val BUTTON_STATE_KEY = "ButtonState"

class OptionSettings(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    private var deleteRoutineSpinner: Spinner
    private var spinnerMap = mutableMapOf<String, Long>()

    private var openButton: Button
    private var createRoutineButton: Button
    private var deleteRoutineButton: Button

    private var settingContainer: LinearLayout
    private var createRoutineContainer: LinearLayout
    private var deleteRoutineContainer: LinearLayout

    private var settingsOpened: Boolean = false
    private var createRoutineOpened: Boolean = false
    private var deleteRoutineOpened: Boolean = false

    private var radiobuttonDefault: RadioButton
    private var radiobuttonHCOne: RadioButton

    init {
        spinnerMap

        LayoutInflater.from(context).inflate(R.layout.option_settings, this, true)
        orientation = VERTICAL

        deleteRoutineSpinner = findViewById(R.id.deleteRoutineSpinner)

        createRoutineButton = findViewById(R.id.createRoutineButton)
        openButton = findViewById(R.id.openButton)
        deleteRoutineButton = findViewById(R.id.deleteRoutineButton)

        settingContainer = findViewById(R.id.setting_container)
        createRoutineContainer = findViewById(R.id.createRoutineContainer)
        deleteRoutineContainer = findViewById(R.id.deleteRoutineContainer)

        radiobuttonDefault = findViewById(R.id.radioButton_default)
        radiobuttonHCOne = findViewById(R.id.radioButton_hc1)

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
            ArrayAdapter(context, android.R.layout.simple_spinner_item, namesList.toArray()).also {
                arrayAdapter ->
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    deleteRoutineSpinner.adapter = arrayAdapter
            }
        } else {
            deleteRoutineContainer.visibility = GONE
            deleteRoutineButton.text = context.getString(R.string.deleteRoutine_unclicked)
        }
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