package ua.edu.lnu.cannongame

//import android.R
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.app.ProgressDialog.show
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import ua.edu.lnu.cannongame.R.id.game_over_cancel
import ua.edu.lnu.cannongame.R.id.game_over_save


class GameOverDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v: View = inflater.inflate(R.layout.game_over_dialog, container, false)
        val tv: View = v.findViewById(R.id.game_over_text)
        (tv as TextView).text = "Game is over"

        val cancelButton: Button = v.findViewById(game_over_cancel) as Button
        cancelButton.setOnClickListener { // When button is clicked, call up to owning activity.
            Log.i("GameOverDialog", "User cancelled the dialog")
            Log.i("GameOverDialog", "$activity")
            Thread(Runnable {
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
                (activity as GameActivity).finish()
            }).start()

        }
        val saveButton: Button = v.findViewById(game_over_save) as Button
        saveButton.setOnClickListener { // When button is clicked, call up to owning activity.
            Log.i("GameOverDialog", "User clicked save button")
            Log.i("GameOverDialog", "$activity")
            Thread(Runnable {
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
                (activity as GameActivity).finish()
            }).start()
        }

        return v
    }

    companion object {
        /**
         * Create a new instance of MyDialogFragment, providing "num"
         * as an argument.
         */
        fun newInstance(num: Int): GameOverDialog {
            val f = GameOverDialog()

            // Supply num input as an argument.
            val args = Bundle()
            args.putInt("num", num)
            f.arguments = args
            return f
        }
    }
}