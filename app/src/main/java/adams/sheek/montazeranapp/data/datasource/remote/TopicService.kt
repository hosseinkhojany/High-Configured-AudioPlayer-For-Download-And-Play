package adams.sheek.montazeranapp.data.datasource.remote

import adams.sheek.montazeranapp.data.config.retrofit.EndPoints
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class TopicService @Inject constructor(private val endPoints: EndPoints) {

    suspend fun fetchListSubject() = endPoints.getUserSubjects()
    suspend fun fetchDownloadFile(id: Int, file: String) = endPoints.downloadFile(id = id, file = file.toRequestBody("text/plain".toMediaTypeOrNull()))

}