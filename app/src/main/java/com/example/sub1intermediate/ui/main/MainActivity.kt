package com.example.sub1intermediate.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sub1intermediate.ui.detail.DetailStory
import com.example.sub1intermediate.R
import com.example.sub1intermediate.ui.upload.Upload
import com.example.sub1intermediate.ViewModelFactory
import com.example.sub1intermediate.ui.auth.login.Login
import com.example.sub1intermediate.databinding.ActivityMainBinding
import com.example.sub1intermediate.data.model.ListStoryItem
import com.example.sub1intermediate.data.repository.Result
import com.example.sub1intermediate.ui.map.MapsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val factory = ViewModelFactory.getInstance(this)
    private val mainViewModel: MainViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)

        binding.apply {
            rvStory.layoutManager = layoutManager
            rvStory.addItemDecoration(itemDecoration)
            addstory.setOnClickListener {
                val intent = Intent(this@MainActivity, Upload::class.java)
                startActivity(intent)
            }
        }

        mainViewModel.getToken().observe(this) { token ->
            if (token != null) {
                setListStories(token)
            }
        }
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                showLoading(true)
                mainViewModel.clearToken()
                showLoading(false)
                toLogin()
                super.onOptionsItemSelected(item)
            }
            R.id.action_map -> {
                toMap()
                super.onOptionsItemSelected(item)
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun setListStories(token: String) {
        val adapter = MainAdapter()
        adapter.setOnItemClickCallback(object : MainAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListStoryItem, optionsCompat: ActivityOptionsCompat) {
                val intent = Intent(this@MainActivity, DetailStory::class.java)
                intent.putExtra(EXTRA_ID, data.id)
                startActivity(intent, optionsCompat.toBundle())
            }
        })
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        mainViewModel.getAllStory(token).observe(this@MainActivity, {
            adapter.submitData(lifecycle, it)
        })
    }

    private fun toLogin() {
        val intent = Intent(this@MainActivity, Login::class.java)
        startActivity(intent)
        finish()
    }

    private fun toMap() {
        val intent = Intent(this@MainActivity, MapsActivity::class.java)
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressbar.visibility = View.VISIBLE
        } else {
            binding.progressbar.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }

}