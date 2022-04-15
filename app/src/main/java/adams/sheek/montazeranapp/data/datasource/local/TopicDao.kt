package adams.sheek.montazeranapp.data.datasource.local

import adams.sheek.montazeranapp.data.model.Topic
import adams.sheek.montazeranapp.data.model.TopicFile
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TopicDao {

    @Query("SELECT * FROM Topic")
    suspend fun getAllTopic(): List<Topic>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopic(topic: Topic)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopic(topic: List<Topic>)

//    @Query("SELECT * FROM TopicFile")
//    suspend fun getAllTopicFile(): List<TopicFile>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertTopicFile(topicFile: TopicFile)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertTopicFile(topicFile: List<TopicFile>)

}