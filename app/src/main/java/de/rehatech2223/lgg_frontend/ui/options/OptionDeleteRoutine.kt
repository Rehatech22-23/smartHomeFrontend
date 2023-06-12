package de.rehatech2223.lgg_frontend.ui.options

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import de.rehatech2223.datamodel.RoutineDTO
import de.rehatech2223.lgg_frontend.R
import de.rehatech2223.lgg_frontend.services.ServiceProvider

/**
 * Option UI Element, subclass of LinearLayout.
 * Manages the option for deleting routines.
 *
 * @author Jan Pixberg, Fynn Debus
 */
class OptionDeleteRoutine(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    private var deleteRoutineButton: LinearLayout
    private val dropdownArrow: ImageView
    private var deleteRoutineExecButton: Button
    private var deleteRoutineSpinner: Spinner
    private var deleteRoutineContainer: LinearLayout
    private var deleteRoutineText: TextView

    private var spinnerMap = mutableMapOf<String, RoutineDTO>()
    private var deleteRoutineOpened: Boolean = false

    init {

        LayoutInflater.from(context).inflate(R.layout.option_delete_routine, this, true)
        orientation = LinearLayout.VERTICAL

        dropdownArrow = findViewById(R.id.dropdown_arrow)
        deleteRoutineButton = findViewById(R.id.deleteRoutineButton)
        deleteRoutineExecButton = findViewById(R.id.deleteRoutineExecButton)
        deleteRoutineSpinner = findViewById(R.id.deleteRoutineSpinner)
        deleteRoutineContainer = findViewById(R.id.deleteRoutineContainer)
        deleteRoutineText = findViewById(R.id.deleteRoutineText)

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
            for (routine in routineList) {
                val name = routine.routineName
                    spinnerMap[name] = routine
                    namesList.add(name)
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
        val routineId = spinnerMap[deleteRoutineSpinner.selectedItem.toString()]?.routineId
        if(routineId != null) {
            deleteRoutineText.text = "Lösche den Ablauf"
            val success = ServiceProvider.routineService.deleteRoutine(routineId)
            if(success) deleteRoutineText.text = "Ablauf erfolgreich gelöscht"
            else deleteRoutineText.text = "Ablauf konnte nicht gelöscht werden"
        }
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