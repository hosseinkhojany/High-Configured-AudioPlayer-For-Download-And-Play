package adams.sheek.montazeranapp.data.model.response

import kotlinx.serialization.Serializable

@Serializable
open class SendMessageResponse(val data: String) : BaseResponse()
