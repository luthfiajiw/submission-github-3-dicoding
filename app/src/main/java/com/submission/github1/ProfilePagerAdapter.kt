package com.submission.github1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ProfilePagerAdapter(activity: AppCompatActivity, private val username: String) : FragmentStateAdapter(activity) {
    companion object {
        var EXTRA_USERNAME = ""
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null

        val mFollowingFragment = FollowingFragment()
        val mFollowersFragment = FollowersFragment()
        val mBundle = Bundle()

        mBundle.putString(EXTRA_USERNAME, username)
        mFollowingFragment.arguments = mBundle
        mFollowersFragment.arguments = mBundle

        when(position) {
            0 -> fragment = mFollowingFragment
            1 -> fragment = mFollowersFragment
        }

        return fragment as Fragment
    }
}