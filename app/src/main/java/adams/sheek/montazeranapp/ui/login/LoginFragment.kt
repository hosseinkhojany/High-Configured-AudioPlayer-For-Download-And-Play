package adams.sheek.montazeranapp.ui.login

import adams.sheek.montazeranapp.databinding.FragmentLoginBinding
import adams.sheek.montazeranapp.utils.SharedConfig
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment() : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by activityViewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        initOnClickListeners()
        initViewBehaviors()
        return binding.root
    }

    private fun initViewBehaviors(){
        binding.mobileField.setText(SharedConfig.getMobile())
        binding.mobileField.doOnTextChanged { text, start, before, count -> SharedConfig.setMobile(text.toString()) }
    }

    private fun initOnClickListeners(){
        binding.loginButton.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v){
            binding.loginButton -> {
                viewModel.login {
                    if (binding.mobileField.text.toString().length == 11){
                        if (it){
                            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToListTopicFragment())
                        }
                    }
                }
            }
        }
    }

}