package de.rehatech2223.lgg_frontend.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import de.rehatech2223.lgg_frontend.R
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

class FragmentDevices : Fragment() {

    var currentLinearLayout: LinearLayout? = null;
    var mainLinearLayout: LinearLayout? = null;
    var addedDevicesCount: Int = 0;

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

        mainLinearLayout = view.findViewById<LinearLayout>(R.id.linearLayout);

        addNewDevicesTile("Bild", "Lampe");
        addNewDevicesTile("Bild2", "Lampe2");
        addNewDevicesTile("Bild3", "Lampe3");

        addNewDevicesTile("Bild", "Lampe");
        addNewDevicesTile("Bild2", "Lampe2");
        addNewDevicesTile("Bild3", "Lampe3");

        addNewDevicesTile("Bild", "Lampe");
        addNewDevicesTile("Bild2", "Lampe2");
        addNewDevicesTile("Bild3", "Lampe3");

        addNewDevicesTile("Bild", "Lampe");
        addNewDevicesTile("Bild2", "Lampe2");
        addNewDevicesTile("Bild3", "Lampe3");

        addNewDevicesTile("Bild", "Lampe");
        addNewDevicesTile("Bild2", "Lampe2");
        addNewDevicesTile("Bild3", "Lampe3");

        addNewDevicesTile("Bild", "Lampe");
        addNewDevicesTile("Bild2", "Lampe2");
        addNewDevicesTile("Bild3", "Lampe3");

        addNewDevicesTile("Bild", "Lampe");
        addNewDevicesTile("Bild2", "Lampe2");
        addNewDevicesTile("Bild3", "Lampe3");
    }

    fun addNewDevicesTile(image: String, name: String) {
        if(addedDevicesCount % 3 == 0) {
            currentLinearLayout = layoutInflater.inflate(R.layout.devices_view, null).findViewById(R.id.linearLayout);
            mainLinearLayout?.addView(currentLinearLayout);
        }
        var newDeviceTile = layoutInflater.inflate(R.layout.devices_tile, null);
        newDeviceTile.findViewById<TextView>(R.id.name).text = name;
        newDeviceTile.findViewById<TextView>(R.id.image).text = image;
        currentLinearLayout?.addView(newDeviceTile);
        addedDevicesCount++;
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