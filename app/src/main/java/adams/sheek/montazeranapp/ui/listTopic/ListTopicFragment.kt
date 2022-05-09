package adams.sheek.montazeranapp.ui.listTopic

import adams.sheek.montazeranapp.R
import adams.sheek.montazeranapp.data.datasource.ram.InRamDs
import adams.sheek.montazeranapp.databinding.FragmentListTopicsBinding
import adams.sheek.montazeranapp.ui.adapter.ListTopicAdapter
import adams.sheek.montazeranapp.ui.login.LoginViewModel
import adams.sheek.montazeranapp.ui.main.MainActivity
import adams.sheek.montazeranapp.ui.shared.ClassesViewModel
import adams.sheek.montazeranapp.utils.Helper
import adams.sheek.montazeranapp.utils.SharedConfig
import adams.sheek.montazeranapp.utils.extension.Toaster
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import adams.sheek.montazeranapp.utils.animatedThemeManager.AppTheme
import adams.sheek.montazeranapp.utils.animatedThemeManager.ThemeFragment
import adams.sheek.montazeranapp.utils.animatedThemeManager.ThemeManager
import adams.sheek.montazeranapp.utils.animatedThemeManager.themes.LightTheme
import adams.sheek.montazeranapp.utils.animatedThemeManager.themes.MyAppTheme
import adams.sheek.montazeranapp.utils.animatedThemeManager.themes.NightTheme
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.material.switchmaterial.SwitchMaterial
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListTopicFragment : Fragment(), View.OnClickListener{

    private lateinit var binding: FragmentListTopicsBinding
    private val viewModel: ClassesViewModel by activityViewModels()
    private val loginViewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListTopicsBinding.inflate(inflater)
        binding.mobile.text = SharedConfig.getMobile()
        binding.naviView.findViewById<TextView>(R.id.mobile).text = SharedConfig.getMobile()
        syncData()
        initViewOnlClicks()
        initView()
        return binding.root
    }

    private fun initView(){
        if (SharedConfig.getAppTheme() == R.style.Black){
            binding.naviView.findViewById<SwitchMaterial>(R.id.switch1).isChecked = true
        }
    }

    private fun initViewOnlClicks(){
        binding.mobile2.setOnClickListener(this)
        binding.menu.setOnClickListener(this)
        binding.shop.setOnClickListener(this)
        binding.insta.setOnClickListener(this)
        binding.telegram.setOnClickListener(this)
        binding.call.setOnClickListener(this)
        binding.naviView.findViewById<SwitchMaterial>(R.id.switch1).setOnClickListener(this)
        binding.naviView.findViewById<TextView>(R.id.logout).setOnClickListener(this)
        binding.naviView.findViewById<TextView>(R.id.support).setOnClickListener(this)
        binding.naviView.findViewById<TextView>(R.id.about).setOnClickListener(this)
        binding.naviView.findViewById<TextView>(R.id.report).setOnClickListener(this)
    }

    private fun syncData(forceUpdate: Boolean = false){
        viewModel.getTopics(forceUpdate) {
            if (it){
                binding.listTopic.adapter = ListTopicAdapter(InRamDs.listTopics){ topic ->
                    viewModel.selectedTopic = topic
                    findNavController().navigate(ListTopicFragmentDirections.actionListTopicFragmentToListClassesFragment())
                }
            }
        }
    }

    override fun onClick(v: View) {
        when(v){
            binding.insta -> {
                requireContext().startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/jalasate_ostad")))
            }
            binding.telegram -> {
                requireContext().startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://telegram.me/jalasate_ostad")))
            }
            binding.mobile2 -> {
                syncData(true)
            }
            binding.menu -> {
                binding.drawerMenu.openDrawer(GravityCompat.END)
            }
            binding.shop -> {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://montazeran-monji.ir")))
            }
            binding.naviView.findViewById<SwitchMaterial>(R.id.switch1) -> {
                if (SharedConfig.getAppTheme() == R.style.Default){
                    SharedConfig.setAppTheme(R.style.Black)
                    requireActivity().recreate()
                }else{
                    SharedConfig.setAppTheme(R.style.Default)
                    requireActivity().recreate()
                }
            }
            binding.naviView.findViewById<TextView>(R.id.logout) -> {
                Helper.showLogoutDialog(requireContext()){
                    loginViewModel.logout {
                        if (it){
                            SharedConfig.clearAll()
                            requireActivity().finishAndRemoveTask()
                            requireActivity().startActivity(Intent(requireActivity(), MainActivity::class.java))
                        }
                    }
                }
            }
            binding.naviView.findViewById<TextView>(R.id.report) -> {
                findNavController().navigate(ListTopicFragmentDirections.actionListTopicFragmentToSendMessageFragment())
            }
            binding.naviView.findViewById<TextView>(R.id.support) -> { makeCall() }
            binding.call ->                                          { makeCall() }
            binding.naviView.findViewById<TextView>(R.id.about) -> {
                findNavController().navigate(ListTopicFragmentDirections.actionListTopicFragmentToAboutFragment())
            }
        }
    }

    private fun makeCall(){
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:05138539186")
        requireContext().startActivity(intent)
    }


}