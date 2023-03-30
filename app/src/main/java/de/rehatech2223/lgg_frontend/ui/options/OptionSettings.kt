package de.rehatech2223.lgg_frontend.ui.options

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.preference.PreferenceManager
import de.rehatech2223.lgg_frontend.DynamicThemeActivity
import de.rehatech2223.lgg_frontend.R
import de.rehatech2223.lgg_frontend.ThemeEnum
import de.rehatech2223.lgg_frontend.services.ServiceProvider
import de.rehatech2223.lgg_frontend.ui.main.TabFragmentStateAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

const val BUTTON_STATE_KEY = "ButtonState"
const val DEVICE_FRAGMENT_INDEX_KEY = "DeviceFragmentIndex"

class OptionSettings(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    private val openButton: LinearLayout
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
        initStartPageOption()
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

        val startScreenSpinner: Spinner = findViewById(R.id.start_screen_spinner)
        startScreenSpinner.setSelection(PreferenceManager
            .getDefaultSharedPreferences(context).getInt(DEVICE_FRAGMENT_INDEX_KEY, 0))
    }

    private fun setSettingsOpen(opened: Boolean) {
        val dropdownArrow: ImageView = findViewById(R.id.dropdown_arrow)
        this.settingsOpened = opened
        if (opened) {
            settingContainer.visibility = VISIBLE
            dropdownArrow.setImageResource(R.drawable.dropdown_arrow_down)
        } else {
            settingContainer.visibility = GONE
            dropdownArrow.setImageResource(R.drawable.dropdown_arrow_right)
        }
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putBoolean(BUTTON_STATE_KEY, opened).apply()
    }

    private fun initButtons() {
        val updateDevicesButton: Button = findViewById(R.id.update_db_button)
        updateDevicesButton.setOnClickListener {
            ServiceProvider.devicesService.updateDeviceDatabase()
        }
        openButton.setOnClickListener {
            Log.d("handler", "clicked dropdown")
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

    private fun initStartPageOption() {
        val startScreenSpinner: Spinner = findViewById(R.id.start_screen_spinner)
        val comparisonList = listOf("Geräte", "Abläufe")
        ArrayAdapter(
            context,
            R.layout.spinner_item,
            comparisonList.toTypedArray()
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            startScreenSpinner.adapter = arrayAdapter
        }
        startScreenSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                when (startScreenSpinner.selectedItem) {
                    "Geräte" -> PreferenceManager.getDefaultSharedPreferences(context).edit()
                        .putInt(DEVICE_FRAGMENT_INDEX_KEY, 0).apply()
                    "Abläufe" -> PreferenceManager.getDefaultSharedPreferences(context).edit()
                        .putInt(DEVICE_FRAGMENT_INDEX_KEY, 1).apply()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}