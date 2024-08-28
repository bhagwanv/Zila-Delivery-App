package com.sk.ziladelivery.ui.views.adapter

import android.app.Activity
import com.sk.ziladelivery.data.model.RoutModel
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sk.ziladelivery.R
import com.sk.ziladelivery.databinding.EntireRouteAdapterBinding
import com.sk.ziladelivery.utilities.TextUtils
import java.util.ArrayList

class EntireRouteAdapter(private val context: Activity, var addressList: ArrayList<RoutModel>) :
    RecyclerView.Adapter<EntireRouteAdapter.ViewHolder>() {
    private var layoutInflater: LayoutInflater? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        return ViewHolder(
            DataBindingUtil.inflate<EntireRouteAdapterBinding>(
                layoutInflater!!, R.layout.entire_route_adapter, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val routModel = addressList[i]
        holder.mBinding.tvAdd.text = routModel.shippingAddress
        if (!TextUtils.isNullOrEmpty(routModel.customerName)) {
            holder.mBinding.tvShopName.text = routModel.skcode + " , " + routModel.customerName
        }
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    inner class ViewHolder(var mBinding: EntireRouteAdapterBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )
}