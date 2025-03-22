package com.syq.meituan.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syq.meituan.Domain.ItemsModel
import com.syq.meituan.databinding.ViewholderSingleBinding


class SingleAdapter(var id :Int, val items: MutableList<ItemsModel>) : RecyclerView.Adapter<SingleAdapter.Viewholder>() {
    lateinit var context: Context
    class Viewholder(val binding: ViewholderSingleBinding): RecyclerView.ViewHolder(binding.root)

    interface OnQuantityChangeListener {
        fun onQuantityChanged(updatedNumberInCart: ArrayList<Int>)
    }

    private var quantityListener: OnQuantityChangeListener? = null

    fun setOnQuantityChangeListener(listener: OnQuantityChangeListener) {
        this.quantityListener = listener
    }

    var numberInCart: ArrayList<Int> = arrayListOf(0, 0, 0, 0) // 根据实际长度初始化

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        context = parent.context
        val binding = ViewholderSingleBinding.inflate(LayoutInflater.from(context),parent,false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.binding.title.text = items[id].description[position + 1]
        holder.binding.extra.text = items[id].extra[position + 1]
        holder.binding.price.text ="￥" + items[id].price[position + 1]
        holder.binding.count.text = numberInCart[position].toString()
        Glide.with(context)
            .load(items[id].picUrl[position + 1])
            .into(holder.binding.storePic)

        holder.binding.addButton.setOnClickListener {
            numberInCart[position]++
            holder.binding.count.text = numberInCart[position].toString()
            quantityListener?.onQuantityChanged(numberInCart) // 触发回调
        }

        holder.binding.subButton.setOnClickListener {
            if (numberInCart[position] > 0) {
                numberInCart[position]--
                holder.binding.count.text = numberInCart[position].toString()

                quantityListener?.onQuantityChanged(numberInCart) // 触发回调
            }
        }

    }

    override fun getItemCount(): Int = items[id].description.size - 1

}