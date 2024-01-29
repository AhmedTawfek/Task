package com.ahmedtawfek.task.presentation.ui.posts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ahmedtawfek.task.domain.model.PostModel
import com.ahmedtawfek.task.databinding.PostsItemLayoutBinding

class PostsRecyclerViewAdapter(val onClickListener: (PostModel) -> Unit) : ListAdapter<PostModel, PostsRecyclerViewAdapter.ViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<PostModel>() {
            override fun areItemsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ViewHolder(val binding : PostsItemLayoutBinding, itemPosition: (Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        init {
            // Define click listener for the ViewHolder's View.
            binding.cardViewPosts.setOnClickListener {
                itemPosition(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PostsItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding) { position ->
            onClickListener(getItem(position))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = getItem(position)
        holder.binding.textViewPostTitle.text = post.title
        holder.binding.textViewPostBody.text = post.body
    }
}