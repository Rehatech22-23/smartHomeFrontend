package de.rehatech2223.lgg_frontend.ui.main

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.preference.PreferenceManager
import de.rehatech2223.lgg_frontend.DynamicThemeActivity
import de.rehatech2223.lgg_frontend.R
import de.rehatech2223.lgg_frontend.ThemeEnum

const val BUTTON_STATE_KEY = "ButtonState"

class OptionSettings(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    private var openButton: Button
    private var createRoutineButton: Button

    private var settingContainer: LinearLayout
    private var createRoutineContainer: LinearLayout

    private var settingsOpened: Boolean = false
    private var createRoutineOpened: Boolean = false
    private var deleteRoutineOpened: Boolean = false

    private var radiobuttonDefault: RadioButton
    private var radiobuttonHCOne: RadioButton

    init {
        LayoutInflater.from(context).inflate(R.layout.option_settings, this, true)
        orientation = VERTICAL

        createRoutineButton = findViewById(R.id.createRoutineButton)
        openButton = findViewById(R.id.openButton)

        settingContainer = findViewById(R.id.setting_container)
        createRoutineContainer = findViewById(R.id.createRoutineContainer)

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

    private fun initButtons() {
        openButton.setOnClickListener {
            setSettingsOpen(!settingsOpened)
        }
        createRoutineButton.setOnClickListener {
            setCreateRoutineOpen(!createRoutineOpened)
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