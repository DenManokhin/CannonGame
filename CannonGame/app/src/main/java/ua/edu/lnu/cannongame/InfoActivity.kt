package ua.edu.lnu.cannongame

import android.app.DialogFragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity


class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        findViewById<ImageButton>(R.id.backButton)
            .setOnClickListener {
                Log.i("InfoActivity", "Back clicked")
                val intent = Intent(this@InfoActivity, MainActivity::class.java)
                startActivity(intent)
            }
        findViewById<ImageButton>(R.id.settingsButton)
            .setOnClickListener {
                Log.i("MainActivity", "Settings clicked")
                val intent = Intent(this@InfoActivity, SettingsActivity::class.java)
                startActivity(intent)
            }
    }
}
