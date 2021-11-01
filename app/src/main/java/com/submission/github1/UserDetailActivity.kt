package com.submission.github1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.submission.github1.database.FavoriteUser
import com.submission.github1.databinding.ActivityUserDetailBinding
import com.submission.github1.helper.ViewModelFactory

class UserDetailActivity : AppCompatActivity() {

    private var isFavorite: Boolean = false
    private var binding : ActivityUserDetailBinding? = null
    private lateinit var favoriteUser: FavoriteUser
    private lateinit var userViewModel: UserViewModel
    private lateinit var user : UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        userViewModel = obtainViewModel(this@UserDetailActivity)
        user = intent.getParcelableExtra<UserModel>(EXTRA_USER) as UserModel
        setTab(user.login!!)
        supportActionBar?.apply {
            title = user.login
            setDisplayHomeAsUpEnabled(true)
        }

        binding?.fabFavorite?.setOnClickListener { view ->
            if (view.id == R.id.fab_favorite) {
                when (isFavorite) {
                    true -> {
                        userViewModel.deleteFavoriteUser(favoriteUser as FavoriteUser)
                        showToast(getString(R.string.user_removed))
                    }
                    else -> {
                        favoriteUser.login = user.login
                        favoriteUser.avatarUrl = user.avatar_url
                        userViewModel.insertFavoriteUser(favoriteUser as FavoriteUser)
                        showToast(getString(R.string.user_added))
                    }
                }
            }
        }
        userViewModel.getFavUser(user.login!!).observe(this, { favUser ->
            checkFavoriteUser(favUser)
        })
        userViewModel.getDetailUser(user.login!!)
        userViewModel.isLoading.observe(this, { isLoading ->
            showLoading(isLoading)
        })
        userViewModel.getUser.observe(this, { response ->
            binding?.apply {
                repository.text = response.public_repos.toString()
                followers.text = response.followers.toString()
                following.text = response.following.toString()
                name.text = response.name
                company.text = response.company ?: "-"
                location.text = response.location ?: "-"
                Glide.with(this@UserDetailActivity)
                    .load(response.avatar_url)
                    .into(detailAvatar)
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

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun setTab(username: String) {
        val profilePagerAdapter = ProfilePagerAdapter(this, username)
        val viewPager: ViewPager2 = binding?.viewPager!!
        viewPager.adapter = profilePagerAdapter

        val tabs: TabLayout = binding?.tabs!!
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding?.apply {
                progressBar.visibility = View.VISIBLE
                profileSection.visibility = View.GONE
                profileDesc.visibility = View.GONE
                tabSection.visibility = View.GONE
            }
        } else {
            binding?.apply {
                progressBar.visibility = View.GONE
                profileSection.visibility = View.VISIBLE
                profileDesc.visibility = View.VISIBLE
                tabSection.visibility = View.VISIBLE
            }
        }
    }

    private fun checkFavoriteUser(favUser: FavoriteUser?) {
        if (favUser != null) {
            isFavorite = true
            favoriteUser = favUser
            binding?.fabFavorite?.setImageResource(R.drawable.ic_baseline_favorite_24_red)
        } else {
            isFavorite = false
            favoriteUser = FavoriteUser()
            binding?.fabFavorite?.setImageResource(R.drawable.ic_baseline_favorite_border_white)
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): UserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(UserViewModel::class.java)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.following,
            R.string.followers
        )
    }
}