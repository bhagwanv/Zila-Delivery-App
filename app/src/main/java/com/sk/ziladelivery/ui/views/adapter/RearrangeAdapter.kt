package com.sk.ziladelivery.ui.views.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sk.ziladelivery.R
import com.sk.ziladelivery.data.model.RearrangModel
import com.sk.ziladelivery.databinding.ItemRearrangeViewBinding
import com.sk.ziladelivery.listener.ItemTouchHelperContract
import com.sk.ziladelivery.listener.StartDragListener
import java.util.*
import kotlin.collections.ArrayList

class RearrangeAdapter(
    context: Context,
    private val rearrangeList: ArrayList<RearrangModel>?,
    private val startDragListener: StartDragListener?,
) : RecyclerView.Adapter<RearrangeAdapter.ViewHolder>(), ItemTouchHelperContract {
    private var layoutInflater: LayoutInflater?


    init {
        layoutInflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.context)
        }
        val mBinding = DataBindingUtil.inflate<ItemRearrangeViewBinding>(
            layoutInflater!!, R.layout.item_rearrange_view, viewGroup, false
        )
        return ViewHolder(mBinding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val model = rearrangeList!![i]
        viewHolder.mBinding.ShopName.text = model.shopName
        viewHolder.mBinding.skCode.text = model.skcode
        viewHolder.mBinding.shippingAdd.text = model.shippingAddress
        viewHolder.mBinding.orderList.text = model.orderList

        viewHolder.mBinding.imageview.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                startDragListener!!.requestDrag(viewHolder,rearrangeList)
            }
            false
        }


    }

    override fun getItemCount(): Int {
        return rearrangeList?.size ?: 0
    }

    inner class ViewHolder(var mBinding: ItemRearrangeViewBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )

    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(rearrangeList!!, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(rearrangeList!!, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onRowSelected(myViewHolder: MyListViewAdapter.ViewHolder?) {
        myViewHolder!!.mBinding.rlmaindragview.setBackgroundColor(Color.GRAY)
    }

    override fun onRowClear(myViewHolder: MyListViewAdapter.ViewHolder?) {
        myViewHolder!!.mBinding.rlmaindragview.setBackgroundColor(Color.WHITE)
    }
}