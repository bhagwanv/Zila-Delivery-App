package com.sk.ziladelivery.ui.views.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sk.ziladelivery.R
import com.sk.ziladelivery.data.model.CustomerOrderInfo
import com.sk.ziladelivery.listener.LisnerCustomerAllOrder

class CustomersOrdersAdapter(
    private val childItems: List<CustomerOrderInfo>,
    private var onChildClickListener: LisnerCustomerAllOrder
) : RecyclerView.Adapter<CustomersOrdersAdapter.ViewHolder>() {

    init {
        this.onChildClickListener = onChildClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_customer_order_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = childItems[position]
        holder.bind(item, onChildClickListener)
    }

    override fun getItemCount(): Int = childItems.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvOderId: TextView = itemView.findViewById(R.id.tvOrderId)
        private val tvOrderAmount: TextView = itemView.findViewById(R.id.tvAmount)
        private val liMenu: LinearLayout = itemView.findViewById(R.id.liMenu)

        fun bind(item: CustomerOrderInfo, onChildClickListener: LisnerCustomerAllOrder) {
            tvOderId.text = "Order id: "+item.OrderId.toString()
            tvOrderAmount.text = "Amount: "+item.GrossAmount.toString()
            liMenu.setOnClickListener { onChildClickListener.onLisnerCustomerAllOrderClick(item) }
        }
    }
}