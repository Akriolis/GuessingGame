package com.akrio.guessinggame

import android.os.Bundle
import android.view.Display
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.akrio.guessinggame.databinding.FragmentGameBinding

class GameFragment : Fragment() {
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private val words = listOf("Android","Activity","Fragment")
    private val secretWord = words.random().uppercase()
    private var secretWordDisplay = ""
    private var correctGuesses = ""
    private var incorrectGuesses = ""

    private var livesLeft = 8

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentGameBinding.inflate(inflater, container, false)
        val view = binding.root

        secretWordDisplay = deriveSecretWordDisplay()
        updateScreen()

        binding.guessButton.setOnClickListener {
            if (binding.guess.text.trim().isEmpty()) {
                Toast.makeText(activity, getString(R.string.make_guess), Toast.LENGTH_LONG).show()
            } else {
                makeGuess(binding.guess.text.toString().uppercase())
                binding.guess.text = null
                updateScreen()
                if (isWon() || isLost()) {
                    val action = GameFragmentDirections
                        .actionGameFragmentToResultFragment(wonLostMessage())
                    view.findNavController().navigate(action)
                }
            }
        }
        return view

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateScreen(){
        binding.word.text = secretWordDisplay
        binding.lives.text = getString(R.string.lives_remain, livesLeft)
        binding.incorrectGuesses.text = getString(R.string.incorrect_guesses, incorrectGuesses)
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

    private fun makeGuess(guess: String){
        // works fine with (guess in secretWord) too
        if(secretWord.contains(guess)){
            correctGuesses += guess
            secretWordDisplay = deriveSecretWordDisplay()
        } else {
            incorrectGuesses += "$guess "
            livesLeft--
        }
    }

    private fun isWon() = secretWord.equals(secretWordDisplay, true)
    private fun isLost() = livesLeft <= 0

    private fun wonLostMessage(): String{
        var message = ""
        if(isWon()) message = getString(R.string.you_won)
        else if (isLost()) message = getString(R.string.you_lost)
        message += getString(R.string.word_was, secretWord)
        return message
    }


}