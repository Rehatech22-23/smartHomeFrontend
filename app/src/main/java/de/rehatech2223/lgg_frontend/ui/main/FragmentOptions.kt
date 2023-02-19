package de.rehatech2223.lgg_frontend.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import de.rehatech2223.lgg_frontend.R

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentOptions.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentOptions : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val optionContainerLayout = view.findViewById<LinearLayout>(R.id.optionContainerLayout)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FragmentOptions().apply {
                arguments = Bundle().apply {
                    // Deleted something
                }
            }
    }
}