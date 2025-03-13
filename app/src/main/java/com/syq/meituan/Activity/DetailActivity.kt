package com.syq.meituan.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.syq.meituan.Adapter.ListAdapter
import com.syq.meituan.Adapter.SingleAdapter
import com.syq.meituan.Fragment.PayFragment
import com.syq.meituan.R
import com.syq.meituan.ViewModel.MainViewModel
import com.syq.meituan.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() , SingleAdapter.OnQuantityChangeListener{
    lateinit var binding: ActivityDetailBinding
    private val viewModel = MainViewModel()

    var id: Int = 0;
    private var numberincart:ArrayList<Int> = arrayListOf(0,0,0,0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        id = intent.getIntExtra("id", 0)

        binding.backBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }



        binding.payBtn.setOnClickListener{
            val dialog = PayFragment()
            // 通过 Bundle 传递参数
            val args = Bundle().apply {
                putInt("id_key", id) // "id_key" 是参数名，id 是你要传递的值
                putIntegerArrayList("numberInCart",numberincart)
            }
            dialog.arguments = args // 将 Bundle 设置给 Fragment
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
            binding.single.adapter = adapter
            binding.progressBar4.visibility = View.GONE
        }
        viewModel.loadStore()
    }

    override fun onQuantityChanged(numberInCart: ArrayList<Int>) {
        numberincart = numberInCart
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