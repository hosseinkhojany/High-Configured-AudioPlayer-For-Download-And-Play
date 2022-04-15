package adams.sheek.montazeranapp.ui.shared

import adams.sheek.montazeranapp.App
import adams.sheek.montazeranapp.R
import adams.sheek.montazeranapp.data.datasource.ram.InRamDs
import adams.sheek.montazeranapp.data.model.TopicFile
import adams.sheek.montazeranapp.data.repositories.TopicRepository
import adams.sheek.montazeranapp.utils.extension.Toaster
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
class ClassesViewModel @Inject constructor(private val topicRepository: TopicRepository)
    : BindingViewModel() {

    var selectedTopic: Int = -1
    var selectedTopicFile: TopicFile? = null

    @get:Bindable
    var loadingClasses: Boolean by bindingProperty(false)
        private set

    fun getTopics(forceRefresh: Boolean, onCollect: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            topicRepository.getListTopic(forceRefresh = forceRefresh, onStart = {
                loadingClasses = true
            }, onComplete = {
                if (forceRefresh){
                    Toaster.show("بروز رسانی انجام شد")
                }
                loadingClasses = false
            }).collect {
                CoroutineScope(Dispatchers.Main).launch { onCollect(it) }
            }
        }
    }

    fun registerOnDownloadChangeReceiver(onCollect: (Boolean, Int) -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            topicRepository.registerDownloadStatusChecker().collect {
                CoroutineScope(Dispatchers.Main).launch {
                    onCollect(it, InRamDs.selectedTopicFilePositionInList)
                }
            }
        }
    }



    @get:Bindable
    var loadingDownload: Boolean by bindingProperty(false)
        private set

    fun downloadFile(topicFile: TopicFile, onCollect: (String) -> Unit) {
        if(!loadingDownload){
            if (topicFile.downloadUrl?.isNotEmpty() == true){
                onCollect(topicFile.downloadUrl!!)
            }else{
                CoroutineScope(Dispatchers.IO).launch {
                    topicRepository.getFileUrl(InRamDs.listTopics[selectedTopic].id, topicFile.filename!!, onStart = {
                        loadingDownload = true
                    }, onComplete = {
                        loadingDownload = false
                    }).collect {
                        CoroutineScope(Dispatchers.Main).launch { onCollect(it) }
                    }
                }
            }
        }else{
            Toaster.show(App.context().getString(R.string.please_wait))
        }
    }

    fun saveCurrentMediaState(filename: String, elapsedPosition: Float) {
        TopicRepository.saveMediaState(filename, elapsedPosition)
    }

}