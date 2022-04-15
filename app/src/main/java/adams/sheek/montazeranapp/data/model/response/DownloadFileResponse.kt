package adams.sheek.montazeranapp.data.model.response

import kotlinx.serialization.Serializable

@Serializable
open class DownloadFileResponse( val data: DownloadUrl) : BaseResponse()
@Serializable
data class DownloadUrl( val downloadUrl: String)
