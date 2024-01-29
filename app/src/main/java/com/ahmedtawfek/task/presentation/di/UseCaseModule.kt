package com.ahmedtawfek.task.presentation.di

import com.ahmedtawfek.task.domain.repo.PostsRepo
import com.ahmedtawfek.task.domain.usecases.PostsUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun providePostsUseCase(postsRepo: PostsRepo): PostsUseCases{
        return PostsUseCases(postsRepo)
    }
}