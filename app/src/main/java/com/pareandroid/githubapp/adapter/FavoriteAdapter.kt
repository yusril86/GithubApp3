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
import com.pareandroid.githubapp.R
import com.pareandroid.githubapp.model.User
import com.pareandroid.githubapp.ui.DetailActivity
import kotlinx.android.synthetic.main.item_list.view.*

class FavoriteAdapter() : RecyclerView.Adapter<FavoriteAdapter.FavotiteViewHolder>() {

    var listUser = ArrayList<User>()
        set(listUser) {
            if (listUser.size > 0) {
                this.listUser.clear()
            }
            this.listUser.addAll(listUser)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavotiteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return FavotiteViewHolder(view)
    }

    override fun getItemCount(): Int = this.listUser.size


    override fun onBindViewHolder(holder: FavotiteViewHolder, position: Int) {
        val mListUser = listUser[position]
        Glide.with(holder.itemView.context)
            .load(mListUser.avatar)
            .apply(RequestOptions().override(60, 60))
            .into(holder.imgPhoto)

        holder.tvName.text = mListUser.username

        holder.itemView.setOnClickListener {
            val dataUser = User(
                mListUser.username,
                mListUser.name,
                mListUser.avatar,
                mListUser.location,
                mListUser.company,
                mListUser.repository,
                mListUser.follower,
                mListUser.following,
                mListUser.link,
                mListUser.id
            )
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra(DetailActivity.EXTRA_USER,dataUser.username)
            intentDetail.putExtra(DetailActivity.EXTRA_AVATAR_URL,dataUser.avatar)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    inner class FavotiteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var imgPhoto : ImageView = itemView.ci_photo
        var tvName : TextView = itemView.tv_name

    }
}