package com.syq.meituan.Adapter

import android.content.Context
import android.media.RouteListingPreference
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syq.meituan.Domain.ItemsModel
import com.syq.meituan.databinding.ViewholderListBinding
import com.syq.meituan.databinding.ViewholderPayBinding

class PayAdapter(var id: Int, val items:MutableList<ItemsModel>,private var numberInCart: ArrayList<Int>): RecyclerView.Adapter<PayAdapter.Viewholder>() {

    lateinit var context: Context
    private var price = 0.0

    interface OnQuantityChangeListener {
        fun onQuantityChanged(updatedNumberInCart: Double)
    }

    private var quantityListener: OnQuantityChangeListener? = null

    fun setOnQuantityChangeListener(listener: OnQuantityChangeListener) {
        this.quantityListener = listener
    }

    // 定义过滤后的有效数据索引集合（核心改造）
    private val filteredPositions: List<Int> by lazy {
        numberInCart.indices
            .filter { index -> numberInCart[index] != 0 }
            .toList()
    }

    // 获取实际要显示的 item 数量
    override fun getItemCount(): Int = filteredPositions.size

    class Viewholder (val binding:ViewholderPayBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayAdapter.Viewholder {
        context = parent.context
        val binding = ViewholderPayBinding.inflate(LayoutInflater.from(context), parent, false)
        return Viewholder(binding)
    }


    override fun onBindViewHolder(holder: PayAdapter.Viewholder, position: Int) {
        val originalIndex = filteredPositions[position]
        val itemData = items[id]

        holder.binding.foodTitle.text = itemData.description[originalIndex + 1]
        holder.binding.price.text = "￥${itemData.price[originalIndex + 1]}"
        holder.binding.count.text = "*${numberInCart[originalIndex]}" // 根据实际需求调整数量显示
        price += itemData.price[originalIndex + 1] * numberInCart[originalIndex]
        Glide.with(holder.itemView.context)
            .load(itemData.picUrl[originalIndex + 1])
            .into(holder.binding.foodPic)
    }

    private fun loadPrice(){
        quantityListener?.onQuantityChanged(price)
    }
}