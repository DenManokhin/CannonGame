package ua.edu.lnu.cannongame

import android.util.Log

class GameData {
    // 10 seconds
    private var timeLeft = 100000L

    private var totalTime = 0L

    private var timeFromLastShot = 3000L

    var shotsCount = 0L
        get() = field
        private set

    fun canMakeNewShot() : Boolean {
        return timeFromLastShot >= 3000
    }

    fun trackNewShot(){
        timeFromLastShot = 0
        shotsCount++

        Log.i("GameData", "New shot made, shoutCount=${shotsCount}, timeLeft=${timeLeft}")
    }

    fun updateTime(waitTime: Long){
        timeLeft -= waitTime
        timeFromLastShot += waitTime
        totalTime += waitTime
    }

    fun isGameRunning() : Boolean {
        return timeLeft > 0
    }

//    fun updateShot(isHit: Boolean){
//        if (isHit){
//
//        }
//    }
}