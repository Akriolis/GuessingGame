package com.akrio.guessinggame

import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {

    val TAG = GameViewModel::class.simpleName

    private val words = listOf("Android","Activity","Fragment")
    private val secretWord = words.random().uppercase()

    private var correctGuesses = ""

    var secretWordDisplay = ""
    private set
    var incorrectGuesses = ""
    private set
    var livesLeft = 8
    private set

    init {
        secretWordDisplay = deriveSecretWordDisplay()
    }

    private fun deriveSecretWordDisplay(): String{
        var display = ""
        secretWord.forEach {
            display += checkLetter(it.toString())
        }
        return display
    }

    private fun checkLetter(str: String) = when(correctGuesses.contains(str)){
        true -> str
        false -> "_"
    }

    fun makeGuess(guess: String){
        // works fine with (guess in secretWord) too
        if(secretWord.contains(guess)){
            correctGuesses += guess
            secretWordDisplay = deriveSecretWordDisplay()
        } else {
            incorrectGuesses += "$guess "
            livesLeft--
        }
    }

    fun isWon() = secretWord.equals(secretWordDisplay, true)
    fun isLost() = livesLeft <= 0

    fun wonLostMessage(): String{
        var message = ""
        if(isWon()) message = "You won!" + "\n"
        else if (isLost()) message = "You lost!" + "\n"
        message += "The word was $secretWord"
        return message
    }

    override fun onCleared() {
        Log.wtf(TAG, "View model cleared")
        super.onCleared()
    }

}