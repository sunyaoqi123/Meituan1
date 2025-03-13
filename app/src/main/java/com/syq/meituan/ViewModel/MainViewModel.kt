package com.syq.meituan.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.syq.meituan.Adapter.StoreAdapter
import com.syq.meituan.Domain.ItemsModel
import com.syq.meituan.Repository.MainRepository
import com.syq.meituan.databinding.ActivityMainBinding

class MainViewModel() : ViewModel(){
    private val repository = MainRepository()
   fun loadStore(): LiveData<MutableList<ItemsModel>>{
        return  repository.loadStore()
    }
}