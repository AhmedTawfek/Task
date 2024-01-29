package com.ahmedtawfek.task.data.posts.remote

import com.ahmedtawfek.task.domain.model.PostModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PostsApi {
    @GET("posts")
    suspend fun getPostsList(): List<PostModel>

    @GET("posts/{id}")
    suspend fun getPostDetails(@Path("id") postId  : Int) : PostModel
}