package com.pareandroid.githubapp.ui.fragment

import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.pareandroid.githubapp.R
import com.pareandroid.githubapp.services.AlarmReceiver

class PreferenceFragment : PreferenceFragmentCompat() {

    private lateinit var switchPreference : SwitchPreference
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)

        val alarmReceiver = AlarmReceiver()

        switchPreference = findPreference(resources.getString(R.string.notification))!!

        switchPreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
            if (switchPreference.isChecked){
                activity?.let { alarmReceiver.cancelAlarm(it) }
                Toast.makeText(activity, getString(R.string.set_alarm_off), Toast.LENGTH_SHORT).show()
                switchPreference.isChecked = false
            }else{
                activity?.let { alarmReceiver.setRepeatAlarm(it) }
                Toast.makeText(activity, getString(R.string.alarm_set), Toast.LENGTH_SHORT).show()
                switchPreference.isChecked = true
            }
            false
        }
    }

}