package com.ahmedtawfek.task.presentation.ui.posts.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedtawfek.task.domain.model.PostModel
import com.ahmedtawfek.task.domain.model.Result
import com.ahmedtawfek.task.domain.usecases.PostsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(private val postsUseCases: PostsUseCases) : ViewModel() {

    private val _postsListStateFlow : MutableStateFlow<Result<List<PostModel>>> = MutableStateFlow(Result.Idle)
    val postsListStateFlow = _postsListStateFlow.asStateFlow()

    private val _postsStateFlow : MutableStateFlow<Result<PostModel>> = MutableStateFlow(Result.Idle)
    val postsStateFlow = _postsStateFlow.asStateFlow()

    var currentPostId = -1
    var postsRetrieved = false

    fun getPostsList(){
        viewModelScope.launch {
            _postsListStateFlow.value = Result.Loading
            try {
                _postsListStateFlow.value = Result.Success(postsUseCases.getPostsListUseCase())
            }catch (e:Exception){
                _postsListStateFlow.value = Result.Error(e)
            }
        }
    }

    fun getPostDetails(postId : Int){
        viewModelScope.launch {
            _postsStateFlow.value = Result.Loading
            try {
                _postsStateFlow.value = Result.Success(postsUseCases.getPostDetailsUseCase(postId))
            }catch (e:Exception){
                _postsStateFlow.value = Result.Error(e)
            }
        }
    }

    fun handleServerErrors(exception :Exception) : String{
        return when(exception){
            is IOException -> "No internet available"
            is HttpException -> "Server error"
            else -> "Something went wrong, please try again."
        }
    }
}