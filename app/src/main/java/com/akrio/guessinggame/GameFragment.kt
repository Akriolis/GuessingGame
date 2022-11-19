package com.akrio.guessinggame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController

open class GameFragment : Fragment() {

//    private var _binding: FragmentGameBinding? = null
//    private val binding get() = _binding!!

    lateinit var viewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this)[GameViewModel::class.java]

        viewModel.gameOver.observe(viewLifecycleOwner) {
            if (it) {
                val action = GameFragmentDirections
                    .actionGameFragmentToResultFragment(viewModel.wonLostMessage())
                view?.findNavController()?.navigate(action)
            }
        }
//        _binding = FragmentGameBinding.inflate(inflater, container, false).apply {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    Surface {
                        GameFragmentContent(viewModel)
                    }

                }
            }
        }
//        val view = binding.root

    }

//        viewModel.screenState.observe(viewLifecycleOwner){
//            binding.lives.text = getString(R.string.lives_remain, it.livesLeft)
//            binding.word.text = it.secretWordDisplay
//            binding.incorrectGuesses.text = getString(R.string.incorrect_guesses, it.incorrectGuesses)
//        }

//        viewModel.livesLeft.observe(viewLifecycleOwner, Observer { newValue ->
//            binding.lives.text = getString(R.string.lives_remain, newValue)
//        })
//
//        viewModel.secretWordDisplay.observe(viewLifecycleOwner, Observer { newValue ->
//            binding.word.text = newValue
//        })
//
//        viewModel.incorrectGuesses.observe(viewLifecycleOwner) {
//            binding.incorrectGuesses.text = getString(R.string.incorrect_guesses, it)
//        }
//
//
//        binding.guessButton.setOnClickListener {
//            if (binding.guess.text.trim().isEmpty()) {
//                Toast.makeText(activity, getString(R.string.make_guess), Toast.LENGTH_LONG).show()
//            } else if (viewModel.incorrectGuesses.value
//                    .toString()
//                    .contains(binding.guess.text.toString()
//                    .uppercase())){
//                Snackbar.make(binding.guessButton,
//                    getString(R.string.already_guessed),
//                    Snackbar.LENGTH_LONG)
//                    .setAction("UNDO"){
//                        binding.guess.text.clear()
//                    }
//                    .show()
//                } else {
//            viewModel.makeGuess(binding.guess.text.toString().uppercase())
//            binding.guess.text.clear()
//
//        }
//    }
//        binding.finishButton.setOnClickListener {
//            viewModel.finishGame()
//        }
//
//
//        return view
    }

    @Composable
    fun FinishGameButton(clicked: () -> Unit){
        Button(onClick = clicked) {
            Text(text = stringResource(R.string.finish_the_game))
        }
    }

    @Composable
    fun EnterGuess(guess: String, changed: (String) -> Unit){
        TextField(value = guess,
            onValueChange = changed,
        label = { Text(text = stringResource(R.string.guess_a_letter))})
    }

    @Composable
    fun GuessButton(clicked: () -> Unit){
        Button(onClick = clicked){
            Text(text = stringResource(R.string.guess))
        }
    }
    
    @Composable
    fun IncorrectGuessesText(viewModel: GameViewModel){
        val incorrectGuesses = viewModel.incorrectGuesses.observeAsState()
        incorrectGuesses.value?.let{
            Text(stringResource(id = R.string.incorrect_guesses, it) )
        }
    }
    
    @Composable
    fun LivesLeftText(viewModel: GameViewModel){
        val livesLeft = viewModel.livesLeft.observeAsState()
        livesLeft.value?.let { 
            Text(text = stringResource(id = R.string.lives_remain, it))
        }
    }
    
    @Composable
    fun SecretWordDisplay(viewModel: GameViewModel){
        val display = viewModel.secretWordDisplay.observeAsState()
        display.value?.let { 
            Text(text = it,
            letterSpacing = 0.1 .em,
            fontSize = 36. sp)
        }
    }

    @Composable
    fun GameFragmentContent(viewModel: GameViewModel){
        val guess = remember {
            mutableStateOf("")
        }
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center) {
                SecretWordDisplay(viewModel = viewModel)
            }
            LivesLeftText(viewModel = viewModel)
            IncorrectGuessesText(viewModel = viewModel)
            EnterGuess(guess = guess.value, changed = {guess.value = it})
            Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
                GuessButton {
                    viewModel.makeGuess(guess.value.uppercase())
                    guess.value = ""
                }
                FinishGameButton {
                    viewModel.finishGame()
                }
            }

        }
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}