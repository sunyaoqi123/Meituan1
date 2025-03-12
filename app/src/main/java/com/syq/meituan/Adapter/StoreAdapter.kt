package com.syq.meituan.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.syq.meituan.Activity.MainActivity
import com.syq.meituan.Domain.ItemsModel
import com.syq.meituan.R
import com.syq.meituan.databinding.ViewholderStoreBinding
import kotlinx.coroutines.NonDisposableHandle.parent


class StoreAdapter(val items: MutableList<ItemsModel>) : RecyclerView.Adapter<StoreAdapter.Viewholder>() {
    lateinit var context: Context

    class Viewholder(val binding: ViewholderStoreBinding): RecyclerView.ViewHolder(binding.root)

    private var itemClickListener: ((position: Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (position: Int) -> Unit) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        context = parent.context
        val binding = ViewholderStoreBinding.inflate(LayoutInflater.from(context),parent,false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.binding.title.text = items[position].description[0]
        holder.binding.extra.text = items[position].extra[0]

        Glide.with(context)
            .load(items[position].picUrl[0])
            .into(holder.binding.storePic)

        holder.itemView.setOnClickListener {
            val currentPos = holder.adapterPosition
            items.getOrNull(currentPos)?.let { item ->
                itemClickListener?.invoke(position)
            }
        }
    }

    override fun getItemCount(): Int = items.size

}