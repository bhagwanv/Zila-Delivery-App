package com.sk.ziladelivery.ui.views.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sk.ziladelivery.R
import com.sk.ziladelivery.databinding.ItemReturnOrderDetailBinding
import com.sk.ziladelivery.data.model.ReturnOrderItemModel
import java.util.*

class ReturnOrderDetailAdapter : RecyclerView.Adapter<ReturnOrderDetailAdapter.ViewHolder> {
    private var activity: Activity? = null
    private var list: ArrayList<ReturnOrderItemModel>? = null


    constructor(activity: Activity?, list: ArrayList<ReturnOrderItemModel>?) {
        this.activity = activity
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_return_order_detail, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: ReturnOrderItemModel = list!![position]
        holder.mBinding.tvSerialNo.text = "" + position + 1
        holder.mBinding.tvName.text = model.itemName
        holder.mBinding.tvPrice.text = "" + model.price
        if (model.requestType == 0) {
            holder.mBinding.tvQty.text = "" + model.qty
        } else {
            holder.mBinding.tvQty.text = "" + model.qty
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    class ViewHolder(var mBinding: ItemReturnOrderDetailBinding) :
        RecyclerView.ViewHolder(mBinding.root)
}