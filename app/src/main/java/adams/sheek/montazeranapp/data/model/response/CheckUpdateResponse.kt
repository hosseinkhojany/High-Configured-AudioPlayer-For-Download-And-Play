package adams.sheek.montazeranapp.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class CheckUpdateResponse(val checkUpdate: CheckUpdateData):BaseResponse()
@Serializable
data class CheckUpdateData(val needUpdate: Boolean,val forceUpdate: Boolean, val url: String):BaseResponse()