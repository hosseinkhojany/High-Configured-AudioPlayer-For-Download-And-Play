package adams.sheek.montazeranapp.data.model

import kotlinx.serialization.Serializable


@Serializable
data class User(val token: String,  val user: UserDetail)
@Serializable
data class UserDetail(val mobile: String)
