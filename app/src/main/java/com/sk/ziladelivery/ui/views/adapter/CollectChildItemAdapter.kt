package com.sk.ziladelivery.ui.views.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sk.ziladelivery.R
import com.sk.ziladelivery.data.model.CollectionPaymentModel
import com.sk.ziladelivery.databinding.CollectChildItemBinding

class CollectChildItemAdapter(
    private val context: Activity,
    var itemOrderDetailList: ArrayList<CollectionPaymentModel.ItemOrderDetailModel>
) : RecyclerView.Adapter<CollectChildItemAdapter.ViewHolder>() {
    private var layoutInflater: LayoutInflater? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        return ViewHolder(
            DataBindingUtil.inflate(
                layoutInflater!!, R.layout.collect_child_item, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val customerorderinfoEntity = itemOrderDetailList[position]
        holder.mBinding.itItemName.text = "" + customerorderinfoEntity.itemname
        holder.mBinding.TvAmount.text = customerorderinfoEntity.totalAmt.toString()
        holder.mBinding.tvQTY.text = customerorderinfoEntity.qty.toString()
    }

    override fun getItemCount(): Int {
        return itemOrderDetailList.size
    }

    inner class ViewHolder(var mBinding: CollectChildItemBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )
}