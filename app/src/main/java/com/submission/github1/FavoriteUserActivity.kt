package com.submission.github1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.github1.database.FavoriteUser
import com.submission.github1.databinding.ActivityFavoriteUserBinding
import com.submission.github1.helper.ViewModelFactory

class FavoriteUserActivity : AppCompatActivity() {
    private var _favoriteUserBinding: ActivityFavoriteUserBinding? = null
    private val binding get() = _favoriteUserBinding

    private lateinit var adapter: FavoriteUserAdpater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _favoriteUserBinding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.apply {
            title = getString(R.string.favorite)
            setDisplayHomeAsUpEnabled(true)
        }

        adapter = FavoriteUserAdpater()

        binding?.apply {
            rvUsers.layoutManager = LinearLayoutManager(this@FavoriteUserActivity)
            rvUsers.setHasFixedSize(true)
            rvUsers.adapter = adapter
        }

        val viewModel = obtainViewModel(this)
        viewModel.getListFavorite().observe(this, { favoriteUsers ->
            if (favoriteUsers != null) {
                adapter.setListFavorites(favoriteUsers)
                showEmptyState(favoriteUsers)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun obtainViewModel(activity: AppCompatActivity) : FavoriteUserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteUserViewModel::class.java)
    }

    private fun showEmptyState(list: List<FavoriteUser>) {
        if (list.isEmpty()) {
            binding?.apply {
                emptyState.visibility = View.VISIBLE
                rvUsers.visibility = View.GONE
            }
        } else {
            binding?.apply {
                emptyState.visibility = View.GONE
                rvUsers.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _favoriteUserBinding = null
    }
}