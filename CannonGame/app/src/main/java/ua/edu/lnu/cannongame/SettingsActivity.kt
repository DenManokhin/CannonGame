package ua.edu.lnu.cannongame

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    val APP_PREFERENCES = "cannon_game_settings"
    val APP_DIFFICULTY = "difficulty"
    lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pref = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        setContentView(R.layout.activity_settings)
        findViewById<ImageButton>(R.id.backButton)
            .setOnClickListener {
                Log.i("SettingsActivity", "Back clicked")
                val intent = Intent(this@SettingsActivity, MainActivity::class.java)
                startActivity(intent)
            }
        findViewById<ImageButton>(R.id.infoButton)
            .setOnClickListener {
                Log.i("SettingsActivity", "Info clicked")
                val intent = Intent(this@SettingsActivity, InfoActivity::class.java)
                startActivity(intent)
            }
        findViewById<Button>(R.id.easyLevelButton)
            .setOnClickListener{
                Log.i("SettingsActivity", "Easy clicked")
                val editor = pref.edit()
                editor.putInt(APP_DIFFICULTY, 1)
                editor.apply()
                val intent = Intent(this@SettingsActivity, MainActivity::class.java)
                startActivity(intent)
            }
        findViewById<Button>(R.id.mediumLevelButton)
            .setOnClickListener{
                Log.i("SettingsActivity", "Medium clicked")
                val editor = pref.edit()
                editor.putInt(APP_DIFFICULTY, 2)
                editor.apply()
                val intent = Intent(this@SettingsActivity, MainActivity::class.java)
                startActivity(intent)
            }
        findViewById<Button>(R.id.hardLevelButton)
            .setOnClickListener{
                Log.i("SettingsActivity", "Hard clicked")
                val editor = pref.edit()
                editor.putInt(APP_DIFFICULTY, 3)
                editor.apply()
                val intent = Intent(this@SettingsActivity, MainActivity::class.java)
                startActivity(intent)
            }
    }
}
