package com.sk.ziladelivery.ui.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sk.ziladelivery.R
import com.sk.ziladelivery.data.model.AllTripModel
import com.sk.ziladelivery.databinding.ItemAlltripListBinding
import com.sk.ziladelivery.listener.LisnerAllTrip

class AllTripAdapter(
    private val context: Context,
    private val allTripList: List<AllTripModel>?,
    allTripLinser: LisnerAllTrip
) : RecyclerView.Adapter<AllTripAdapter.ViewHolder>() {
    private var layoutInflater: LayoutInflater? = null
    private var lisnerAllTrip: LisnerAllTrip? = null

    init {
        this.lisnerAllTrip = allTripLinser
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): AllTripAdapter.ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.context)
        }
        return ViewHolder(
            DataBindingUtil.inflate<ItemAlltripListBinding>(
                (layoutInflater)!!, R.layout.item_alltrip_list, viewGroup, false
            )
        )
    }

    inner class ViewHolder(var mBinding: ItemAlltripListBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

    }

    override fun getItemCount(): Int {
        return allTripList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val allTripModel = allTripList!![position]
        holder.mBinding.tvNoOrder.text="Orders : "+allTripModel.orderCount
        holder.mBinding.tvAmount.text="Amount : "+allTripModel.totalAmount
        holder.mBinding.tvAssitId.text= ""+allTripModel.zilaTripMasterId
        if (allTripModel.tripCurrentStatus=="Delivering"){
            holder.mBinding.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
            holder.mBinding.tvStatus.text= allTripModel.tripCurrentStatus
        }else{
            holder.mBinding.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.colorLightBlueHeader));
            holder.mBinding.tvStatus.text= allTripModel.tripCurrentStatus
        }


        holder.mBinding.rlMain.setOnClickListener {

            lisnerAllTrip!!.onButtonClick(allTripModel)
        }

    }
}