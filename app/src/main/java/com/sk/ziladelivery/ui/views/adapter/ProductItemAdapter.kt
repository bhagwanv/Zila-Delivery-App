package com.sk.ziladelivery.ui.views.adapter

import android.app.Activity
import com.sk.ziladelivery.data.model.OrderItemDetails
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sk.ziladelivery.R
import com.sk.ziladelivery.databinding.ItemProductItemBinding
import java.util.ArrayList

class ProductItemAdapter(
    private val context: Activity,
    private val orderDetails: ArrayList<OrderItemDetails>
) : RecyclerView.Adapter<ProductItemAdapter.ViewHolder>() {
    private var layoutInflater: LayoutInflater? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        return ViewHolder(
            DataBindingUtil.inflate(
                layoutInflater!!, R.layout.item_product_item, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemDetails = orderDetails[position]
        holder.mBinding.itItemName.text = itemDetails.itemname
        holder.mBinding.tvQty.text = itemDetails.qty.toString()
        holder.mBinding.tvAmount.text = Math.round(itemDetails.totalamt).toString()
    }

    override fun getItemCount(): Int {
        return orderDetails.size
    }

    inner class ViewHolder(var mBinding: ItemProductItemBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )
}