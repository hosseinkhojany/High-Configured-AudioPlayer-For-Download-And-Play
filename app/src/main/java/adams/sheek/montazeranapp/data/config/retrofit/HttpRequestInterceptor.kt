package adams.sheek.montazeranapp.data.config.retrofit

import adams.sheek.montazeranapp.BuildConfig
import adams.sheek.montazeranapp.utils.SharedConfig
import okhttp3.Interceptor
import okhttp3.Response

class HttpRequestInterceptor : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val originalRequest = chain.request()
    val request = if (!originalRequest.url.toString().contains("login")){
      originalRequest.newBuilder()
        .addHeader("Authorization", SharedConfig.getUserToken())
        .url(originalRequest.url)
    }else{
      originalRequest.newBuilder()
    }
    request.addHeader("appVersionCode", BuildConfig.VERSION_NAME)
    request.addHeader("Content-Type", "application/json")
    request.addHeader("Accept", "application/json")
    SharedConfig.logAllCfg()
    val response = chain.proceed(request.build())
    return response
  }
}
