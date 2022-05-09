package adams.sheek.montazeranapp.ui.sendMessage

import adams.sheek.montazeranapp.data.repositories.SupportRepository
import adams.sheek.montazeranapp.utils.extension.Toaster
import com.skydoves.bindables.BindingViewModel
import com.skydoves.whatif.whatIfNotNullAs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SendMessageViewModel @Inject constructor(private val repository: SupportRepository) :
    BindingViewModel() {

    private var loading = false

    fun sendMessage(message: String, onSuccess: ()->Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            if (!loading){
                repository.sendMessage(
                    message,
                    onStart = {
                        loading = true
                    },
                    onComplete = {
                        loading = false
                    },
                ).collect{
                    if (it){
                        Toaster.show("پیام با موفقیت ارسال شد")
                        onSuccess.invoke()
                    }else{
                        Toaster.show("مشکلی رخ داده لطفا دوباره تلاش کنید")
                    }
                }
            }
        }
    }

}