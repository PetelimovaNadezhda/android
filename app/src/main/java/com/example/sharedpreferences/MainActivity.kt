package com.example.sharedpreferences

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.sharedpreferences.databinding.ActivityMainBinding
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId
import java.time.ZonedDateTime


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

    val handler = Handler(Looper.getMainLooper())
    var isHandle = true
    var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        i = Integer.valueOf(sharedPreferences.getString("iter", "0"))
        setContentView(view)
    }

    override fun onStart() {
        super.onStart()
        setSettingsSharedPreferences()
        changeThemeRuntime()
        goToProfileListener()
        goToSiteListener()
        notificationByTime()
        editTextListener()
        handler.postDelayed(updateLight, 1000)
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(updateLight)
    }

    private fun notificationByTime() {
        val ldt = LocalDateTime.of(2021, Month.JULY, 26, 23, 30)
        val zdt: ZonedDateTime = ldt.atZone(ZoneId.of("America/Los_Angeles"))
        val currentTimeMillis = zdt.toInstant().toEpochMilli()
        val intent = Intent(applicationContext, Receiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(
                this,
                1,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        val am: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        am.set(AlarmManager.RTC_WAKEUP, currentTimeMillis, pendingIntent)
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
            val editor = sharedPreferences.edit()
            if (binding.switch1.isActivated) {
                application.setTheme(R.style.SuperRedTheme)
                binding.textView.text = getString(R.string.red_theme)
                editor.putString("iter", i.toString()).apply()
                recreate()
            } else {
                application.setTheme(R.style.AppTheme)
                binding.textView.text = getString(R.string.default_theme)
                editor.putString("iter", i.toString()).apply()
                recreate()
            }
        }
    }

    private val updateLight: Runnable = object : Runnable {
        override fun run() {
            handler.postDelayed(this, 1000)
//            color = colors[(colors.indexOf(color) + 1) % colors.size]
//            window.decorView.setBackgroundColor(color)
            binding.editText.setText(Integer.toString(i))
            i++
        }
    }

    private fun editTextListener() {
        binding.editText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                i = Integer.valueOf(binding.editText.text.toString())
                if (!isHandle)
                    handler.postDelayed(updateLight, 1000)
            } else {
                handler.removeCallbacks(updateLight)
                isHandle = false
            }
        }
    }
}