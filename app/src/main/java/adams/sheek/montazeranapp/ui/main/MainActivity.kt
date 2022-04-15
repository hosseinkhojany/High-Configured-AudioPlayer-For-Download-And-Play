package adams.sheek.montazeranapp.ui.main

import adams.sheek.montazeranapp.databinding.ActivityMainBinding
import adams.sheek.montazeranapp.ui.login.LoginViewModel
import adams.sheek.montazeranapp.utils.Helper
import adams.sheek.montazeranapp.utils.SharedConfig
import adams.sheek.montazeranapp.utils.exo.PlayerForegroundService
import adams.sheek.montazeranapp.utils.extension.Toaster
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(SharedConfig.getAppTheme())
        binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        Toaster.register(this)
        viewModel.checkUpdate {
            if (it.forceUpdate){
                Helper.showForceUpdateDialog(this, it.url)
                return@checkUpdate
            }
            if (it.needUpdate){
                Helper.showNeedUpdateDialog(this, it.url)
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q){
            PlayerForegroundService.kill = true
        }
    }

}