package ua.edu.lnu.cannongame

import android.app.DialogFragment
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson


class GameActivity : AppCompatActivity() {

    class Score(
        val nickName: String,
        val totalTime: Long
    )

    val APP_PREFERENCES = "cannon_game_settings"
    val APP_DIFFICULTY = "difficulty"

    lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pref = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
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
        val gameSurface: GameSurface = findViewById(R.id.surface)
        val newFragment: DialogFragment = GameOverDialog.newInstance(
            gameSurface.gameData!!.totalTime
        )
        saveCurrentTime(gameSurface.gameData!!.totalTime)
        saveResultMessage(gameSurface.gameData!!.resultMessage)

        newFragment.show(fragmentManager, "dialog")
//        fragmentManager.beginTransaction()
//            .show(newFragment)
//            .commit()
        Log.i("GameActivity", "showDialog()")
//        val gameSurface: GameSurface = findViewById(R.id.surface)
//        gameSurface.pause()
    }

    fun getDifficulty(): Int{
        return pref.getInt(APP_DIFFICULTY, 1)
    }

    private fun saveCurrentTime(totalTime: Long){
        val editor = pref.edit()
        editor.putLong("currentTime", totalTime)
        editor.apply()
    }

    fun getCurrentTime(): Long{
        return pref.getLong("currentTime", -1)
    }

    private fun saveResultMessage(ResultMessage: String){
        val editor = pref.edit()
        editor.putString("resultMessage", ResultMessage)
        editor.apply()
    }

    fun getResultMessage(): String {
        return pref.getString("resultMessage", "lose")!!
    }

    fun updateScoreboard(nickName: String, totalTime: Long){
        val gson = Gson()
        val list = mutableListOf<Score>()
        for (i in 1 until 11){
            val obj = gson.fromJson(pref.getString("pos_$i", "")!!, Score::class.java)
            if (obj != null){
                list.add(obj)
            }
        }
        list.add(Score(nickName, totalTime))
        list.sortBy{ it.totalTime }
        if (list.size > 10){
            list.removeAt(10)
        }
        val editor = pref.edit()
        for (i in 1 until 11){
            if (i > list.size){
                break
            }
            editor.putString("pos_$i", gson.toJson(list[i-1]))
        }
        editor.apply()
        Log.i("GameActivity", "Scores: $list")
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
