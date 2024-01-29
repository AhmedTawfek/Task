package com.ahmedtawfek.task.presentation.ui.posts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmedtawfek.task.R
import com.ahmedtawfek.task.databinding.FragmentPostsListBinding
import com.ahmedtawfek.task.domain.model.Result
import com.ahmedtawfek.task.presentation.ui.activity.MainActivity
import com.ahmedtawfek.task.presentation.ui.posts.adapter.PostsRecyclerViewAdapter
import com.ahmedtawfek.task.presentation.ui.posts.viewmodel.PostsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PostsListFragment : Fragment() {

    private var _binding: FragmentPostsListBinding? = null
    private val binding get() = _binding!!

    private lateinit var postsViewModel: PostsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPostsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postsViewModel = (activity as MainActivity).postsViewModel

        with(binding){

            val recyclerViewAdapter = PostsRecyclerViewAdapter { postModel ->
                postsViewModel.currentPostId = postModel.id
                findNavController().navigate(R.id.action_postsListFragment_to_postsDetailsFragment)
            }

            recyclerViewPosts.adapter = recyclerViewAdapter
            recyclerViewPosts.layoutManager = LinearLayoutManager(context)

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    postsViewModel.postsListStateFlow.collect { result ->
                        withContext(Dispatchers.Main) {
                            when (result) {
                                is Result.Loading -> changeUiState(progressBarVisibility = View.VISIBLE)

                                is Result.Error -> {
                                    changeUiState(layoutNoInternetVisibility = View.VISIBLE)
                                    val error = postsViewModel.handleServerErrors(result.exception)
                                    textViewError.text = error
                                }

                                is Result.Success -> {
                                    changeUiState(recyclerVisibility = View.VISIBLE)
                                    recyclerViewAdapter.submitList(result.value)
                                }

                                else -> {}
                            }
                        }
                    }
                }
            }

            lifecycleScope.launch(Dispatchers.IO) {
                if (!postsViewModel.postsRetrieved) {
                    postsViewModel.getPostsList()
                    postsViewModel.postsRetrieved = true
                }
            }

            buttonRefresh.setOnClickListener {
                lifecycleScope.launch(Dispatchers.IO) {
                    postsViewModel.getPostsList()
                }
            }
        }
    }

    private fun changeUiState(progressBarVisibility : Int = View.GONE,layoutNoInternetVisibility : Int = View.GONE,recyclerVisibility : Int = View.GONE){
        with(binding) {
            if (progressBar.visibility != progressBarVisibility){
                progressBar.visibility = progressBarVisibility
            }

            if (recyclerViewPosts.visibility != recyclerVisibility){
                recyclerViewPosts.visibility = recyclerVisibility
            }

            if (layoutNoInternet.visibility != layoutNoInternetVisibility){
                layoutNoInternet.visibility = layoutNoInternetVisibility
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}