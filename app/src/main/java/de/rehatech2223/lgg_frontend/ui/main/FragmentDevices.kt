package de.rehatech2223.lgg_frontend.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.rehatech2223.lgg_frontend.R

class FragmentDevices : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // Deleted something
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_devices, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FragmentDevices().apply {
                arguments = Bundle().apply {
                    // Deleted something
                }
            }
    }
}