package com.example.sharedpreferences

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sharedpreferences.databinding.ActivityMainBinding
import java.util.*

lateinit var sharedPreferences: SharedPreferences
private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()
        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        if (sharedPreferences.getString("theme", "default") == "red")
            theme.applyStyle(R.style.SuperRedTheme, true)
        return theme
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSettingsSharedPreferences()
//        changeThemeRuntime()
        goToProfileListener()
        goToSiteListener()
        notificationByTime()
    }

    private fun notificationByTime() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, 2021)
        calendar.set(Calendar.MONTH, Calendar.APRIL)
        calendar.set(Calendar.DAY_OF_MONTH, 21)

        calendar.set(Calendar.HOUR_OF_DAY, 13)
        calendar.set(Calendar.MINUTE, 44)
        calendar.set(Calendar.SECOND, 0)
        val chosenTime = calendar.timeInMillis
        val intent = Intent(applicationContext, Receiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val am: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        am.set(AlarmManager.RTC_WAKEUP, chosenTime, pendingIntent)
    }

    private fun setSettingsSharedPreferences() {
        sharedPreferences = getPreferences(MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val textForTextView = sharedPreferences.getString("theme", "default") + " theme"
        binding.buttonToSite.display
        binding.textView.text = textForTextView
        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            editor.putString("theme", if (isChecked) "red" else "default").apply()
        }
        binding.switch1.isChecked = sharedPreferences.getString("theme", "default") == "red"
    }

    private fun goToProfileListener() {
        binding.buttonToProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("topics", binding.editText.text.toString())
            startActivity(intent)
        }
    }

    private fun goToSiteListener() {
        binding.buttonToSite.setOnClickListener {
            val website = Uri.parse("https://hyperskill.org")
            val intent = Intent(Intent.ACTION_VIEW, website)
            startActivity(intent)
        }
    }

    private fun changeThemeRuntime() {
        binding.switch1.setOnClickListener {
            if (binding.switch1.isActivated) {
                application.setTheme(R.style.SuperRedTheme)
                binding.textView.text = getString(R.string.red_theme)
                recreate()
            } else {
                application.setTheme(R.style.AppTheme)
                binding.textView.text = getString(R.string.default_theme)
                recreate()
            }
        }
    }
}