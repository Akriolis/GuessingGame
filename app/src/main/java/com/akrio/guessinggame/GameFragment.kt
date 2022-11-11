package com.akrio.guessinggame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.akrio.guessinggame.databinding.FragmentGameBinding
import com.google.android.material.snackbar.Snackbar

open class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentGameBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this)[GameViewModel::class.java]

        updateScreen()

        binding.guessButton.setOnClickListener {
            if (binding.guess.text.trim().isEmpty()) {
                Toast.makeText(activity, getString(R.string.make_guess), Toast.LENGTH_LONG).show()
            } else if (binding.guess.text.toString().uppercase() in viewModel.incorrectGuesses){
                Snackbar.make(binding.guessButton, getString(R.string.already_guessed), Snackbar.LENGTH_LONG)
                    .setAction("UNDO"){
                        binding.guess.text.clear()
                    }
                    .show()
                } else {
            viewModel.makeGuess(binding.guess.text.toString().uppercase())
            binding.guess.text.clear()
            updateScreen()
            if (viewModel.isWon() || viewModel.isLost()) {
                val action = GameFragmentDirections
                    .actionGameFragmentToResultFragment(viewModel.wonLostMessage())
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
        binding.word.text = viewModel.secretWordDisplay
        binding.lives.text = getString(R.string.lives_remain, viewModel.livesLeft)
        binding.incorrectGuesses.text = getString(R.string.incorrect_guesses, viewModel.incorrectGuesses)
    }


}