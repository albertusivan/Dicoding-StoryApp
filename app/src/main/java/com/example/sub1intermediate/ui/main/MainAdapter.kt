package com.example.sub1intermediate.ui.main

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sub1intermediate.R
import com.example.sub1intermediate.data.model.ListStoryItem

class MainAdapter: PagingDataAdapter<ListStoryItem, MainAdapter.ViewHolder>(DIFF_CALLBACK){
        private lateinit var onItemClickCallback: OnItemClickCallback

        interface OnItemClickCallback {
            fun onItemClicked(data: ListStoryItem, optionsCompat: ActivityOptionsCompat)
        }

        fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
            this.onItemClickCallback = onItemClickCallback
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
            ViewHolder(
                LayoutInflater.from(viewGroup.context).inflate(R.layout.item_story, viewGroup, false)
            )

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val ivStory: ImageView = view.findViewById(R.id.imageView2)
            val tvName: TextView = view.findViewById(R.id.tv_username)
            val tvDesc: TextView = view.findViewById(R.id.tv_story)
            val tvDate: TextView = view.findViewById(R.id.tv_date)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val story = getItem(position)
            holder.apply {
                Glide.with(itemView.context)
                    .load(story?.photoUrl)
                    .centerCrop()
                    .into(ivStory)
                tvName.text = story?.name
                tvDesc.text = story?.description
                tvDate.text = story?.createdAt
                itemView.setOnClickListener {
                    if (story != null) {
                        val optionsCompat: ActivityOptionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                itemView.context as Activity,
                                androidx.core.util.Pair(ivStory, "photo"),
                                androidx.core.util.Pair(tvName, "username"),
                                androidx.core.util.Pair(tvDesc, "desc"),
                                androidx.core.util.Pair(tvDate, "date")
                            )
                        onItemClickCallback.onItemClicked(story, optionsCompat)
                    }
                }
            }
        }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }




    }



