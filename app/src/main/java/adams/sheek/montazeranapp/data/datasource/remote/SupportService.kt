package adams.sheek.montazeranapp.data.datasource.remote

import adams.sheek.montazeranapp.data.config.retrofit.EndPoints
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class SupportService @Inject constructor(private val endPoints: EndPoints) {

    suspend fun sendMessage(message: String) =
        endPoints.sendMessage(message.toRequestBody("text/plain".toMediaTypeOrNull()))
}