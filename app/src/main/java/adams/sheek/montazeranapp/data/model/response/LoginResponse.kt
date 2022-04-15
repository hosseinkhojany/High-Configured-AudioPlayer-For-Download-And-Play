package adams.sheek.montazeranapp.data.model.response

import adams.sheek.montazeranapp.data.model.User
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
open class LoginResponse(val data: User? = null): BaseResponse()
