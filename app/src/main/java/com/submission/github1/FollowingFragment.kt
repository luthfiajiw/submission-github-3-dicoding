package com.submission.github1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.github1.databinding.FragmentFollowingBinding
import com.submission.github1.helper.ViewModelFactory

class FollowingFragment : Fragment() {
    private var binding: FragmentFollowingBinding? = null
    private lateinit var userViewModel: UserViewModel
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

        userViewModel = obtainViewModel(requireActivity())

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

    private fun obtainViewModel(activity: FragmentActivity): UserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(UserViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}