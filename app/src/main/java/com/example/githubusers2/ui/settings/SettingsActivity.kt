package com.example.githubusers2.ui.settings

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.example.githubusers2.R
import com.example.githubusers2.alarm.AlarmReceiver
import com.example.githubusers2.databinding.SettingsActivityBinding
import com.example.githubusers2.utility.UserPreference

class SettingsActivity : AppCompatActivity() {
    private var binding: SettingsActivityBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.title = getString(R.string.settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding?.apply {
            tvLanguage.setOnClickListener {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }

            if (UserPreference(this@SettingsActivity).getPref(UserPreference.NOTIF) != null) {
                switchNotif.isChecked = true
            }
            switchNotif.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    //set repeating alarm
                    UserPreference(this@SettingsActivity).setPref(
                        UserPreference.NOTIF,
                        UserPreference.ACTIVE
                    )
                    AlarmReceiver().setRepeatingAlarm(
                        this@SettingsActivity,
                        getString(R.string.notif_message)
                    )
                } else {
                    UserPreference(this@SettingsActivity).clearPref(UserPreference.NOTIF)
                    //cancel repeating alarm
                    AlarmReceiver().cancelAlarm(this@SettingsActivity)
                }
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}