package com.submission.github1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.github1.databinding.FragmentFollowersBinding
import com.submission.github1.helper.ViewModelFactory

class FollowersFragment : Fragment() {
    private var binding: FragmentFollowersBinding? = null
    private lateinit var userViewModel: UserViewModel
    private lateinit var listFollowingAdapter: ListFollowingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFollowersBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListAdapter()
        binding?.apply {
            rvFollowers.setHasFixedSize(true)
            rvFollowers.adapter = listFollowingAdapter
            rvFollowers.layoutManager = LinearLayoutManager(view.context)
        }

        userViewModel = obtainViewModel(requireActivity())

        if (arguments != null) {
            val username = arguments?.getString(ProfilePagerAdapter.EXTRA_USERNAME)
            userViewModel.getListFollowers(username!!)
        }
        userViewModel.listFollowers.observe(viewLifecycleOwner, { listFollowers ->
            listFollowingAdapter.setList(listFollowers)
        })
        userViewModel.isLoadingFollowers.observe(viewLifecycleOwner, { isLoading ->
            showLoading(isLoading)
        })
    }

    private fun initListAdapter() {
        listFollowingAdapter = ListFollowingAdapter()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding?.apply {
                rvFollowers.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }
        } else {
            binding?.apply {
                rvFollowers.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun obtainViewModel(activity: FragmentActivity): UserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(UserViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}