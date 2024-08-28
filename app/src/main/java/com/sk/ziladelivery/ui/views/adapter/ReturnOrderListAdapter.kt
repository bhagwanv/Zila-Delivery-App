package com.sk.ziladelivery.ui.views.adapter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sk.ziladelivery.R
import com.sk.ziladelivery.databinding.ItemReturnOrderListBinding
import com.sk.ziladelivery.listener.ButtonClick
import com.sk.ziladelivery.data.model.ReturnOrderListModel
import com.sk.ziladelivery.utilities.Utils
import java.util.*

class ReturnOrderListAdapter : RecyclerView.Adapter<ReturnOrderListAdapter.ViewHolder> {
    private var activity: Activity? = null
    private var list: ArrayList<ReturnOrderListModel>? = null
    private var buttonClick: ButtonClick? = null


    constructor(
        activity: Activity?,
        list: ArrayList<ReturnOrderListModel>?,
        buttonClick: ButtonClick?
    ) {
        this.activity = activity
        this.list = list
        this.buttonClick = buttonClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_return_order_list, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val model: ReturnOrderListModel = list!![i]
        holder.mBinding.tvOrderId.text = "OrderId: " + model.orderId
        if (model.requestType == 0) holder.mBinding.tvOrderType.text =
            "Return Order" else holder.mBinding.tvOrderType.text = "Replace Order"
        holder.mBinding.tvDate.text = Utils.getDayMonthFormat(model.modifiedDate)
        holder.mBinding.tvName.text = model.name
        holder.mBinding.tvShopname.text = model.shopName
        holder.mBinding.tvAddress.text = model.shippingAddress
        holder.mBinding.tvStatus.text = model.status
    }

    override fun getItemCount(): Int {
        return if (list == null) 0 else list!!.size
    }

    inner class ViewHolder(var mBinding: ItemReturnOrderListBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        init {
            mBinding.liCall.setOnClickListener {
                activity!!.startActivity(
                    Intent(
                        Intent.ACTION_DIAL,
                        Uri.parse("tel:" + "+91" + list!![adapterPosition].mobile)
                    )
                )
            }
            mBinding.liDirection.setOnClickListener {
                val mapUri =
                    Uri.parse("geo:0,0?q=" + Uri.encode(list!![adapterPosition].shippingAddress))
                val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                activity!!.startActivity(mapIntent)
            }
            mBinding.tvDetail.setOnClickListener {
                buttonClick?.onButtonClick(adapterPosition)
            }
            mBinding.root.setOnClickListener {
                buttonClick?.onButtonClick(adapterPosition)
            }
        }
    }
}