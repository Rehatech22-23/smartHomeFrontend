package de.rehatech2223.lgg_frontend.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import de.rehatech2223.datamodel.DeviceDTO
import de.rehatech2223.datamodel.RoutineDTO
import de.rehatech2223.lgg_frontend.R
import de.rehatech2223.lgg_frontend.services.ServiceProvider

class FragmentRoutines : Fragment() {

    private lateinit var mainLinearLayout: LinearLayout
    private var currentLinearLayout: LinearLayout? = null
    private val routineTiles: ArrayList<RoutineTile> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_routines, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainLinearLayout = view.findViewById(R.id.linearLayout);

        loadRoutineTiles()
    }
    private fun loadRoutineTiles() {
        val routineList = ServiceProvider.routineService.getRoutineList()
        for (routineId in routineList) {
            val routineDTO = ServiceProvider.routineService.getRoutineInfo(routineId) ?: continue
            val routineTile = addRoutineTile(routineDTO)
            routineTiles.add(routineTile)
        }
    }


    private fun addRoutineTile(routineDTO: RoutineDTO): RoutineTile {
        val routineTile = RoutineTile(requireContext(), null, routineDTO)
        if (routineTiles.size % 3 == 0) {
            currentLinearLayout = layoutInflater.inflate(R.layout.routines_view, null)
                .findViewById(R.id.linearLayout)
            (currentLinearLayout as LinearLayout).clipChildren = false
            mainLinearLayout.addView(currentLinearLayout)
        }
        currentLinearLayout?.addView(routineTile)
        return routineTile
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentRoutines().apply {
                arguments = Bundle().apply {}
            }
    }
}