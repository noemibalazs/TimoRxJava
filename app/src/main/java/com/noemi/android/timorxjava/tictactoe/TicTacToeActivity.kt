package com.noemi.android.timorxjava.tictactoe

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.noemi.android.timorxjava.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_tic_tac_toe.*

@AndroidEntryPoint
class TicTacToeActivity : AppCompatActivity() {

    private var activePlayer = 0
    private var gameState = intArrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2)
    private var counter = 0
    private var flag = false

    private val winPositions = listOf(
        listOf(0, 1, 2),
        listOf(3, 4, 5),
        listOf(6, 7, 8),
        listOf(0, 3, 6),
        listOf(1, 4, 7),
        listOf(2, 5, 8),
        listOf(0, 4, 8),
        listOf(2, 4, 6)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tic_tac_toe)

        gameReset()
    }

    fun clickOnView(view: View) {
        (view as AppCompatImageView).apply {
            activePlayer = if (activePlayer == 0) {
                setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.x))
                1
            } else {
                setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.o))
                0
            }

            val tag = this.tag.toString().toInt()
            gameState[tag] = activePlayer
        }
        counter++
        status.text = setStatusText(activePlayer)
        resetIfOver()
    }

    private fun setStatusText(activePlayer: Int): String {
        val player = if (activePlayer == 0) "X" else "O"
        return String.format(getString(R.string.player_turn_to_play), player)
    }

    private fun resetIfOver() {

        if (counter == 9 && !flag) {
            Toast.makeText(this, "Match draw", Toast.LENGTH_LONG).show()
            resetGame()
        }

        if (counter <= 9) {
            val winner = checkForWinner()
            if (flag) {
                Toast.makeText(this, "The winner is the $winner player", Toast.LENGTH_LONG).show()
                resetGame()
            }
        }
    }

    private fun resetGame() {
        Handler(mainLooper).postDelayed(
            {
                gameReset()
            },
            1200L
        )
    }

    private fun gameReset() {
        activePlayer = 0
        counter = 0
        flag = false

        for (i in gameState.indices) {
            gameState[i] = 2
        }

        resetImageDrawables()
        status.text = setStatusText(0)
    }

    private fun resetImageDrawables() {
        val views = listOf<AppCompatImageView>(view0, view1, view2, view3, view4, view5, view6, view7, view8)
        views.forEach {
            it.setImageDrawable(null)
        }
    }

    private fun checkForWinner(): String {
        var winner = ""
        winPositions.forEach {
            if (gameState[it[0]] == gameState[it[1]] && gameState[it[1]] == gameState[it[2]] && gameState[it[1]] != 2) {
                winner = if (gameState[it[0]] == 0) "second" else "first"
                flag = true
            }
        }
        return winner
    }
}