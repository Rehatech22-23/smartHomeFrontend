package de.rehatech2223.lgg_frontend

import android.os.Bundle
import androidx.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity

abstract class DynamicThemeActivity: AppCompatActivity() {

    private var currentTheme: Int = ThemeEnum.DEFAULT.theme

    override fun onCreate(savedInstanceState: Bundle?) {
        currentTheme = PreferenceManager.getDefaultSharedPreferences(this).getInt(KEY_THEME, ThemeEnum.DEFAULT.theme)
        super.onCreate(savedInstanceState)
    }
     protected fun setTheme() { setTheme(currentTheme) }

    fun changeTheme(themeEnum: ThemeEnum){
        if(currentTheme == themeEnum.theme) return
        currentTheme = themeEnum.theme
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(KEY_THEME, currentTheme).apply()
        setTheme(currentTheme)
        recreate()
    }

    fun getCurrentTheme(): Int{
        return currentTheme
    }

    companion object {
        private const val KEY_THEME = "Theme"
    }
}