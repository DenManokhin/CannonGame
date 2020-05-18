package ua.edu.lnu.cannongame

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.game_over_dialog.*


class ScoreActivity : AppCompatActivity() {

    val APP_PREFERENCES = "cannon_game_settings"
    lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pref = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        setContentView(R.layout.scoreboard_activity)
        findViewById<ImageButton>(R.id.backButton)
                .setOnClickListener {
                    Log.i("InfoActivity", "Back clicked")
                    val intent = Intent(this@ScoreActivity, MainActivity::class.java)
                    startActivity(intent)
                }
    }

    override fun onResume() {
        super.onResume()
        val gson = Gson()
        val scoreLayout = findViewById<LinearLayout>(R.id.scoreLayout)
        for (i in 1 until 11){
            val score = gson.fromJson(pref.getString("pos_$i", "")!!, GameActivity.Score::class.java)
            if (score != null) {
                val textView = TextView(this)
                textView.text = "${score.nickName} - ${score.totalTime/1000f} seconds"
                scoreLayout.addView(textView)
            }
        }
    }
}
