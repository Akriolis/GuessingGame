package com.akrio.guessinggame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
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

//        viewModel.screenState.observe(viewLifecycleOwner){
//            binding.lives.text = getString(R.string.lives_remain, it.livesLeft)
//            binding.word.text = it.secretWordDisplay
//            binding.incorrectGuesses.text = getString(R.string.incorrect_guesses, it.incorrectGuesses)
//        }

        viewModel.livesLeft.observe(viewLifecycleOwner, Observer { newValue ->
            binding.lives.text = getString(R.string.lives_remain, newValue)
        })

        viewModel.secretWordDisplay.observe(viewLifecycleOwner, Observer { newValue ->
            binding.word.text = newValue
        })

        viewModel.incorrectGuesses.observe(viewLifecycleOwner) {
            binding.incorrectGuesses.text = getString(R.string.incorrect_guesses, it)
        }

        viewModel.gameOver.observe(viewLifecycleOwner) {
            if (it) {
                val action = GameFragmentDirections
                    .actionGameFragmentToResultFragment(viewModel.wonLostMessage())
                view.findNavController().navigate(action)
            }
        }
        binding.guessButton.setOnClickListener {
            if (binding.guess.text.trim().isEmpty()) {
                Toast.makeText(activity, getString(R.string.make_guess), Toast.LENGTH_LONG).show()
            } else if (viewModel.incorrectGuesses.value
                    .toString()
                    .contains(binding.guess.text.toString()
                    .uppercase())){
                Snackbar.make(binding.guessButton,
                    getString(R.string.already_guessed),
                    Snackbar.LENGTH_LONG)
                    .setAction("UNDO"){
                        binding.guess.text.clear()
                    }
                    .show()
                } else {
            viewModel.makeGuess(binding.guess.text.toString().uppercase())
            binding.guess.text.clear()

        }
    }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}