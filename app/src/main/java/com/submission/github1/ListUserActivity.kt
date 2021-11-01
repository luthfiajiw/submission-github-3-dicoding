package com.submission.github1

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.submission.github1.databinding.ActivityListBinding
import com.submission.github1.helper.ViewModelFactory

class ListUserActivity : AppCompatActivity() {
    private var binding : ActivityListBinding? = null
    private lateinit var userViewModel: UserViewModel
    private lateinit var listUserAdapter: ListUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        userViewModel = obtainViewModel(this@ListUserActivity)
        initListAdapter()
        binding?.apply {
            rvUsers.setHasFixedSize(true)
            rvUsers.adapter = listUserAdapter
            rvUsers.layoutManager = LinearLayoutManager(this@ListUserActivity)
        }

        userViewModel.isLoading.observe(this, { isLoading ->
            showLoading(isLoading)
        })
        userViewModel.snackbarText.observe(this, {
            it.getContentIfNotHandled()?.let { text ->
                Snackbar.make(window.decorView.rootView, text, Snackbar.LENGTH_SHORT).show()
            }
        })
        userViewModel.getUsers.observe(this, { users ->
            listUserAdapter.setData(users)
            showEmptyState(users)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query?.isEmpty() == true) return false

                searchView.clearFocus()
                userViewModel.getListUser(query!!)
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return false
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favotite -> {
                val intent = Intent(this@ListUserActivity, FavoriteUserActivity::class.java)
                startActivity(intent)
            }
            R.id.settings -> {
                val intent = Intent(this@ListUserActivity, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initListAdapter() {
        listUserAdapter = ListUserAdapter()
        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserModel) {
                val intent : Intent = Intent(this@ListUserActivity, UserDetailActivity::class.java)
                intent.putExtra(UserDetailActivity.EXTRA_USER, data)
                startActivity(intent)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        binding?.apply {
            if (state) {
                progressBar.visibility = View.VISIBLE
                rvUsers.visibility = View.GONE
                emptyState.visibility = View.GONE
            } else {
                progressBar.visibility = View.GONE
                rvUsers.visibility = View.VISIBLE
            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): UserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(UserViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun showEmptyState(users: UsersModel) {
        if (users.total_count > 0) {
            binding?.emptyState?.visibility = View.GONE
        } else {
            binding?.emptyState?.visibility = View.VISIBLE
        }
    }
}