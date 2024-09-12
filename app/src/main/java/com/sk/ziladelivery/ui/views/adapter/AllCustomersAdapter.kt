package com.sk.ziladelivery.ui.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sk.ziladelivery.R
import com.sk.ziladelivery.data.model.AllTripModel
import com.sk.ziladelivery.data.model.CustomerList
import com.sk.ziladelivery.databinding.ItemAllOrderListBinding
import com.sk.ziladelivery.listener.LisnerAllOrder
import com.sk.ziladelivery.listener.LisnerCustomerAllOrder

class AllCustomersAdapter(
    private val context: Context,
    private var allTripList: MutableList<CustomerList>,
    private val lisnerAllTrip: LisnerAllOrder,
    private val onChildClickListener: LisnerCustomerAllOrder
) : RecyclerView.Adapter<AllCustomersAdapter.ViewHolder>() {

    private var layoutInflater: LayoutInflater? = null

    // Function to update data in the adapter
    fun updateData(newData: List<CustomerList>) {
        allTripList.clear()            // Clear old data
        allTripList.addAll(newData)    // Add new data
        notifyDataSetChanged()         // Notify adapter about the data change
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): AllCustomersAdapter.ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.context)
        }
        return ViewHolder(
            DataBindingUtil.inflate(
                layoutInflater!!, R.layout.item_all_order_list, viewGroup, false
            )
        )
    }

    // ViewHolder class
    inner class ViewHolder(var mBinding: ItemAllOrderListBinding) :
        RecyclerView.ViewHolder(mBinding.root)

    override fun getItemCount(): Int {
        return allTripList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val customerList = allTripList[position]

        // Bind data with null safety
        val customerInfo = customerList.customerInfo
        holder.mBinding.tvSkCode.text = "SK Code: ${customerInfo?.Skcode ?: "N/A"}"
        holder.mBinding.tvShopName.text = "Shop Name: ${customerInfo?.ShopName ?: "N/A"}"
        holder.mBinding.tvBillingAddress.text = "Billing Address: ${customerInfo?.BillingAddress ?: "N/A"}"
        holder.mBinding.tvTotalAmount.text = "Total Amount: ${customerInfo?.GrossAmount ?: "N/A"}"

        // Set up the child RecyclerView with horizontal layout
        holder.mBinding.rvChildItems.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val customerOrders = customerList.customerOrderInfo ?: listOf()  // Handle potential null
        holder.mBinding.rvChildItems.adapter = CustomersOrdersAdapter(customerOrders, onChildClickListener)

        // Handle main row click
        holder.mBinding.rlMain.setOnClickListener {
            customerInfo?.let {
                lisnerAllTrip.onButtonClick(it)
            }
        }
    }
}
