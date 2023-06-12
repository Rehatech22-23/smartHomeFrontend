package de.rehatech2223.lgg_frontend.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.preference.PreferenceManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import de.rehatech2223.lgg_frontend.SmarthomeApplication
import de.rehatech2223.lgg_frontend.ui.options.DEVICE_FRAGMENT_INDEX_KEY
import kotlin.math.abs

/**
 * Adapter for managing fragments in the top-bar and the advanced mode.
 *
 * @author Fynn Debus
 */
class TabFragmentStateAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    var disableOptions: Boolean = true
    private val deviceFragmentPosition: Int = PreferenceManager.getDefaultSharedPreferences(SmarthomeApplication.getContext())
        .getInt(DEVICE_FRAGMENT_INDEX_KEY, 0)
    private val routineFragmentPosition: Int = abs(deviceFragmentPosition-1)

    override fun getItemCount(): Int {
        return if (disableOptions) 2 else 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            deviceFragmentPosition -> FragmentDevices.newInstance()
            routineFragmentPosition -> FragmentRoutines.newInstance()
            2 -> {
                if (!disableOptions) FragmentOptions.newInstance()
                else FragmentRoutines.newInstance()
            }
            else -> FragmentDevices.newInstance()
        }
    }

    companion object{
    }
}