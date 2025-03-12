package com.syq.meituan.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syq.meituan.Domain.ItemsModel
import com.syq.meituan.databinding.ViewholderListBinding


class ListAdapter(var id: Int ,val items: MutableList<ItemsModel>) : RecyclerView.Adapter<ListAdapter.Viewholder>() {
    lateinit var context: Context

    class Viewholder(val binding: ViewholderListBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        context = parent.context
        val binding = ViewholderListBinding.inflate(LayoutInflater.from(context),parent,false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.binding.list.text = items[id].description[position + 1]
    }

    override fun getItemCount(): Int = items[id].description.size - 1

}