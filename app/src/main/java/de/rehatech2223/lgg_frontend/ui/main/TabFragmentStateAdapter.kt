package de.rehatech2223.lgg_frontend.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabFragmentStateAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    var disableOptions: Boolean = true

    override fun getItemCount(): Int {
        return if (disableOptions) 2 else 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FragmentDevices.newInstance()
            1 -> FragmentRoutines.newInstance()
            2 -> {
                if (!disableOptions) FragmentOptions.newInstance()
                else FragmentRoutines.newInstance()
            }
            else -> FragmentDevices.newInstance()
        }
    }
}