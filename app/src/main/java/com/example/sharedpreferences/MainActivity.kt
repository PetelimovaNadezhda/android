package com.example.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sharedpreferences.databinding.ActivityMainBinding

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

        sharedPreferences = getPreferences(Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()

        val textForTextView = sharedPreferences.getString("theme", "default") + " theme"
        binding.textView.text = textForTextView
        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            editor.putString("theme", if (isChecked) "red" else "default").apply()
        }
        binding.switch1.isChecked = sharedPreferences.getString("theme", "default") == "red"

//        changeThemeRuntime()
    }

    private fun changeThemeRuntime() {
        binding.switch1.setOnClickListener { _ ->
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