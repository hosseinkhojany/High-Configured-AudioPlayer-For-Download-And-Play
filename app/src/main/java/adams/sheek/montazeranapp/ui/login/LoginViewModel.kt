package adams.sheek.montazeranapp.ui.login

import adams.sheek.montazeranapp.data.model.response.CheckUpdateData
import adams.sheek.montazeranapp.data.repositories.LoginRepository
import androidx.databinding.Bindable
import com.skydoves.bindables.BindingViewModel
import com.skydoves.bindables.bindingProperty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository
) : BindingViewModel() {

    @get:Bindable
    var loadingLogin: Boolean by bindingProperty(false)
        private set

    fun login(onCollect: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            loginRepository.login(onStart = {
                loadingLogin = true
            }, onComplete = {
                loadingLogin = false
            }).collect {
                CoroutineScope(Dispatchers.Main).launch { onCollect(it) }
            }
        }
    }

    fun logout(onCollect: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            loginRepository.login(onStart = {
                loadingLogin = true
            }, onComplete = {
                loadingLogin = false
            }).collect {
                CoroutineScope(Dispatchers.Main).launch { onCollect(it) }
            }
        }
    }

    fun checkUpdate(onCollect: (CheckUpdateData) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            loginRepository.getForceUpdate().collect {
                CoroutineScope(Dispatchers.Main).launch { onCollect(it) }
            }
        }
    }


}