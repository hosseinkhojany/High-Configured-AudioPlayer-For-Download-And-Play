package adams.sheek.montazeranapp.data.config.room

import adams.sheek.montazeranapp.data.model.TopicFile
import androidx.room.TypeConverter
import com.google.gson.Gson

object Converters {
    @TypeConverter
    @JvmStatic
    fun listToJsonString(value: List<TopicFile>?): String = Gson().toJson(value)

    @TypeConverter
    @JvmStatic
    fun jsonStringToList(value: String) = Gson().fromJson(value, Array<TopicFile>::class.java).toList()
}