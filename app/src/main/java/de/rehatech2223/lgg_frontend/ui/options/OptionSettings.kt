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
    private var radiobuttonHCTwo: RadioButton
    private var radiobuttonHCThree: RadioButton
    private var radiobuttonBlackWhite: RadioButton
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
        radiobuttonHCTwo = findViewById(R.id.radioButton_hc2)
        radiobuttonHCThree = findViewById(R.id.radioButton_hc3)
        radiobuttonBlackWhite = findViewById(R.id.radioButton_blwh)

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
            ThemeEnum.HIGH_CONTRAST_TWO.theme -> radiobuttonHCTwo.isChecked = true
            ThemeEnum.HIGH_CONTRAST_THREE.theme -> radiobuttonHCThree.isChecked = true
            ThemeEnum.BLACK_WHITE.theme -> radiobuttonBlackWhite.isChecked = true
        }

        val startScreenSpinner: Spinner = findViewById(R.id.start_screen_spinner)
        startScreenSpinner.setSelection(PreferenceManager
            .getDefaultSharedPreferences(context).getInt(DEVICE_FRAGMENT_INDEX_KEY, 0))
        val pinRoutineSpinner: Spinner = findViewById(R.id.pin_routine_spinner)
        pinRoutineSpinner.setSelection(PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(PIN_ROUTINE_SELECTION_INDEX_KEY, 0))
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
        radiobuttonHCTwo.setOnClickListener { view -> changeColorTheme(view) }
        radiobuttonHCThree.setOnClickListener { view -> changeColorTheme(view) }
        radiobuttonBlackWhite.setOnClickListener { view -> changeColorTheme(view) }
    }

    private fun changeColorTheme(view: View) {
        val activity: DynamicThemeActivity = context as DynamicThemeActivity
        if (view is RadioButton && view.isChecked) {
            when (view.id) {
                R.id.radioButton_default -> activity.changeTheme(ThemeEnum.DEFAULT)
                R.id.radioButton_light -> activity.changeTheme(ThemeEnum.LIGHT)
                R.id.radioButton_hc1 -> activity.changeTheme(ThemeEnum.HIGH_CONTRAST_ONE)
                R.id.radioButton_hc2 -> activity.changeTheme(ThemeEnum.HIGH_CONTRAST_TWO)
                R.id.radioButton_hc3 -> activity.changeTheme(ThemeEnum.HIGH_CONTRAST_THREE)
                R.id.radioButton_blwh -> activity.changeTheme(ThemeEnum.BLACK_WHITE)
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

    private fun initPinRoutineOption(){
        val pinRoutineSpinner: Spinner = findViewById(R.id.pin_routine_spinner)
        val comparisonList = mutableListOf("Keine Routine Anheften")
        val indexToRoutineDTOIdMap: Map<Int, Long> = getRoutineDTOIdMapping()
        for (routineId: Long in indexToRoutineDTOIdMap.values){
            val routineDTO: RoutineDTO = ServiceProvider.routineService.getRoutineInfo(routineId)!!
            comparisonList.add(routineDTO.routineName.split(':')[1])
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
                    || pinRoutineSpinner.selectedItemPosition == INVALID_POSITION){
                    PreferenceManager.getDefaultSharedPreferences(context).edit()
                        .putLong(PIN_ROUTINE_ID_KEY, -1L).apply()
                    PreferenceManager.getDefaultSharedPreferences(context).edit()
                        .putInt(PIN_ROUTINE_SELECTION_INDEX_KEY, 0).apply()
                }else{
                    val dTOId: Long = indexToRoutineDTOIdMap[pinRoutineSpinner.selectedItemPosition]!!
                    PreferenceManager.getDefaultSharedPreferences(context).edit()
                        .putLong(PIN_ROUTINE_ID_KEY, dTOId).apply()
                    PreferenceManager.getDefaultSharedPreferences(context).edit()
                        .putInt(PIN_ROUTINE_SELECTION_INDEX_KEY, pinRoutineSpinner.selectedItemPosition).apply()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun getRoutineDTOIdMapping(): Map<Int, Long> {
        val map: MutableMap<Int, Long> = HashMap()
        var index = 1
        for(id in ServiceProvider.routineService.getRoutineList()){
            map[index] = id
            index++
        }
        return map
    }
}