package adams.sheek.montazeranapp.data.config.room

import adams.sheek.montazeranapp.data.datasource.local.TopicDao
import adams.sheek.montazeranapp.data.model.Topic
import adams.sheek.montazeranapp.data.model.TopicFile
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Topic::class, TopicFile::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun topicDao(): TopicDao
}