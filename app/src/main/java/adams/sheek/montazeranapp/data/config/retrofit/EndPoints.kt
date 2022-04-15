package adams.sheek.montazeranapp.data.config.retrofit

import adams.sheek.montazeranapp.BuildConfig
import adams.sheek.montazeranapp.data.model.response.CheckUpdateResponse
import adams.sheek.montazeranapp.data.model.response.DownloadFileResponse
import adams.sheek.montazeranapp.data.model.response.LoginResponse
import adams.sheek.montazeranapp.data.model.response.UserSubjectsResponse
import adams.sheek.montazeranapp.utils.Helper
import adams.sheek.montazeranapp.utils.SharedConfig
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface EndPoints {

    @Multipart
    @POST("customer/login")
    suspend fun login(
        @Part("mobile") mobile: RequestBody = SharedConfig.getMobile().toRequestBody("text/plain".toMediaTypeOrNull()),
        @Part("password") password: RequestBody = "123456".toRequestBody("text/plain".toMediaTypeOrNull()),
        @Part("imei") imei: RequestBody = Helper.getUid().toRequestBody("text/plain".toMediaTypeOrNull())
    ): Response<LoginResponse>

    @Multipart
    @POST("checkUpdate")
    suspend fun forceUpdate(
        @Part("appVersionCode") appVersionCode: RequestBody = BuildConfig.VERSION_NAME.toRequestBody("text/plain".toMediaTypeOrNull())
        ): Response<CheckUpdateResponse>

    @Multipart
    @POST("subject")
    suspend fun getUserSubjects(
        @Part("mobile") mobile: RequestBody = SharedConfig.getMobile().toRequestBody("text/plain".toMediaTypeOrNull())
    ): Response<UserSubjectsResponse>

    @Multipart
    @POST("subject/download")
    suspend fun downloadFile(
        @Part("file") file: RequestBody,
        @Part("id") id: Int,
    ): Response<DownloadFileResponse>


}