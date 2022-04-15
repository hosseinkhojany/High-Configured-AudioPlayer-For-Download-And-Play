package adams.sheek.montazeranapp.data.model.response

import kotlinx.serialization.Serializable

@Serializable
open class BaseResponse(val success: Boolean = false,
                        val errors: String? = null)
