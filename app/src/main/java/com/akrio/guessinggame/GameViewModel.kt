package com.akrio.guessinggame


import android.util.Log

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {

    val TAG = GameViewModel::class.simpleName

    private val words = listOf("Android","Activity","Fragment")
    private val secretWord = words.random().uppercase()

    private var correctGuesses = ""

    val secretWordDisplay = MutableLiveData<String>()
    val incorrectGuesses = MutableLiveData<String>("")
    val livesLeft = MutableLiveData<Int>(8)

    init {
        secretWordDisplay.value = deriveSecretWordDisplay()
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
            secretWordDisplay.value = deriveSecretWordDisplay()
        } else {
            incorrectGuesses.value += "$guess "
            livesLeft.value = livesLeft.value?.minus(1)
        }
    }

    fun isWon() = secretWord.equals(secretWordDisplay.value, true)
    fun isLost() = (livesLeft.value ?: 0) <= 0

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