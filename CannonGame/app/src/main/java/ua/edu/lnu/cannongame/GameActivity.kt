package ua.edu.lnu.cannongame

import android.app.DialogFragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_activity)
        val surface: GameSurface = findViewById(R.id.surface)
        surface.setOnFocusChangeListener { v, hasFocus ->
            Log.i("GameActivity", "Surface focus changed, ${hasFocus}")
            if (!hasFocus){
                surface.pause()
            }
        }
    }

    override fun onPause() {
        super.onPause()
//        val gameSurface: GameSurface = findViewById(R.id.surface)
//        gameSurface.pause()
//        Log.i("GameActivity", "onPause()")
    }

    fun showDialog() {
//        val fragmentManager = fragmentManager
        val newFragment: DialogFragment = GameOverDialog.newInstance(
            0
        )
        newFragment.show(fragmentManager, "dialog")
//        fragmentManager.beginTransaction()
//            .show(newFragment)
//            .commit()
        Log.i("GameActivity", "showDialog()")
//        val gameSurface: GameSurface = findViewById(R.id.surface)
//        gameSurface.pause()
    }

    fun returnToMenu(view: View?) {
        val intent = Intent(this@GameActivity, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        Log.i("GameActivity", "Activity: $this")
        Log.i("GameActivity", "onStart()")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("GameActivity", "onRestart()")
    }

    override fun onResume() {
        super.onResume()
        Log.i("GameActivity", "onResume()")
    }
}