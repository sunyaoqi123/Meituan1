package com.syq.meituan.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.syq.meituan.Adapter.StoreAdapter
import com.syq.meituan.ViewModel.MainViewModel
import com.syq.meituan.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initStore()

    }

    private fun initStore() {
        binding.progressBar2.visibility = View.VISIBLE
        viewModel.loadStore().observeForever { storeList ->
            binding.store.layoutManager = GridLayoutManager(this, 1)
            val adapter = StoreAdapter(storeList).apply {
                setOnItemClickListener { selectedInt ->
                    navigateToDetail(selectedInt)
                }
            }
            binding.store.adapter = adapter
            binding.progressBar2.visibility = View.GONE
        }
        viewModel.loadStore()
    }

    private fun navigateToDetail(item: Int) {
        Intent(this, DetailActivity::class.java).apply {
            putExtra("id", item) // 传递必要参数
        }.also { startActivity(it) }
    }
}