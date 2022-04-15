package adams.sheek.montazeranapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Topic(@PrimaryKey  val id: Int,
                 val name: String? = "",
                 val description: String? = "",
                 val duration: String? = "",
                 val image: String? = "",
                 val files: MutableList<TopicFile>)