package adams.sheek.montazeranapp.data.repositories

import adams.sheek.montazeranapp.data.datasource.remote.LoginService
import adams.sheek.montazeranapp.utils.SharedConfig
import adams.sheek.montazeranapp.utils.extension.Toaster
import com.skydoves.whatif.whatIfNotNull
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

class LoginRepository (private val service: LoginService,
                       private val coroutineDispatcher: CoroutineDispatcher
) : Repository{

    fun login(onStart: () -> Unit,
              onComplete: () -> Unit) = flow {
        try {
            service.fetchLogin().body().whatIfNotNull {
                if (it.errors == null){
                    if(it.success){
                        SharedConfig.setUserToken("Bearer "+it.data!!.token)
                    }
                }else{
                    Toaster.show(it.errors)
                }
                emit(it.success)
            }
        }catch (e: Exception){
            e.message?.let { Toaster.show(it) }
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(coroutineDispatcher)

    fun getForceUpdate() = flow {
        try {
            service.fetchForceUpdate().body().whatIfNotNull {
                if (it.errors == null){
                    if(it.success){
                        emit(it.checkUpdate)
                    }
                }else{
                    Toaster.show(it.errors)
                }
            }
        }catch (e: Exception){
            Toaster.show("خطا در اتصال به سرور")
        }
    }.flowOn(coroutineDispatcher)

}