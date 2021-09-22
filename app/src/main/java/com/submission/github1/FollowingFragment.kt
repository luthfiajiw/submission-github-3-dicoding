package com.submission.github1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.submission.github1.databinding.FragmentFollowingBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FollowingFragment(private val username: String) : Fragment() {
    private var binding: FragmentFollowingBinding? = null
    private val userViewModel by viewModels<UserViewModel>()

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
        lifecycleScope.launch(Dispatchers.IO) {
            userViewModel.getListFollowing(username)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}