package com.sk.ziladelivery.ui.views.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sk.ziladelivery.R
import com.sk.ziladelivery.data.model.MyTripOrderResponseModel.OrderlistEntity
import com.sk.ziladelivery.databinding.MyTripOrderAdapterBinding
import com.sk.ziladelivery.ui.views.main.ProductDetailsActivity
import com.sk.ziladelivery.utilities.TextUtils

class MyTripOrderAdapter(
    private val context: Activity,
    private val orderlist: ArrayList<OrderlistEntity>?
) : RecyclerView.Adapter<MyTripOrderAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.my_trip_order_adapter, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.mBinding.tvOrderid.text = orderlist!![i].orderid.toString() + ""
        holder.mBinding.tvAmount.text = "₹ " + orderlist[i].amount
        holder.mBinding.tvNoItems.text = "No.Item " + orderlist[i].noofitems
        holder.mBinding.tvStatus.text = "Status : " + orderlist[i].status
        if (orderlist[i].reDispatchCount > 0) {
            holder.mBinding.btRedespach.visibility = View.VISIBLE
            holder.mBinding.btRedespach.text = "RD " + orderlist[i].reDispatchCount
        }
        if (orderlist[i].reAttemptCount > 0) {
            holder.mBinding.btReattamp.visibility = View.VISIBLE
            holder.mBinding.btReattamp.text = "RA " + orderlist[i].reAttemptCount
        }

      //  orderlist[i].orderType="ReturnOrder"

        if (!TextUtils.isNullOrEmpty(orderlist[i].orderType)) {
            holder.mBinding.tvOrderTypeStatus.text = "  " + orderlist[i].orderType
            if (orderlist[i].orderType=="Clearence Order"){
                holder.mBinding.tvOrderTypeStatus.setTextColor(ContextCompat.getColor(context, R.color.white))
                holder.mBinding.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.white))
                holder.mBinding.tvNoItems.setTextColor(ContextCompat.getColor(context, R.color.white))
                holder.mBinding.tvtype.setTextColor(ContextCompat.getColor(context, R.color.white))
                holder.mBinding.llMainLayout.setBackgroundResource(R.drawable.button_yellow_bottom_cl)
            }
            if (orderlist[i].orderType=="ReturnOrder"){
                holder.mBinding.tvOrderTypeStatus.setTextColor(ContextCompat.getColor(context, R.color.white))
                holder.mBinding.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.white))
                holder.mBinding.tvNoItems.setTextColor(ContextCompat.getColor(context, R.color.white))
                holder.mBinding.tvtype.setTextColor(ContextCompat.getColor(context, R.color.white))
                holder.mBinding.llMainLayout.setBackgroundResource(R.drawable.return_card_bg)
            }
        } else {
            holder.mBinding.liOrderType.visibility = View.GONE
        }

        holder.mBinding.tvOrderDetails.setOnClickListener {
            context.startActivity(
                Intent(
                    context,
                    ProductDetailsActivity::class.java
                ).putExtra("ORDER_ID", orderlist[holder.adapterPosition].orderid)
            )
        }
    }

    override fun getItemCount(): Int {
        return orderlist?.size ?: 0
    }

    inner class ViewHolder(var mBinding: MyTripOrderAdapterBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )
}