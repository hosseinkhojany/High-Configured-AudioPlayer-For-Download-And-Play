package adams.sheek.montazeranapp.data.model.response

import adams.sheek.montazeranapp.data.model.Topic
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class UserSubjectsResponse(val data: List<Topic>? = null): BaseResponse()
