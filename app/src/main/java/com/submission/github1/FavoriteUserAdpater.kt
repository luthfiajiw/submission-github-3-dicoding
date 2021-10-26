package com.submission.github1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.submission.github1.database.FavoriteUser
import com.submission.github1.databinding.ItemRowUserBinding
import com.submission.github1.helper.FavoriteUserDiffCallback

class FavoriteUserAdpater : RecyclerView.Adapter<FavoriteUserAdpater.FavoriteUserViewHolder>() {
    private val listFavorites = ArrayList<FavoriteUser>()
    fun setListFavorites(listNew: List<FavoriteUser>) {
        val diffCallback = FavoriteUserDiffCallback(listFavorites, listNew)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listFavorites.clear()
        listFavorites.addAll(listNew)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class FavoriteUserViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRowUserBinding.bind(itemView)
        fun bind(favoriteUser: FavoriteUser) {
            with(itemView) {
                binding.apply {
                    username.text = favoriteUser.login
                    Glide.with(context)
                        .load(favoriteUser.avatarUrl)
                        .into(listAvatar)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteUserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return FavoriteUserViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteUserViewHolder, position: Int) {
        return holder.bind(listFavorites[position])
    }

    override fun getItemCount(): Int = listFavorites.size
}