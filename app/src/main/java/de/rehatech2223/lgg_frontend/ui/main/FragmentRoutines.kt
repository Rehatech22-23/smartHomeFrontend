package de.rehatech2223.lgg_frontend.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.flexbox.FlexboxLayout
import de.rehatech2223.datamodel.DeviceDTO
import de.rehatech2223.datamodel.RoutineDTO
import de.rehatech2223.lgg_frontend.R
import de.rehatech2223.lgg_frontend.services.ServiceProvider

class FragmentRoutines : Fragment() {

    private lateinit var flexboxLayout: FlexboxLayout

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
        flexboxLayout = view.findViewById(R.id.flexboxLayout);

        loadRoutineTiles()
    }

    override fun onResume() {
        super.onResume()
        Log.d("handler", "resumed routines")
        flexboxLayout.removeAllViews()
        loadRoutineTiles()
    }
    private fun loadRoutineTiles() {
        val routineList = ServiceProvider.routineService.getRoutineList()
        for (routineId in routineList) {
            val routineDTO = ServiceProvider.routineService.getRoutineInfo(routineId) ?: continue
            flexboxLayout.addView(RoutineTile(requireContext(), null, routineDTO))
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FragmentRoutines().apply {
                arguments = Bundle().apply {}
            }
    }
}