package com.submission.github1

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.submission.github1.database.FavoriteUser
import com.submission.github1.repository.FavoriteUserRepository

class FavoriteUserViewModel(application: Application) : ViewModel() {
    private val mFavoriteUserRepository = FavoriteUserRepository(application)

    fun getListFavorite(): LiveData<List<FavoriteUser>> = mFavoriteUserRepository.getListUser()
}