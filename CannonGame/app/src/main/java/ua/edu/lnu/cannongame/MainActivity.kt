package ua.edu.lnu.cannongame

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val startBtn = findViewById<Button>(R.id.buttonStart)
        startBtn.setOnClickListener {
            Log.i("MainActivity", "Start clicked")
            val intent = Intent(this@MainActivity, GameActivity::class.java)
            startActivity(intent)
        }
    }
//        val intent = Intent(this@MainActivity, GameActivity::class.java)
//        startActivity(intent)
//    }

    override fun onStart() {
        super.onStart()
        Log.i("MainActivity", "Activity: $this")
        Log.i("MainActivity", "onStart()")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("MainActivity", "onRestart()")
    }

    override fun onResume() {
        super.onResume()
        Log.i("MainActivity", "onResume()")
    }

    override fun onPause() {
        super.onPause()
        Log.i("MainActivity", "onPause()")
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.i("MainActivity", "onTouch()")
        return super.onTouchEvent(event)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        // Sticky immersive mode
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                // Content
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
}
