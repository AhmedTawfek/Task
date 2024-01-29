package com.ahmedtawfek.task.data.posts.repo

import com.ahmedtawfek.task.data.posts.remote.PostsApi
import com.ahmedtawfek.task.domain.model.PostModel
import com.ahmedtawfek.task.domain.repo.PostsRepo

class PostsRepoImpl(private val postsApi: PostsApi) : PostsRepo {

    override suspend fun getPostsList(): List<PostModel> {
        return postsApi.getPostsList()
    }

    override suspend fun getPostDetails(id: Int): PostModel {
        return postsApi.getPostDetails(id)
    }
}