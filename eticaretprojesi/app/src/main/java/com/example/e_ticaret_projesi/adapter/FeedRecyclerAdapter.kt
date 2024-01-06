package com.example.e_ticaret_projesi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_ticaret_projesi.databinding.ActivityUploadBinding
import com.example.e_ticaret_projesi.databinding.RecyclerRowBinding
import com.example.e_ticaret_projesi.model.post

class FeedRecyclerAdapter(private val postList: ArrayList<post>):RecyclerView.Adapter<FeedRecyclerAdapter.PostHolder>() {
    class PostHolder(val binding: RecyclerRowBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
       val binding=RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
       holder.binding.recylerviewText.text=postList.get(position).ErkekGym
        holder.binding.recylerCommentText.text=postList.get(position).KadinGym
        holder.binding.recylerviewText.text=postList.get(position).Montkaban
        holder.binding.recylerviewText.text=postList.get(position).comment
        holder.binding.recylerviewText.text=postList.get(position).Pc

    }
}