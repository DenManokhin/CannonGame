package ua.edu.lnu.cannongame

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        findViewById<ImageButton>(R.id.backButton)
            .setOnClickListener {
                Log.i("SettingsActivity", "Back clicked")
                val intent = Intent(this@SettingsActivity, MainActivity::class.java)
                startActivity(intent)
            }
        findViewById<ImageButton>(R.id.infoButton)
            .setOnClickListener {
                Log.i("MainActivity", "Info clicked")
                val intent = Intent(this@SettingsActivity, InfoActivity::class.java)
                startActivity(intent)
            }
    }
}
