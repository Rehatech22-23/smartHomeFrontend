package de.rehatech2223.lgg_frontend

import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import de.rehatech2223.lgg_frontend.databinding.ActivityMainBinding
import de.rehatech2223.lgg_frontend.ui.main.TabFragmentStateAdapter

val tabTitles = arrayOf(
    "Geräte",
    "Abläufe",
    "Optionen"
)

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
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
        prepareSwitchFunctionality(tabs)
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