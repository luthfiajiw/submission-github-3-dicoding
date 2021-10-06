package com.submission.github1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.github1.databinding.FragmentFollowingBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FollowingFragment() : Fragment() {
    private var binding: FragmentFollowingBinding? = null
    private val userViewModel by viewModels<UserViewModel>()
    private lateinit var listFollowingAdapter: ListFollowingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFollowingBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListAdapter()
        binding?.apply {
            rvFollowing.setHasFixedSize(true)
            rvFollowing.adapter = listFollowingAdapter
            rvFollowing.layoutManager = LinearLayoutManager(view.context)
        }

        if (arguments != null) {
            val username = arguments?.getString(ProfilePagerAdapter.EXTRA_USERNAME)
            userViewModel.getListFollowing(username!!)
        }
        userViewModel.listFollowing.observe(viewLifecycleOwner, { listFollowing ->
            listFollowingAdapter.setList(listFollowing)
        })
        userViewModel.isLoadingFollowing.observe(viewLifecycleOwner, { isLoading ->
            showLoading(isLoading)
        })
    }

    private fun initListAdapter() {
        listFollowingAdapter = ListFollowingAdapter()
        listFollowingAdapter.notifyDataSetChanged()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding?.apply {
                rvFollowing.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }
        } else {
            binding?.apply {
                rvFollowing.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}