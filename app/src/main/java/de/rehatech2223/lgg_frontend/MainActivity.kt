package de.rehatech2223.lgg_frontend

import android.os.Bundle
import androidx.preference.PreferenceManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import de.rehatech2223.lgg_frontend.databinding.ActivityMainBinding
import de.rehatech2223.lgg_frontend.ui.main.TabFragmentStateAdapter
import de.rehatech2223.lgg_frontend.ui.options.DEVICE_FRAGMENT_INDEX_KEY

class MainActivity : DynamicThemeActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var tabFragmentStateAdapter: TabFragmentStateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tabFragmentStateAdapter = TabFragmentStateAdapter(supportFragmentManager, lifecycle)
        val viewPager: ViewPager2 = binding.viewPagerTwo
        viewPager.adapter = tabFragmentStateAdapter
        val tabs: TabLayout = binding.tabs
        val tabTitles: List<String> = initTabTitles()
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
        prepareSwitchFunctionality(tabs)
    }

    private fun initTabTitles(): List<String> {
        val deviceIndex = PreferenceManager.getDefaultSharedPreferences(this)
            .getInt(DEVICE_FRAGMENT_INDEX_KEY, 0)
        return listOf(
            if (deviceIndex == 0) "Geräte" else "Abläufe",
            if (deviceIndex == 1) "Geräte" else "Abläufe",
            "Optionen"
        )
    }

    private fun prepareSwitchFunctionality(tabs: TabLayout) {
        binding.switch1.setOnCheckedChangeListener { _, c ->
            if (!c) {
                tabFragmentStateAdapter.disableOptions = false
                tabFragmentStateAdapter.notifyItemChanged(3)
            } else {
                if (tabs.selectedTabPosition > 1) tabs.selectTab(tabs.getTabAt(0))
                tabFragmentStateAdapter.disableOptions = true
//                tabFragmentStateAdapter.notifyItemChanged(3)
//                tabFragmentStateAdapter.notifyItemRemoved(3)
                //todo later
                tabFragmentStateAdapter.notifyDataSetChanged()
            }
        }
    }
}