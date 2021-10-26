package com.submission.github1.helper

import androidx.recyclerview.widget.DiffUtil
import com.submission.github1.database.FavoriteUser

class FavoriteUserDiffCallback(private val mOldFavoriteUserList: List<FavoriteUser>, private val mNewFavoriteUserList: List<FavoriteUser>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = mOldFavoriteUserList.size

    override fun getNewListSize(): Int = mNewFavoriteUserList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldFavoriteUserList[oldItemPosition].id == mNewFavoriteUserList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldFavoriteUserList[oldItemPosition]
        val newEmployee = mNewFavoriteUserList[newItemPosition]
        return oldEmployee.login == newEmployee.login
    }
}