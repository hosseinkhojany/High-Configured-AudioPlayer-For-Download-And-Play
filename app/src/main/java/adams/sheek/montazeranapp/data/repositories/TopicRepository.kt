package adams.sheek.montazeranapp.data.repositories

import adams.sheek.montazeranapp.data.datasource.local.TopicDao
import adams.sheek.montazeranapp.data.datasource.ram.InRamDs
import adams.sheek.montazeranapp.data.datasource.remote.TopicService
import adams.sheek.montazeranapp.data.model.Topic
import adams.sheek.montazeranapp.data.model.TopicFile
import adams.sheek.montazeranapp.utils.extension.Toaster
import com.skydoves.whatif.whatIfNotNull
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

class TopicRepository (private val service: TopicService,
                       private val dao: TopicDao,
                       private val coroutineDispatcher: CoroutineDispatcher
) : Repository{

    init {
        TopicRepository.dao = this.dao
    }

    fun getListTopic(forceRefresh: Boolean, onStart: () -> Unit,
                     onComplete: () -> Unit) = flow {
        try {
            val listTopics = dao.getAllTopic()
            if (listTopics.isEmpty() || forceRefresh){
                service.fetchListSubject().body().whatIfNotNull {
                    if (it.errors == null){
                        if(it.success){
                            InRamDs.listTopics.clear()
                            InRamDs.listTopics.addAll(it.data!!)
                            InRamDs.listTopics.forEach { topic ->
                                val newTopicFiles = mutableListOf<TopicFile>()
                                newTopicFiles.add(TopicFile(id = -1))
                                newTopicFiles.addAll(topic.files)
                                topic.files.clear()
                                topic.files.addAll(newTopicFiles)
                            }
                            dao.insertTopic(InRamDs.listTopics)
                        }
                    }else{
                        Toaster.show(it.errors)
                    }
                    emit(it.success)
                }
            }else{
                if (InRamDs.listTopics.isEmpty()){
                    InRamDs.listTopics.addAll(listTopics)
                }
                emit(true)
            }
        }catch (e: Exception){
            Toaster.show("خطا در اتصال به سرور")
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(coroutineDispatcher)

    fun getFileUrl(id: Int, file: String, onStart: () -> Unit,
                   onComplete: () -> Unit) = flow {
        try {
            service.fetchDownloadFile(id, file).body().whatIfNotNull {
                if (it.errors == null){
                    if(it.success){
                        updateTopicRecord(filename = file, url = it.data.downloadUrl, downloaded = false, downloading = true)
                        emit(it.data.downloadUrl)
                    }
                }else{
                    Toaster.show(it.errors)
                }
            }
        }catch (e: Exception){
            Toaster.show("خطا در اتصال به سرور")
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(coroutineDispatcher)

    fun registerDownloadStatusChecker() = flow {
        while(true){
            delay(1000)
            if (TopicRepository.onDownloadStatusChanged){
                TopicRepository.onDownloadStatusChanged = false
                emit(true)
            }
        }
    }.flowOn(coroutineDispatcher)

    companion object{
        var dao: TopicDao? = null
        var onDownloadStatusChanged = false
        //update topicFile with `url`
        fun updateTopicRecord(url: String, downloaded: Boolean, downloading: Boolean) {
            try {
                var selectedTopic: Topic? = null
                var selectedTopicFile: TopicFile? = null
                InRamDs.listTopics.forEach { topic ->
                    topic.files.forEach { topicFile ->
                        if (topicFile.downloadUrl == url){
                            selectedTopic = topic
                            selectedTopicFile = topicFile
                            selectedTopicFile?.let {
                                it.apply {
                                    isDownloading = downloading
                                    isDownloaded = downloaded
                                }
                                CoroutineScope(Dispatchers.IO).launch {
                                    dao?.insertTopic(selectedTopic!!)
                                    onDownloadStatusChanged = true
                                }
                            }
                        }
                    }
                }

            }catch (e: Exception){
                e.printStackTrace()
            }
        }
        //update topicFile with `filename`
        fun updateTopicRecord(filename: String, url: String, downloaded: Boolean, downloading: Boolean) {
            try {
                var selectedTopic: Topic? = null
                var selectedTopicFile: TopicFile? = null
                InRamDs.listTopics.forEach { topic ->
                    topic.files.forEach { topicFile ->
                        if (topicFile.filename == filename){
                            selectedTopic = topic
                            selectedTopicFile = topicFile
                            selectedTopicFile?.let {
                                it.apply {
                                    downloadUrl = url
                                    isDownloading = downloading
                                    isDownloaded = downloaded
                                }
                                CoroutineScope(Dispatchers.IO).launch {
                                    dao?.insertTopic(selectedTopic!!)
                                    onDownloadStatusChanged = true
                                }
                            }
                        }
                    }
                }

            }catch (e: Exception){
                e.printStackTrace()
            }
        }

        fun saveMediaState(filename: String, elapsedPosition: Float) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    var selectedTopic: Topic? = null
                    var selectedTopicFile: TopicFile? = null
                    InRamDs.listTopics.forEach { topic ->
                        topic.files.forEach { topicFile ->
                            if (topicFile.filename == filename){
                                selectedTopic = topic
                                selectedTopicFile = topicFile
                                selectedTopicFile?.let {
                                    it.apply {
                                        this.elapsedPosition = elapsedPosition
                                    }
                                    CoroutineScope(Dispatchers.IO).launch {
                                        dao?.insertTopic(selectedTopic!!)
                                    }
                                }
                            }
                        }
                    }

                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
    }
}