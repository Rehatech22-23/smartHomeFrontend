package de.rehatech2223.lgg_frontend.ui.main

import android.os.Bundle
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
    private val deviceTiles: ArrayList<DeviceTile> = ArrayList()

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

    private fun loadDeviceTiles() {
        val deviceList = ServiceProvider.devicesService.getDeviceList()
        for (deviceId in deviceList) {
            val deviceDTO = ServiceProvider.devicesService.getDeviceInfo(deviceId) ?: continue
            val deviceTile = addDeviceTile(deviceDTO)
            deviceTiles.add(deviceTile)
        }
    }


    private fun addDeviceTile(deviceDTO: DeviceDTO): DeviceTile {
        val deviceTile = DeviceTile(requireContext(), null, deviceDTO)
        flexboxLayout.addView(deviceTile)
        return deviceTile
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