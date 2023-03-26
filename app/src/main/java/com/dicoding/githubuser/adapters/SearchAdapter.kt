package com.dicoding.githubuser.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.activities.DetailActivity
import com.dicoding.githubuser.models.SearchModel

class SearchAdapter() : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    var mUsers: ArrayList<SearchModel> = ArrayList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatar: ImageView = itemView.findViewById(R.id.avatar)
        val login: TextView = itemView.findViewById(R.id.login)
        val score: TextView = itemView.findViewById(R.id.score)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val searchView = inflater.inflate(R.layout.item_search, parent, false)

        return ViewHolder(searchView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = mUsers[position]

        Glide
            .with(holder.itemView.context)
            .load(user.avatarUrl)
            .placeholder(R.drawable.placeholder)
            .circleCrop()
            .into(holder.avatar)

        holder.login.text = user.login
        holder.score.text = user.score.toString()

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, DetailActivity::class.java)
            intent.putExtra("login", user.login)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = mUsers.size
}