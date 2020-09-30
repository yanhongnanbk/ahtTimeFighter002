/**
 * To summarize, the animation will:
• Scale the animated View to twice its size.
• Shrink it back to its original size.
• Do this over the space of two seconds.
• Use a bouncing interpolator for the rate the animation moves.
 * */

package com.yan.ahttimefighter002

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    /**Init var*/
    // TAG
    private val TAG = MainActivity::class.java.simpleName

    // View
    private lateinit var gameScoreTV: TextView
    private lateinit var timeLeftTV: TextView
    private lateinit var tapMeBtn: Button

    // Others
    private var score = 0
    private var gameStarted = false
    private lateinit var countDownTimer: CountDownTimer
    private var initialCountDown: Long = 10000
    private var countDownInterval: Long = 1000
    private var timeLeft = 10

    /**On Create*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // On Create Debug
        Log.d(TAG, "onCreate called. Score is: $score")

        // connect views to variables
        // 1
        gameScoreTV = findViewById(R.id.tv_game_score)
        timeLeftTV = findViewById(R.id.tv_time_left)
        tapMeBtn = findViewById(R.id.btn_tap_me)
        // 2
        tapMeBtn.setOnClickListener { v ->
            val bounceAnimation = AnimationUtils.loadAnimation(
                this,
                R.anim.bounce
            )
            v.startAnimation(bounceAnimation)
            if (!gameStarted) {
                startGame()
            }
            incrementScore()
        }
        // pass the values back after rotation
        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeft = savedInstanceState.getInt(TIME_LEFT_KEY)
            restoreGame()
        } else {
            resetGame()
        }

    }

    /**increment*/

    private fun incrementScore() {
        // increment score logic
        score++
        val newScore = getString(R.string.your_score, score)
        gameScoreTV.text = newScore
    }

    /**reset*/
    private fun resetGame() {
        // reset game logic
        // 1
        score = 0
        val initialScore = getString(R.string.your_score, score)
        gameScoreTV.text = initialScore
        val initialTimeLeft = getString(R.string.time_left, 10)
        timeLeftTV.text = initialTimeLeft
        // 2
        countDownTimer = object : CountDownTimer(
            initialCountDown,
            countDownInterval
        ) {
            // 3 Inside the CountDownTimer you have two overridden methods: onTick and onFinish. onTick is called at every interval you passed into the timer; in this case, once a second. onTick also passes in a parameter called millisUntilFinished, the amount of time left before the countdown is finished.
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt() / 1000
                val timeLeftString = getString(R.string.time_left, timeLeft)
                timeLeftTV.text = timeLeftString
            }

            override fun onFinish() {
                // To Be Implemented Later
                endGame()
            }
        }
        // 4 You inform the gameStarted property that the game has not started by setting it to false.
        gameStarted = false
    }

    /**start*/
    private fun startGame() {
        // start game logic
        countDownTimer.start()
        gameStarted = true
    }

    /**end game*/
    private fun endGame() {
        // end game logic
        Toast.makeText(this, getString(R.string.game_over_message, score), Toast.LENGTH_LONG).show()
        resetGame()
    }

    /**restore*/
    private fun restoreGame() {
        // restore time and score which were stored before onStop()
        val restoredScore = getString(R.string.your_score, score)
        gameScoreTV.text = restoredScore
        val restoredTime = getString(R.string.time_left, timeLeft)
        timeLeftTV.text = restoredTime
        // run countdowntimer
        countDownTimer = object : CountDownTimer(
            (timeLeft * 1000).toLong(), countDownInterval
        ) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt() / 1000
                val timeLeftString = getString(
                    R.string.time_left,
                    timeLeft
                )
                timeLeftTV.text = timeLeftString
            }

            override fun onFinish() {
                endGame()
            }
        }
        countDownTimer.start()
        gameStarted = true
    }

    /**Companion Object && Storing value*/
    // 1
    companion object {
        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }

    /**On Save*/
    // 2
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCORE_KEY, score)
        outState.putInt(TIME_LEFT_KEY, timeLeft)
        countDownTimer.cancel()
        Log.d(TAG, "onSaveInstanceState: Saving Score: $score & TimeLeft: $timeLeft")
    }

    // 3
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called.")
    }

    /**Menu*/
    // Create menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    // Click Event
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.about_item) {
            showInfo()
        }
        return true
    }

    // show Info
    private fun showInfo() {
        val dialogTitle = getString(R.string.about_title, BuildConfig.VERSION_NAME)
        val dialogMessage = getString(R.string.about_message)
        val builder = AlertDialog.Builder(this)
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.create().show()
    }
}
