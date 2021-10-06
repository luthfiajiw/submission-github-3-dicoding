package com.submission.github1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.submission.github1.databinding.ItemRowUserBinding

class ListFollowingAdapter() : RecyclerView.Adapter<ListFollowingAdapter.ListViewHolder>() {
    private var mData = UsersModel()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setList(list: List<UserModel>) {
        mData = UsersModel()
        mData.items = list
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
    ): ListFollowingAdapter.ListViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_following, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListFollowingAdapter.ListViewHolder, position: Int) {
        val user = mData.items[position]

        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return mData.items.size
    }
}