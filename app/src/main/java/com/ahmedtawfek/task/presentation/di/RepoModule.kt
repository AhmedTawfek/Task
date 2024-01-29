package com.ahmedtawfek.task.presentation.di

import com.ahmedtawfek.task.data.posts.remote.PostsApi
import com.ahmedtawfek.task.data.posts.repo.PostsRepoImpl
import com.ahmedtawfek.task.domain.repo.PostsRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    fun provideRepo(postsApi: PostsApi): PostsRepo{
        return PostsRepoImpl(postsApi)
    }
}