package com.akrio.guessinggame

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {

    val TAG = GameViewModel::class.simpleName

    private val words = listOf("Android","Activity","Fragment")
    private val secretWord = words.random().uppercase()
    private var correctGuesses = ""

    private val _secretWordDisplay = MutableLiveData<String>()
    val secretWordDisplay: LiveData<String>
        get() = _secretWordDisplay

    private val _incorrectGuesses = MutableLiveData<String>("")
    val incorrectGuesses: LiveData<String>
        get() = _incorrectGuesses

    private val _livesLeft = MutableLiveData<Int>(8)
    val livesLeft: LiveData<Int>
        get() = _livesLeft

    private val _gameOver = MutableLiveData<Boolean>(false)
    val gameOver: LiveData<Boolean>
        get() = _gameOver

    init {
        _secretWordDisplay.value = deriveSecretWordDisplay()
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
            _secretWordDisplay.value = deriveSecretWordDisplay()
        } else {
            _incorrectGuesses.value += "$guess "
            _livesLeft.value = livesLeft.value?.minus(1)
        }
        if (isWon() || isLost()) _gameOver.value = true
    }

    private fun isWon() = secretWord.equals(secretWordDisplay.value, true)
    private fun isLost() = (livesLeft.value ?: 0) <= 0

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