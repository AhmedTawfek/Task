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
import com.ahmedtawfek.task.R
import com.ahmedtawfek.task.databinding.FragmentPostsDetailsBinding
import com.ahmedtawfek.task.databinding.FragmentPostsListBinding
import com.ahmedtawfek.task.domain.model.PostModel
import com.ahmedtawfek.task.domain.model.Result
import com.ahmedtawfek.task.presentation.ui.activity.MainActivity
import com.ahmedtawfek.task.presentation.ui.posts.viewmodel.PostsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PostsDetailsFragment : Fragment() {

    private var _binding: FragmentPostsDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var postsViewModel: PostsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPostsDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postsViewModel = (activity as MainActivity).postsViewModel

        with(binding){

            lifecycleScope.launch(Dispatchers.IO) {
                postsViewModel.getPostDetails(postsViewModel.currentPostId)
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    postsViewModel.postsStateFlow.collect { result ->
                        withContext(Dispatchers.Main) {
                            when (result) {
                                is Result.Loading -> changeUiState(progressBarVisibility = View.VISIBLE)

                                is Result.Error -> {
                                    changeUiState(layoutNoInternetVisibility = View.VISIBLE)
                                    val error = postsViewModel.handleServerErrors(result.exception)
                                    textViewError.text = error
                                }

                                is Result.Success -> {
                                    changeUiState(cardViewDetails = View.VISIBLE)
                                    showCurrentPostDetails(result.value)
                                }

                                else -> {}
                            }
                        }
                    }
                }
            }

            toolbar.setOnClickListener {
                findNavController().navigate(R.id.action_postsDetailsFragment_to_postsListFragment)
            }
        }
    }

    private fun showCurrentPostDetails(postModel: PostModel){
        with(binding){
            textViewPostTitle.text = postModel.title
            textViewPostBody.text = postModel.body
            textViewUserAndPostId.text = "UserId = ${postModel.userId} | PostId = ${postModel.id}"
        }
    }

    private fun changeUiState(progressBarVisibility : Int = View.GONE,layoutNoInternetVisibility : Int = View.GONE,cardViewDetails : Int = View.GONE){
        with(binding) {
            if (progressBar.visibility != progressBarVisibility){
                progressBar.visibility = progressBarVisibility
            }

            if (cardViewPostDetails.visibility != cardViewDetails){
                cardViewPostDetails.visibility = cardViewDetails
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