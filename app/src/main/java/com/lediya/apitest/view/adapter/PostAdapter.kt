package com.lediya.apitest.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.lediya.apitest.R
import com.lediya.apitest.databinding.PostListItemRowBinding
import com.lediya.apitest.model.Posts

class PostAdapter : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    private lateinit var binding: PostListItemRowBinding
    private lateinit var postList: List<Posts>
  /**
   * Create the view holder for the post list data */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        binding = createBinding(parent)
        return ViewHolder(binding)
    }
    /**
     * Create binding  for the post list data */
    private fun createBinding(parent: ViewGroup): PostListItemRowBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.post_list_item_row,
            parent,
            false
        )
    }

    /**
     * initialise adapter with data
     */
    fun setItems(itemList: List<Posts>) {
        this.postList = itemList
        notifyDataSetChanged()
    }
/**
 * Get the item total count
 */
    override fun getItemCount(): Int {
        return postList.size
    }
    /**
     * Set the data using the bind view holder
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = postList[position]
        holder.binding.title.text = item.title
        holder.binding.description.text = item.body
    }

    class ViewHolder(val binding: PostListItemRowBinding) :
        RecyclerView.ViewHolder(binding.root)
}