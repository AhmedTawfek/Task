package com.ahmedtawfek.task.domain.usecases

import com.ahmedtawfek.task.domain.model.PostModel
import com.ahmedtawfek.task.domain.model.Result
import com.ahmedtawfek.task.domain.repo.PostsRepo

class PostsUseCases(private val postsRepo: PostsRepo) {
    suspend fun getPostsListUseCase() : List<PostModel> = postsRepo.getPostsList()
    suspend fun getPostDetailsUseCase(postId : Int) : PostModel = postsRepo.getPostDetails(postId)
}