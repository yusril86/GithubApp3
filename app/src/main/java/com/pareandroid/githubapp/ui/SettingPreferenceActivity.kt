package com.pareandroid.githubapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pareandroid.githubapp.R
import com.pareandroid.githubapp.ui.fragment.PreferenceFragment

class SettingPreferenceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_preference)

        supportActionBar?.title=("Alarm")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.beginTransaction()
            .add(R.id.settingPreference,PreferenceFragment())
            .commit()
    }

    override fun onNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }
}