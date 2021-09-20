package com.submission.github1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.submission.github1.databinding.ItemRowUserBinding

class ListUserAdapter() : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
    private var mData = UsersModel()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setData(users: UsersModel) {
        mData = UsersModel()
        mData = users
        notifyDataSetChanged()
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRowUserBinding.bind(itemView)
        fun bind(userModel: UserModel) {
            with(itemView) {
                binding.apply {
                    username.text = userModel.login
                    Glide.with(context)
                        .load(userModel.avatar_url)
                        .into(listAvatar)
                }

                setOnClickListener{
                    onItemClickCallback.onItemClicked(userModel)
                }
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserModel)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListUserAdapter.ListViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListUserAdapter.ListViewHolder, position: Int) {
        val user = mData.items[position]

        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return mData.total_count
    }
}