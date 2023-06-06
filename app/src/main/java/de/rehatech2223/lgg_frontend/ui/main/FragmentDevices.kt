package de.rehatech2223.lgg_frontend.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.android.flexbox.FlexboxLayout
import de.rehatech2223.datamodel.DeviceDTO
import de.rehatech2223.lgg_frontend.R
import de.rehatech2223.lgg_frontend.services.ServiceProvider

class FragmentDevices : Fragment() {

    private lateinit var flexboxLayout: FlexboxLayout

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        flexboxLayout = view.findViewById(R.id.flexboxLayout)
        loadDeviceTiles()
    }

    override fun onResume() {
        super.onResume()
        flexboxLayout.removeAllViews()
        loadDeviceTiles()
    }

    private fun loadDeviceTiles() {
        val deviceList = ServiceProvider.devicesService.getDeviceList()
        for (deviceDTO in deviceList) {
            flexboxLayout.addView(DeviceTile(requireContext(), null, deviceDTO))
        }
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