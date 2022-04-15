package adams.sheek.montazeranapp.ui.splash

import adams.sheek.montazeranapp.databinding.FragmentLoginBinding
import adams.sheek.montazeranapp.databinding.FragmentSplashBinding
import adams.sheek.montazeranapp.utils.SharedConfig
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment() : Fragment(){

    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater)
        CoroutineScope(Dispatchers.Main).launch {
            delay(2600)
            if (SharedConfig.getUserToken().isNotEmpty()){
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToListTopicFragment())
            }else{
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
            }
        }
        return binding.root
    }


}