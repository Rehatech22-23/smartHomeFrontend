package de.rehatech2223.lgg_frontend.ui.main

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import de.rehatech2223.lgg_frontend.DynamicThemeActivity
import de.rehatech2223.lgg_frontend.MainActivity
import de.rehatech2223.lgg_frontend.R
import de.rehatech2223.lgg_frontend.ThemeEnum

const val BUTTON_STATE_KEY = "ButtonState"
const val THEME_RADIO_KEY = "ThemeRadioButtonId"

class OptionSettings(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    private var openButton: Button
    private var settingContainer: LinearLayout
    private var opened: Boolean = false
    private var radiobuttonDefault: RadioButton
    private var radiobuttonHCOne: RadioButton

    init {
        LayoutInflater.from(context).inflate(R.layout.option_settings, this, true)
        orientation = VERTICAL

        openButton = findViewById(R.id.openButton)
        settingContainer = findViewById(R.id.setting_container)
        radiobuttonDefault = findViewById(R.id.radioButton_default)
        radiobuttonHCOne = findViewById(R.id.radioButton_hc1)

        initButton()
        initColorSchemeButtons()
        loadSharedPreferences()
    }

    private fun loadSharedPreferences(){
        opened = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(BUTTON_STATE_KEY, false)
        setOpen(opened)

        val savedButtonId = PreferenceManager.getDefaultSharedPreferences(context).getInt(THEME_RADIO_KEY, R.id.radioButton_default)
        findViewById<RadioButton>(savedButtonId).isChecked = true
    }

    private fun setOpen(opened: Boolean) {
        this.opened = opened
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

    private fun initButton() {
        openButton.setOnClickListener {
            setOpen(!opened)
        }
    }

    private fun initColorSchemeButtons(){
        radiobuttonDefault.setOnClickListener { view -> changeColorTheme(view) }
        radiobuttonHCOne.setOnClickListener { view -> changeColorTheme(view) }
    }

    private fun changeColorTheme(view: View){
        val activity: DynamicThemeActivity = context as DynamicThemeActivity
        if(view is RadioButton && view.isChecked){
            when(view.id){
                R.id.radioButton_default -> activity.changeTheme(ThemeEnum.DEFAULT)
                R.id.radioButton_hc1 -> activity.changeTheme(ThemeEnum.HIGH_CONTRAST_ONE)
            }
            PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(THEME_RADIO_KEY, view.id).apply()
        }
    }

}