package com.syq.meituan.Fragment

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syq.meituan.Adapter.PayAdapter
import com.syq.meituan.Adapter.SingleAdapter
import com.syq.meituan.R
import com.syq.meituan.ViewModel.MainViewModel
import com.syq.meituan.databinding.ActivityDetailBinding
import com.syq.meituan.databinding.FragmentPayBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PayFragment :DialogFragment(),PayAdapter.OnQuantityChangeListener{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var id: Int = -1
    private var _binding: FragmentPayBinding? = null
    private val binding get() = _binding!!
    private val viewModel = MainViewModel()
    private var numberInCart:ArrayList<Int> = arrayListOf(0,0,0,0)
    private var Price: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TopRoundedDialog)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        arguments?.let {
            id = it.getInt("id_key", -1) // 使用相同的 key
            numberInCart = it.getIntegerArrayList("numberInCart")?.let {
                ArrayList(it) // 转换为非空 ArrayList<Int>
            } ?: arrayListOf() // 默认值
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPayBinding.inflate(inflater, container, false)
        return binding.root
        //return inflater.inflate(R.layout.fragment_pay, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPay() // 确保此时 binding 已可用
        initStoreTitle()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PayFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PayFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onStart() {
        super.onStart()
        // 设置弹窗宽度占屏幕 90%，高度自适应，并居中显示
        dialog?.window?.apply {
            setLayout(
                (resources.displayMetrics.widthPixels * 1).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setGravity(Gravity.BOTTOM)  // 或 Gravity.TOP 贴顶显示
        }
    }

    private fun initStoreTitle() {
        viewModel.loadStore().observeForever{
            binding.storeTitle.text = it[id].description[0]
        }
        viewModel.loadStore()
    }

    private fun initPay(){
        binding.progressBar4.visibility = View.VISIBLE
        viewModel.loadStore().observeForever {
            binding.recyclerViewFood.layoutManager = GridLayoutManager(context,1)
            binding.recyclerViewFood.adapter = PayAdapter(id,it,numberInCart)
            val adapter = PayAdapter(id, it,numberInCart)
            adapter.setOnQuantityChangeListener(this) // 绑定监听器
            binding.recyclerViewFood.adapter = adapter
            binding.progressBar4.visibility = View.GONE
        }
        viewModel.loadStore()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 防止内存泄漏
    }

    override fun onQuantityChanged(price: Double) {
        Price = price
        // 直接使用传递的 numberInCart 数组计算总价
        updateTotalPrice(price)
    }

    private fun updateTotalPrice(price: Double) {
        viewModel.loadStore().observeForever {
            binding.allPrice.text = "合计￥${String.format("%.2f", price)}"
            Toast.makeText(context, "合计：${price}", Toast.LENGTH_SHORT).show()
        }
    }
}