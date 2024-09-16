package com.sk.ziladelivery.ui.views.adapter

import android.content.Context
import android.util.Log
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
    private var allTripList: MutableList<AllTripModel>,  // Change to MutableList
    private val allTripListener: LisnerAllTrip           // Use val instead of var where possible
) : RecyclerView.Adapter<AllTripAdapter.ViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    // Update the dataset
    fun updateData(newData: List<AllTripModel>) {
        allTripList.clear()            // Clear old data
        allTripList.addAll(newData)    // Add new data
        notifyDataSetChanged()         // Notify adapter about the data change
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val binding: ItemAlltripListBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_alltrip_list, viewGroup, false
        )
        return ViewHolder(binding)
    }

    inner class ViewHolder(val mBinding: ItemAlltripListBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        fun bind(allTripModel: AllTripModel, position: Int) {
            mBinding.tvNoOrder.text = "Orders: ${allTripModel.orderCount}"
            mBinding.tvAmount.text = "Amount: ${allTripModel.totalAmount}"
            mBinding.tvAssitId.text = "${allTripModel.zilaTripMasterId}"

            if (allTripModel.tripCurrentStatus == "Delivering") {
                mBinding.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.red))
            } else {
                mBinding.tvStatus.setTextColor(
                    ContextCompat.getColor(context, R.color.colorLightBlueHeader)
                )
            }
            mBinding.tvStatus.text = allTripModel.tripCurrentStatus

            // Set click listener for the root layout

            if(allTripModel.isFreezed){
                allTripListener.onButtonClick(allTripModel)
            }else{
                if(position==0){
                    allTripListener.onButtonClick(allTripModel)
                }
            }




            mBinding.rlMain.setOnClickListener {
                allTripListener.onButtonClick(allTripModel)
            }
        }
    }

    override fun getItemCount(): Int {
        return allTripList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val allTripModel = allTripList[position]
        holder.bind(allTripModel,position)  // Bind data to ViewHolder
    }
}
