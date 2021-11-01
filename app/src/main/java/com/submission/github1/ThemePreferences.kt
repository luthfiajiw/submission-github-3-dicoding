package com.submission.github1

import android.content.Context

internal class ThemePreferences(context: Context) {
    companion object {
        private const val PREFS_NAME = "theme_pref"
        private const val IS_DARK_MODE = "is_dark_mode"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setTheme(isDarkMode: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(IS_DARK_MODE, isDarkMode)
        editor.apply()
    }

    fun getTheme(): Boolean {
        val isDarkMode = preferences.getBoolean(IS_DARK_MODE, false)

        return isDarkMode
    }
}