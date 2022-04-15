package adams.sheek.montazeranapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


@Serializable
@Entity
data class TopicFile(
    @PrimaryKey  val id: Int,
    val title: String? = "",
    val filename: String? = "",
    val filesize: Double? = 0.0,
    @Transient var isDownloaded: Boolean = false,
    @Transient var isDownloading: Boolean = false,
    @Transient var downloadUrl: String? = null,
    @Transient var elapsedPosition: Float = 0.0F,
    val duration: String? = ""
)