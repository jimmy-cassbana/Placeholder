package jimmy.assesment.placeholder.network

import jimmy.assesment.placeholder.data.PostEntity
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PostService {
    @GET("/posts")
    suspend fun getPosts(): List<PostEntity>

    @POST("/posts")
    suspend fun createPost(@Body post: PostEntity): PostEntity

    @PUT("/posts/{id}")
    suspend fun updatePost(@Path("id") id: Int, @Body post: PostEntity): PostEntity

    @DELETE("/posts/{id}")
    suspend fun deletePost(@Path("id") id: Int)
}