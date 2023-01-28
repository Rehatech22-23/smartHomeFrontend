package de.rehatech2223.lgg_frontend.ui.main

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.isVisible
import de.rehatech2223.lgg_frontend.R

class OptionSettings(context: Context, attrs: AttributeSet? = null): LinearLayout(context, attrs) {

    private var openButton: Button
    private var settingContainer: LinearLayout

    init {
        LayoutInflater.from(context).inflate(R.layout.option_settings, this, true)
        orientation = VERTICAL

        openButton = findViewById(R.id.openButton)
        settingContainer = findViewById(R.id.setting_container)

        initButton()
    }

    private fun initButton(){
        openButton.setOnClickListener{
            if(settingContainer.isVisible){
                settingContainer.visibility = GONE
                openButton.text = context.getString(R.string.settings_unclicked)
            }
            else {
                settingContainer.visibility = VISIBLE
                openButton.text = context.getString(R.string.settings_clicked)
            }
        }
    }

}