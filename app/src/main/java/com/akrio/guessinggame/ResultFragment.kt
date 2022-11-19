package com.akrio.guessinggame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController

class ResultFragment : Fragment() {

    lateinit var viewModel: ResultViewModel
    lateinit var viewModelFactory: ResultViewModelFactory
//
//    private var _binding: FragmentResultBinding? = null
//    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        _binding = FragmentResultBinding.inflate(inflater,container,false)
        val result = ResultFragmentArgs.fromBundle(requireArguments()).result

        viewModelFactory = ResultViewModelFactory(result)
        viewModel = ViewModelProvider(this, viewModelFactory)[ResultViewModel::class.java]

            return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme{
                    Surface {
                        view?.let { ResultFragmentContent(it) }
                    }
                }
            }
        }
//        val view = binding.root
//

//
//        binding.resultViewModel = viewModel
//
//        binding.newGameButton.setOnClickListener {
//            view.findNavController()
//                .navigate(R.id.action_resultFragment_to_gameFragment)
//        }
//        return view
    }

    @Composable
    fun NewGameButton(clicked: () -> Unit){
        Button (onClick = clicked){
            Text(text = getString(R.string.start_new_game))
        }
    }

    @Composable
    fun ResultText(result: String){
        Text(text = result,
            fontSize = 28.sp,
            textAlign = TextAlign.Center
        )
    }

    @Composable
    fun ResultFragmentContent(view: View){
        Column(modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
            ResultText(viewModel.result)
            NewGameButton {
                view.findNavController()
                    .navigate(R.id.action_resultFragment_to_gameFragment)
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewResultFragment(){
        MaterialTheme{
            Surface {
                view?.let{
                    ResultFragmentContent(view = it)
                }
            }
        }
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}