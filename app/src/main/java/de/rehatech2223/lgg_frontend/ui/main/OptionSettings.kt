package de.rehatech2223.lgg_frontend.ui.main

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.TimePicker
import androidx.preference.PreferenceManager
import de.rehatech2223.datamodel.DeviceDTO
import de.rehatech2223.datamodel.FunctionDTO
import de.rehatech2223.datamodel.RoutineDTO
import de.rehatech2223.datamodel.util.RangeDTO
import de.rehatech2223.datamodel.util.RoutineEventDTO
import de.rehatech2223.datamodel.util.TriggerEventByDeviceDTO
import de.rehatech2223.datamodel.util.TriggerTimeDTO
import de.rehatech2223.lgg_frontend.DynamicThemeActivity
import de.rehatech2223.lgg_frontend.R
import de.rehatech2223.lgg_frontend.ThemeEnum
import de.rehatech2223.lgg_frontend.services.ServiceProvider
import java.time.LocalTime

const val BUTTON_STATE_KEY = "ButtonState"

class OptionSettings(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    private var openButton: Button
    private var radiobuttonDefault: RadioButton
    private var radiobuttonLight: RadioButton
    private var radiobuttonHCOne: RadioButton
    private var settingContainer: LinearLayout
    private var settingsOpened: Boolean = false

    init {

        LayoutInflater.from(context).inflate(R.layout.option_settings, this, true)
        orientation = VERTICAL

        openButton = findViewById(R.id.openButton)
        settingContainer = findViewById(R.id.setting_container)
        radiobuttonDefault = findViewById(R.id.radioButton_default)
        radiobuttonLight = findViewById(R.id.radioButton_light)
        radiobuttonHCOne = findViewById(R.id.radioButton_hc1)

        initButtons()
        initColorSchemeButtons()
        loadState()
    }

    private fun loadState() {
        settingsOpened = PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(BUTTON_STATE_KEY, false)
        setSettingsOpen(settingsOpened)

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

    private fun initButtons() {
        openButton.setOnClickListener {
            setSettingsOpen(!settingsOpened)
        }
    }

    private fun initColorSchemeButtons() {
        radiobuttonDefault.setOnClickListener { view -> changeColorTheme(view) }
        radiobuttonLight.setOnClickListener { view -> changeColorTheme(view) }
        radiobuttonHCOne.setOnClickListener { view -> changeColorTheme(view) }
    }

    private fun changeColorTheme(view: View) {
        val activity: DynamicThemeActivity = context as DynamicThemeActivity
        if (view is RadioButton && view.isChecked) {
            when (view.id) {
                R.id.radioButton_default -> activity.changeTheme(ThemeEnum.DEFAULT)
                R.id.radioButton_light -> activity.changeTheme(ThemeEnum.LIGHT)
                R.id.radioButton_hc1 -> activity.changeTheme(ThemeEnum.HIGH_CONTRAST_ONE)
            }
        }
    }

}