package com.syq.meituan.Domain

import android.content.ClipDescription
import java.io.Serializable

data class ItemsModel(
    var description:ArrayList<String> = ArrayList(),
    var picUrl:ArrayList<String> = ArrayList(),
    var price: ArrayList<Double> = ArrayList(),
    var extra: ArrayList<String> = ArrayList(),
    var size: Int = 5,
    //var numberInCart: ArrayList<Int> = ArrayList(MutableList(size) { 0 })
): Serializable
