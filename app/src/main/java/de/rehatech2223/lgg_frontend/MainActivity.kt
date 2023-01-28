package de.rehatech2223.lgg_frontend

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SwitchCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import de.rehatech2223.lgg_frontend.databinding.ActivityMainBinding
import de.rehatech2223.lgg_frontend.ui.main.SectionsPagerAdapter

class MainActivity : DynamicThemeActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
        prepareSwitchFunctionality(tabs)
    }

    private fun prepareSwitchFunctionality(tabs: TabLayout){
        val optionsTab: Tab? = tabs.getTabAt(2)
        optionsTab!!.view.visibility = View.GONE
        val switch: SwitchCompat = binding.switch1
        switch.setOnCheckedChangeListener { _, c ->
            if(!c) optionsTab.view.visibility = View.VISIBLE
            else {
                if(optionsTab.isSelected) tabs.selectTab(tabs.getTabAt(0))
                optionsTab.view.visibility = View.GONE
            }
        }
    }
}