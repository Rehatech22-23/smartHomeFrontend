package de.rehatech2223.lgg_frontend.ui.options

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import de.rehatech2223.lgg_frontend.R
import de.rehatech2223.lgg_frontend.services.ServiceProvider

class OptionDeleteRoutine(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    private var deleteRoutineButton: LinearLayout
    private val dropdownArrow: ImageView
    private var deleteRoutineExecButton: Button
    private var deleteRoutineSpinner: Spinner
    private var deleteRoutineContainer: LinearLayout

    private var spinnerMap = mutableMapOf<String, Long>()
    private var deleteRoutineOpened: Boolean = false

    init {
        spinnerMap

        LayoutInflater.from(context).inflate(R.layout.option_delete_routine, this, true)
        orientation = LinearLayout.VERTICAL

        dropdownArrow = findViewById(R.id.dropdown_arrow)
        deleteRoutineButton = findViewById(R.id.deleteRoutineButton)
        deleteRoutineExecButton = findViewById(R.id.deleteRoutineExecButton)
        deleteRoutineSpinner = findViewById(R.id.deleteRoutineSpinner)
        deleteRoutineContainer = findViewById(R.id.deleteRoutineContainer)

        deleteRoutineOpened = false
        setDeleteRoutineOpen(deleteRoutineOpened)

        initButtons()
    }

    private fun setDeleteRoutineOpen(opened: Boolean) {
        this.deleteRoutineOpened = opened
        if(opened) {
            deleteRoutineContainer.visibility = VISIBLE
            dropdownArrow.setImageResource(R.drawable.dropdown_arrow_down)
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
            dropdownArrow.setImageResource(R.drawable.dropdown_arrow_right)
        }
    }

    private fun deleteRoutineButtonOnClick() {
        val routineId = spinnerMap[deleteRoutineSpinner.selectedItem.toString()]
        if(routineId != null) ServiceProvider.routineService.deleteRoutine(routineId)
    }

    private fun initButtons() {
        deleteRoutineButton.setOnClickListener {
            setDeleteRoutineOpen(!deleteRoutineOpened)
        }
        deleteRoutineExecButton.setOnClickListener() {
            deleteRoutineButtonOnClick()
        }
    }

}