package adams.sheek.montazeranapp.data.datasource.remote

import adams.sheek.montazeranapp.data.config.retrofit.EndPoints
import javax.inject.Inject

class LoginService @Inject constructor(private val endPoints: EndPoints) {

    suspend fun fetchLogin() = endPoints.login()
    suspend fun fetchForceUpdate() = endPoints.forceUpdate()

}