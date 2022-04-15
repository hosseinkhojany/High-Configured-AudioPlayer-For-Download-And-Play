package adams.sheek.montazeranapp.data.datasource.ram

import adams.sheek.montazeranapp.data.model.Topic


object InRamDs {

    val listTopics = arrayListOf<Topic>()
    var selectedTopicPositionInList = -1
    var selectedTopicFilePositionInList = -1
    var playerServiceIsRunning = false
}