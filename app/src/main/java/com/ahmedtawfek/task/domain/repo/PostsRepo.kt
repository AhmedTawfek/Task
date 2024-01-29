package com.ahmedtawfek.task.domain.repo

import com.ahmedtawfek.task.domain.model.PostModel
import com.ahmedtawfek.task.domain.model.Result

interface PostsRepo {
    suspend fun getPostsList() :List<PostModel>
    suspend fun getPostDetails(id : Int) : PostModel
}