package adams.sheek.montazeranapp.utils.extension

import adams.sheek.montazeranapp.ui.widgets.Snacky.snackError
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData

object Toaster {
    private val receiver = MutableLiveData<String>()
    fun register(activity: AppCompatActivity) {
        receiver.observe(activity) {
            activity.window.decorView.snackError(it)
        }
    }
    fun show(msg: String){
        receiver.postValue(msg)
    }
}