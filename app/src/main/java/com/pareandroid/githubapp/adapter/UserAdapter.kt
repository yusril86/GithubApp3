package com.pareandroid.githubapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pareandroid.githubapp.ui.DetailActivity
import com.pareandroid.githubapp.R
import com.pareandroid.githubapp.model.User
import kotlinx.android.synthetic.main.item_list.view.*

class UserAdapter(private var mListData: ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listUser = mListData[position]

        Glide.with(holder.itemView.context)
            .load(listUser.avatar)
            .apply(RequestOptions().override(60, 60))
            .into(holder.imgPhoto)

        holder.tvUsername.text = listUser.username

        holder.itemView.setOnClickListener {
            val dataUser = User(
                listUser.username,
                listUser.name,
                listUser.avatar,
                listUser.location,
                listUser.company,
                listUser.repository,
                listUser.follower,
                listUser.following,
                listUser.link,
                listUser.id
            )

            val intent = Intent(
                holder.itemView.context,
                DetailActivity::class.java
            )
            intent.putExtra(DetailActivity.EXTRA_USER, dataUser.username)
            holder.itemView.context.startActivity(intent)
        }


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgPhoto: ImageView = itemView.ci_photo
        var tvUsername: TextView = itemView.tv_name
    }

    fun setData(githubUser: ArrayList<User>) {
        mListData.addAll(githubUser)
        notifyDataSetChanged()
    }
}