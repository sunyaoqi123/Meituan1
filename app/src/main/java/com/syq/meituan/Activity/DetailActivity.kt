package com.syq.meituan.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syq.meituan.Adapter.ListAdapter
import com.syq.meituan.Adapter.SingleAdapter
import com.syq.meituan.Adapter.StoreAdapter
import com.syq.meituan.Domain.ItemsModel
import com.syq.meituan.Fragment.PayFragment
import com.syq.meituan.R
import com.syq.meituan.Repository.UserRepository
import com.syq.meituan.SecurityUtils
import com.syq.meituan.ViewModel.MainViewModel
import com.syq.meituan.databinding.ActivityDetailBinding
import com.syq.meituan.databinding.ViewholderStoreBinding

class DetailActivity : AppCompatActivity() , SingleAdapter.OnQuantityChangeListener{
    lateinit var binding: ActivityDetailBinding
    private val viewModel = MainViewModel()

    var id: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        id = intent.getIntExtra("id", 0)
        val back: Button = findViewById(R.id.backBtn)

        back.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val pay: Button = findViewById(R.id.payBtn)
        pay.setOnClickListener {
            val dialog = PayFragment()
            dialog.show(supportFragmentManager, "MyCustomDialog")
        }
        initBanner()
        initList()
        initSingle()
    }

    private fun initBanner() {
        viewModel.loadStore().observeForever{
            binding.storeTitle.text = it[id].description[0]
            binding.storeExtra.text = it[id].extra[0]
            Glide.with(this@DetailActivity)
                .load(it[id].picUrl[0])
                .into(binding.singleStorePic)
        }
        viewModel.loadStore()
    }

    private fun initList(){
        binding.progressBar3.visibility = View.VISIBLE
        viewModel.loadStore().observeForever {
            binding.list.layoutManager =
                LinearLayoutManager(
                    this@DetailActivity, LinearLayoutManager.VERTICAL,
                    false
                )
            binding.list.adapter = ListAdapter(id,it)
            binding.progressBar3.visibility = View.GONE
        }
        viewModel.loadStore()
    }
    private fun initSingle() {
        binding.progressBar4.visibility = View.VISIBLE
        viewModel.loadStore().observeForever {
            binding.single.layoutManager = GridLayoutManager(this, 1)
            val adapter = SingleAdapter(id, it)
            adapter.setOnQuantityChangeListener(this) // 绑定监听器
            adapter.numberInCart = arrayListOf(0, 0, 0, 0)
            binding.single.adapter = adapter
            binding.progressBar4.visibility = View.GONE
        }
        viewModel.loadStore()
    }

    override fun onQuantityChanged(numberInCart: ArrayList<Int>) {
        // 直接使用传递的 numberInCart 数组计算总价
        updateTotalPrice(numberInCart)
    }

    private fun updateTotalPrice(numberInCart: ArrayList<Int>) {
        var total = 0.0
        viewModel.loadStore().observeForever {
            for (i in 0..3) {
                total += it[id].price[i + 1] * numberInCart[i]
                //Toast.makeText(this, "总价： ${total},物品数量：${numberInCart[i]},单价：${it[id].price[i]}", Toast.LENGTH_SHORT).show()
            }
            binding.allPrice.text = "到手约￥${String.format("%.2f", total)}"
        }
    }
}