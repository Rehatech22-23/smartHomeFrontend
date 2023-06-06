package de.rehatech2223.lgg_frontend.ui.options

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.widget.AdapterView.INVALID_POSITION
import android.widget.AdapterView.OnItemSelectedListener
import androidx.preference.PreferenceManager
import de.rehatech2223.datamodel.RoutineDTO
import de.rehatech2223.lgg_frontend.DynamicThemeActivity
import de.rehatech2223.lgg_frontend.R
import de.rehatech2223.lgg_frontend.ThemeEnum
import de.rehatech2223.lgg_frontend.services.ServiceProvider

const val BUTTON_STATE_KEY = "ButtonState"
const val DEVICE_FRAGMENT_INDEX_KEY = "DeviceFragmentIndex"
const val PIN_ROUTINE_ID_KEY = "PinRoutineId"
const val PIN_ROUTINE_SELECTION_INDEX_KEY = "PinRoutineSelectionIndex"

class OptionSettings(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    private val openButton: LinearLayout
    private var radiobuttonDefault: RadioButton
    private var radiobuttonLight: RadioButton
    private var radiobuttonHCOne: RadioButton
    private var radiobuttonCB: RadioButton
    private var radiobuttonBlackWhite: RadioButton
    private var settingContainer: LinearLayout
    private var updateDevicesText: TextView

    private var settingsOpened: Boolean = false

    init {

        LayoutInflater.from(context).inflate(R.layout.option_settings, this, true)
        orientation = VERTICAL

        openButton = findViewById(R.id.openButton)
        settingContainer = findViewById(R.id.setting_container)
        radiobuttonDefault = findViewById(R.id.radioButton_default)
        radiobuttonLight = findViewById(R.id.radioButton_light)
        radiobuttonHCOne = findViewById(R.id.radioButton_hc1)
        radiobuttonCB = findViewById(R.id.radioButton_cb)
        radiobuttonBlackWhite = findViewById(R.id.radioButton_blwh)
        updateDevicesText = findViewById(R.id.updateDevicesText)

        initButtons()
        initColorSchemeButtons()
        initStartPageOption()
        initPinRoutineOption()
        loadState()
    }

    private fun loadState() {
        settingsOpened = PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(BUTTON_STATE_KEY, false)
        setSettingsOpen(settingsOpened)

        when ((context as DynamicThemeActivity).getCurrentTheme()) {
            ThemeEnum.DEFAULT.theme -> radiobuttonDefault.isChecked = true
            ThemeEnum.LIGHT.theme -> radiobuttonLight.isChecked = true
            ThemeEnum.HIGH_CONTRAST_ONE.theme -> radiobuttonHCOne.isChecked = true
            ThemeEnum.COLOR_BLIND.theme -> radiobuttonCB.isChecked = true
            ThemeEnum.BLACK_WHITE.theme -> radiobuttonBlackWhite.isChecked = true
        }

        val startScreenSpinner: Spinner = findViewById(R.id.start_screen_spinner)
        startScreenSpinner.setSelection(
            PreferenceManager
                .getDefaultSharedPreferences(context).getInt(DEVICE_FRAGMENT_INDEX_KEY, 0)
        )
        val pinRoutineSpinner: Spinner = findViewById(R.id.pin_routine_spinner)
        pinRoutineSpinner.setSelection(
            PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(PIN_ROUTINE_SELECTION_INDEX_KEY, 0)
        )
    }

    private fun changeColorTheme(view: View) {
        val activity: DynamicThemeActivity = context as DynamicThemeActivity
        if (view is RadioButton && view.isChecked) {
            when (view.id) {
                R.id.radioButton_default -> activity.changeTheme(ThemeEnum.DEFAULT)
                R.id.radioButton_light -> activity.changeTheme(ThemeEnum.LIGHT)
                R.id.radioButton_hc1 -> activity.changeTheme(ThemeEnum.HIGH_CONTRAST_ONE)
                R.id.radioButton_cb -> activity.changeTheme(ThemeEnum.COLOR_BLIND)
                R.id.radioButton_blwh -> activity.changeTheme(ThemeEnum.BLACK_WHITE)
            }
        }
    }

    private fun initColorSchemeButtons() {
        radiobuttonDefault.setOnClickListener { view -> changeColorTheme(view) }
        radiobuttonLight.setOnClickListener { view -> changeColorTheme(view) }
        radiobuttonHCOne.setOnClickListener { view -> changeColorTheme(view) }
        radiobuttonCB.setOnClickListener { view -> changeColorTheme(view) }
        radiobuttonBlackWhite.setOnClickListener { view -> changeColorTheme(view) }
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
            updateDevicesText.text = "Aktualisiere die Datenbank"
            val success = ServiceProvider.devicesService.updateDeviceDatabase()
            if (success) updateDevicesText.text = "Datenbank erfolgreich aktualisiert"
            else updateDevicesText.text = "Datenbank konnte nicht aktualisiert werden"
        }
        openButton.setOnClickListener {
            setSettingsOpen(!settingsOpened)
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
                //(context as DynamicThemeActivity).refreshCurrentTheme()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun initPinRoutineOption() {
        val pinRoutineSpinner: Spinner = findViewById(R.id.pin_routine_spinner)
        val comparisonList = mutableListOf("Keine Routine Anheften")
        val indexToRoutineDTOIdMap: Map<Int, RoutineDTO> = getRoutineDTOIdMapping()
        for (routine in indexToRoutineDTOIdMap.values) {
            comparisonList.add(routine.routineName.split(':')[1])
        }
        ArrayAdapter(
            context,
            R.layout.spinner_item,
            comparisonList.toTypedArray()
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            pinRoutineSpinner.adapter = arrayAdapter
        }
        pinRoutineSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                if (pinRoutineSpinner.selectedItemPosition == 0
                    || pinRoutineSpinner.selectedItemPosition == INVALID_POSITION
                ) {
                    PreferenceManager.getDefaultSharedPreferences(context).edit()
                        .putLong(PIN_ROUTINE_ID_KEY, -1L).apply()
                    PreferenceManager.getDefaultSharedPreferences(context).edit()
                        .putInt(PIN_ROUTINE_SELECTION_INDEX_KEY, 0).apply()
                } else {
                    val dTOId: Long =
                        indexToRoutineDTOIdMap[pinRoutineSpinner.selectedItemPosition]!!.routineId
                    PreferenceManager.getDefaultSharedPreferences(context).edit()
                        .putLong(PIN_ROUTINE_ID_KEY, dTOId).apply()
                    PreferenceManager.getDefaultSharedPreferences(context).edit()
                        .putInt(
                            PIN_ROUTINE_SELECTION_INDEX_KEY,
                            pinRoutineSpinner.selectedItemPosition
                        ).apply()
                }
                //(context as DynamicThemeActivity).refreshCurrentTheme()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun getRoutineDTOIdMapping(): Map<Int, RoutineDTO> {
        val map: MutableMap<Int, RoutineDTO> = HashMap()
        var index = 1
        for (routine in ServiceProvider.routineService.getRoutineList()) {
            map[index] = routine
            index++
        }
        return map
    }

    /**
     *  Can be used as a mock refresh method, when ui-updates are required
     */

    private fun reloadCurrentColorTheme() {
        val activity: DynamicThemeActivity = context as DynamicThemeActivity
        activity.changeTheme(ThemeEnum from activity.getCurrentTheme())
    }
}