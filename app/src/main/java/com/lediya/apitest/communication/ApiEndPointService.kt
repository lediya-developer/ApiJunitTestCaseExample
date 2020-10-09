package com.lediya.apitest.communication

import com.lediya.apitest.model.Posts
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

/**
 * Interface class to list out the API end points used in this application
 * Responses of each API call is mapped into a global {@link Result<T>} object.
 *
 */
interface ApiEndPointService {
    @GET("posts")
    suspend fun getAllPost(): Response<List<Posts>>
    @GET("users")
    suspend fun getAllUser(): Response<ResponseBody>

}