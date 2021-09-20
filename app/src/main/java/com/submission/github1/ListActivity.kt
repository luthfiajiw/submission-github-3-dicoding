package com.submission.github1

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.submission.github1.databinding.ActivityListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListActivity : AppCompatActivity() {
    private var binding : ActivityListBinding? = null
    private val userViewModel by viewModels<UserViewModel>()
    private lateinit var listUserAdapter: ListUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initListAdapter()
        binding?.apply {
            rvUsers.setHasFixedSize(true)
            rvUsers.adapter = listUserAdapter
            rvUsers.layoutManager = LinearLayoutManager(this@ListActivity)
        }

        supportActionBar?.apply {
            setBackgroundDrawable(ColorDrawable(Color.parseColor("#ff161b22")))
            elevation = 4F
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
                lifecycleScope.launch(Dispatchers.IO) {
                    userViewModel.getListUser(TypeListUser.SEARCH, query!!)
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return false
            }

        })
        return true
    }

    private fun initListAdapter() {
        listUserAdapter = ListUserAdapter()
        listUserAdapter.notifyDataSetChanged()
        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserModel) {
                val intent : Intent = Intent(this@ListActivity, UserDetailActivity::class.java)
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

    private fun showEmptyState(users: UsersModel) {
        if (users != null && users.total_count > 0) {
            binding?.emptyState?.visibility = View.GONE
        } else {
            binding?.emptyState?.visibility = View.VISIBLE
        }
    }
}