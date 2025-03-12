package com.syq.meituan.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.syq.meituan.Domain.ItemsModel

class MainRepository {
    private val firebaseDatabse = FirebaseDatabase.getInstance()
    fun loadStore():LiveData<MutableList<ItemsModel>>{
        val listData = MutableLiveData<MutableList<ItemsModel>>()
        val ref = firebaseDatabse.getReference("Items")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list= mutableListOf<ItemsModel>()
                for(childSnapshot in snapshot.children){
                    val item = childSnapshot.getValue(ItemsModel::class.java)
                    item?.let { list.add(it) }
                }
                listData.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        return listData
    }
}