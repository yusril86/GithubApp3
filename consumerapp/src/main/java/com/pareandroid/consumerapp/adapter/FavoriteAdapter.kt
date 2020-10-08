package com.pareandroid.consumerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pareandroid.consumerapp.R
import com.pareandroid.consumerapp.model.User
import kotlinx.android.synthetic.main.item_list.view.*

class FavoriteAdapter() : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    var listUser = ArrayList<User>()
        set(listUser) {
            if (listUser.size > 0) {
                this.listUser.clear()
            }
            this.listUser.addAll(listUser)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = this.listUser.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mListUser = listUser[position]
        Glide.with(holder.itemView.context)
            .load(mListUser.avatar)
            .apply(RequestOptions().override(60, 60))
            .into(holder.itemView.ci_photo)

        holder.itemView.tv_name.text = mListUser.username

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


}