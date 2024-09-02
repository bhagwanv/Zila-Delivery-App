package com.sk.ziladelivery.ui.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sk.ziladelivery.R
import com.sk.ziladelivery.data.model.CustomerList
import com.sk.ziladelivery.databinding.ItemAllOrderListBinding
import com.sk.ziladelivery.listener.LisnerAllOrder
import com.sk.ziladelivery.listener.LisnerCustomerAllOrder

class AllCustomersAdapter(
    private val context: Context,
    private val allTripList: List<CustomerList>?,
    allTripLinser: LisnerAllOrder,
    private val onChildClickListener: LisnerCustomerAllOrder
) : RecyclerView.Adapter<AllCustomersAdapter.ViewHolder>() {
    private var layoutInflater: LayoutInflater? = null
    private var lisnerAllTrip: LisnerAllOrder? = null

    init {
        this.lisnerAllTrip = allTripLinser
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): AllCustomersAdapter.ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.context)
        }
        return ViewHolder(
            DataBindingUtil.inflate<ItemAllOrderListBinding>(
                (layoutInflater)!!, R.layout.item_all_order_list, viewGroup, false
            )
        )
    }

    inner class ViewHolder(var mBinding: ItemAllOrderListBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
    }

    override fun getItemCount(): Int {
        return allTripList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val allTripModel = allTripList!![position]
        holder.mBinding.tvSkCode.text="SK Code: ${allTripModel.customerInfo!!.Skcode!!}"
        holder.mBinding.tvShopName.text="Shop Name: "+allTripModel.customerInfo!!.ShopName
        holder.mBinding.tvBillingAddress.text= "Billing Address: "+allTripModel.customerInfo!!.BillingAddress
        holder.mBinding.tvTotalAmount.text= "Total Ammount: "+allTripModel.customerInfo!!.GrossAmount
       // Set up the child RecyclerView
        holder.mBinding.rvChildItems.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.mBinding.rvChildItems.adapter = CustomersOrdersAdapter(allTripModel.customerOrderInfo, onChildClickListener!!)

        holder.mBinding.rlMain.setOnClickListener {
            lisnerAllTrip!!.onButtonClick(allTripModel.customerInfo!!)
        }

    }
}