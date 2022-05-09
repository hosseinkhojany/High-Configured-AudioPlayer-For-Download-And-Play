package adams.sheek.montazeranapp.ui.sendMessage

import adams.sheek.montazeranapp.databinding.FragmentSendMessageBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController


class SendMessageFragment : Fragment() {

    private lateinit var binding: FragmentSendMessageBinding
    private val viewModel: SendMessageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSendMessageBinding.inflate(inflater)
        binding.send.setOnClickListener {
            if(binding.message.text.toString().isNotEmpty() &&
                binding.message.text.toString().isNotBlank()){
                viewModel.sendMessage(binding.message.text.toString()){
                    binding.message.setText("")
                }
            }
        }
        binding.back.setOnClickListener{
            findNavController().popBackStack()
        }
        return binding.root
    }
}