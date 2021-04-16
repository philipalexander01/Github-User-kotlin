package com.example.consumer_app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.consumer_app.R
import com.example.consumer_app.databinding.ItemUserBinding
import com.example.consumer_app.pojo.GithubData

class ListUsersViewAdapter(private val type: String? = null) :
    RecyclerView.Adapter<ListUsersViewAdapter.ViewHolder>() {

    private val data = ArrayList<GithubData>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val userData = data[position]
            tvUsername.text = userData.username
            ivAvatar.loadImage(data[position].avatar_url.toString())

            if (type == holder.itemView.context.getString(R.string.search) || type == holder.itemView.context.getString(
                    R.string.favorite
                )
            ) {
                materialCardView.setOnClickListener {
                    onItemClickCallback.onItemClick(userData)
                }
            }
        }
    }

    fun setDataUser(dataUser: ArrayList<GithubData>) {
        data.clear()
        this.data.addAll(dataUser)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = data.size

    interface OnItemClickCallback {
        fun onItemClick(data: GithubData)
    }

    private fun ImageView.loadImage(url: String) {
        Glide.with(this.context).load(url).placeholder(R.drawable.ic_launcher_background).into(this)
    }

}