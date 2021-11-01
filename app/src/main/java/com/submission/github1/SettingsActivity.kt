package com.submission.github1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import com.submission.github1.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private var _settingActivityBinding: ActivitySettingsBinding? = null
    private val binding get() = _settingActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _settingActivityBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val themePreferences = ThemePreferences(this)
        val isDarkMode = themePreferences.getTheme()
        if (isDarkMode) {
            setTheme(isDarkMode)
        }

        supportActionBar?.title = getString(R.string.settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding?.switchTheme?.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            setTheme(isChecked)
        }
    }

    private fun setTheme(isDarkMode: Boolean) {
        val themePreferences = ThemePreferences(this)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            binding?.switchTheme?.isChecked = true
            themePreferences.setTheme(true)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            binding?.switchTheme?.isChecked = false
            themePreferences.setTheme(false)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _settingActivityBinding = null
    }
}