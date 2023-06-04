package com.example.sub1intermediate.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.sub1intermediate.ViewModelFactory
import com.example.sub1intermediate.databinding.ActivityDetailStoryBinding
import com.example.sub1intermediate.ui.main.MainActivity.Companion.EXTRA_ID
import com.example.sub1intermediate.data.model.Story
import com.example.sub1intermediate.data.repository.Result

class DetailStory : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        val factory = ViewModelFactory.getInstance(this)
        val detailStoryViewModel: DetailViewModel by viewModels {
            factory
        }

        val id = intent.getStringExtra(EXTRA_ID)
        setContentView(binding.root)

        detailStoryViewModel.apply {
            getToken().observe(this@DetailStory) { token ->
                if (token != null && id != null) {
                    getDetailStory(token, id).observe(this@DetailStory) {
                        when (it) {
                            is Result.Error -> showLoading(false)
                            is Result.Loading -> showLoading(true)
                            is Result.Success -> {
                                showDetail(it.data.story)
                                showLoading(false)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showDetail(story: Story) {
        binding.apply {
            tvUsername.text = story.name
            tvStory.text = story.description
            tvDate.text = story.createdAt
            Glide.with(this@DetailStory)
                .load(story.photoUrl)
                .centerCrop()
                .into(imageView2)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressbar.visibility = View.VISIBLE
        } else {
            binding.progressbar.visibility = View.GONE
        }
    }

}