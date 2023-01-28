package de.rehatech2223.lgg_frontend.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import de.rehatech2223.lgg_frontend.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [FragmentRoutines.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentRoutines : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var currentLinearLayout: LinearLayout? = null;
    var mainLinearLayout: LinearLayout? = null;
    var addedRoutinesCount: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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

        mainLinearLayout = view.findViewById<LinearLayout>(R.id.linearLayout);

        addNewRoutinesTile("Bild", "Routine");
        addNewRoutinesTile("Bild", "Routine2");
    }

    fun addNewRoutinesTile(image: String, name: String) {
        if(addedRoutinesCount % 3 == 0){
            currentLinearLayout = layoutInflater.inflate(R.layout.routines_view, null).findViewById(R.id.linearLayout);
            mainLinearLayout?.addView(currentLinearLayout);
        }
        var newRoutinesTile = layoutInflater.inflate(R.layout.routines_tile,  null);
        newRoutinesTile.findViewById<TextView>(R.id.name).text = name;
        newRoutinesTile.findViewById<TextView>(R.id.image).text = image;
        currentLinearLayout?.addView(newRoutinesTile);
        addedRoutinesCount++;
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragementRoutines.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentRoutines().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}