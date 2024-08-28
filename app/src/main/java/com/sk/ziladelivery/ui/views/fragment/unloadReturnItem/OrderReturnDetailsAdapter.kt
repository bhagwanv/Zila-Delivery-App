package com.sk.ziladelivery.ui.views.fragment.unloadReturnItem

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sk.ziladelivery.R
import android.annotation.SuppressLint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.sk.ziladelivery.data.model.TripOrderStatusUpdateModel
import com.sk.ziladelivery.databinding.ItemOrderReturnDetailPageBinding
import com.sk.ziladelivery.ui.views.adapter.ChildItemInfoAdapter
import java.util.ArrayList

class OrderReturnDetailsAdapter(
    private val context: Activity,
    var customerListDcs: ArrayList<TripOrderStatusUpdateModel.CustomerorderinfoEntity>,
    private val orderDetailInterface: OrderDetailReturnActivity) : RecyclerView.Adapter<OrderReturnDetailsAdapter.ViewHolder>() {

    private var layoutInflater: LayoutInflater? = null
    private var isShowDetailsList = true
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        return ViewHolder(
            DataBindingUtil.inflate(
                layoutInflater!!,
                R.layout.item_order_return_detail_page,
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val customerOrderInfoEntity = customerListDcs[position]
        holder.mBinding.tvItemId.text = "ID: " + customerOrderInfoEntity.orderid
        holder.mBinding.tvOrderStatus.text = customerOrderInfoEntity.status
        holder.mBinding.tvAmount.text = "₹" + customerOrderInfoEntity.remaningAmount
        holder.mBinding.tvTotalGrossAmount.text = "₹" + customerOrderInfoEntity.grossamount
        holder.mBinding.tvorderType.text = customerOrderInfoEntity.orderType
        holder.mBinding.rvItemDetailList.layoutManager = LinearLayoutManager(context)
        val orderDetailsAdapter = ChildItemInfoAdapter(context, customerOrderInfoEntity.orderDetailsModel)
        holder.mBinding.rvItemDetailList.adapter = orderDetailsAdapter
        holder.mBinding.itemCheckBox.tag = 0
        if (customerOrderInfoEntity.messageText.equals("", ignoreCase = true)) {
            holder.mBinding.tvOrderStatusMessage.visibility = View.GONE
            holder.mBinding.tvItemId.isClickable = false
            holder.mBinding.tvItemId.isEnabled = false
        } else {
            holder.mBinding.tvOrderStatusMessage.visibility = View.VISIBLE
            holder.mBinding.tvOrderStatusMessage.text = customerOrderInfoEntity.messageText
            holder.mBinding.tvItemId.isClickable = true
            holder.mBinding.tvItemId.isEnabled = true
        }
        if (customerOrderInfoEntity.status.equals("Shipped", ignoreCase = true)) {
            holder.mBinding.tvItemId.setTextColor(ContextCompat.getColor(context, R.color.black))
        } else {
            holder.mBinding.tvItemId.setTextColor(ContextCompat.getColor(context, R.color.green))
        }
        holder.mBinding.itemCheckBox.isChecked = customerOrderInfoEntity.isBoolWorkingStatus
        if (customerListDcs[position].orderReDispatchCount <= customerListDcs[position].reAttemptCount) {
            holder.mBinding.btnReAttempt.visibility = View.INVISIBLE
        } else {
            holder.mBinding.btnReAttempt.visibility = View.VISIBLE
        }
        if (customerOrderInfoEntity.status == "Delivery Redispatch" || customerOrderInfoEntity.status == "Delivery Canceled" || customerOrderInfoEntity.status == "Delivered" || customerOrderInfoEntity.status == "ReAttempt") {
            holder.mBinding.itemCheckBox.visibility = View.INVISIBLE
            holder.mBinding.ivDownItem.visibility = View.INVISIBLE
        } else if (customerOrderInfoEntity.workingStatus == 6) { //Order Cancel
            holder.mBinding.itemCheckBox.visibility = View.INVISIBLE
            holder.mBinding.btnReAttempt.visibility = View.INVISIBLE
            holder.mBinding.ivDownItem.visibility = View.VISIBLE
        } else if (customerOrderInfoEntity.workingStatus == 7) { //Order Redispatch
            holder.mBinding.itemCheckBox.visibility = View.INVISIBLE
           // holder.mBinding.btnCancelOrder.visibility = View.INVISIBLE

        }  else if (customerOrderInfoEntity.workingStatus == 0) { //Order Redispatch
            holder.mBinding.itemCheckBox.visibility = View.VISIBLE
          //  holder.mBinding.btnCancelOrder.visibility = View.VISIBLE
        } else {
            //status ----> shipped
            holder.mBinding.itemCheckBox.visibility = View.VISIBLE
            holder.mBinding.ivDownItem.visibility = View.VISIBLE
            holder.mBinding.btnReAttempt.visibility = View.VISIBLE
           // holder.mBinding.btnCancelOrder.visibility = View.VISIBLE
            if (customerListDcs[position].orderReDispatchCount <= customerListDcs[position].reAttemptCount) {
                holder.mBinding.btnReAttempt.visibility = View.INVISIBLE
            } else {
                holder.mBinding.btnReAttempt.visibility = View.VISIBLE
            }
        }

        if (customerOrderInfoEntity.isOnilnePayment && customerOrderInfoEntity.workingStatus == 0) {
          //  holder.mBinding.btnCancelOrder.visibility = View.VISIBLE
            holder.mBinding.tvOrderPaymentMode.visibility = View.VISIBLE
        } else {
            holder.mBinding.tvOrderPaymentMode.visibility = View.GONE
            if (customerOrderInfoEntity.workingStatus == 7) {
              //  holder.mBinding.btnCancelOrder.visibility = View.INVISIBLE
            } else {
             //   holder.mBinding.btnCancelOrder.visibility = View.VISIBLE
            }
        }

        if (!customerOrderInfoEntity.isDeliveryCancelledEnable) {
           // holder.mBinding.btnCancelOrder.visibility = View.INVISIBLE
        }
        holder.mBinding.ivDownItem.setOnClickListener {
            if (isShowDetailsList) {
                holder.mBinding.llRvItemDetailList.visibility = View.VISIBLE
                isShowDetailsList = false
                holder.mBinding.ivDownItem.rotation = 180f
            } else {
                holder.mBinding.llRvItemDetailList.visibility = View.GONE
                isShowDetailsList = true
                holder.mBinding.ivDownItem.rotation = 0f
            }
        }
        holder.mBinding.itemCheckBox.setOnClickListener {
                orderDetailInterface.checkBoxClicked(
                    holder.mBinding.itemCheckBox.isChecked,
                    position)

        }
        /*holder.mBinding.tvItemId.setOnClickListener {
            holder.mBinding.itemCheckBox.isChecked = !holder.mBinding.itemCheckBox.isChecked
            orderDetailInterface.checkBoxClicked(holder.mBinding.itemCheckBox.isChecked, position)
        }*/
        holder.mBinding.btnCancelOrder.visibility = View.GONE
        holder.mBinding.btnCancelOrder.setOnClickListener {
            orderDetailInterface.onButtonClick(
                "Cancel Order", position,
                customerOrderInfoEntity.orderid, customerOrderInfoEntity
            )
        }
        holder.mBinding.btnReAttempt.setOnClickListener {
            orderDetailInterface.onButtonClick("Re Attempt", position, customerOrderInfoEntity.orderid, customerOrderInfoEntity
            )
        }
    }

    override fun getItemCount(): Int {
        return customerListDcs.size
    }

    inner class ViewHolder(var mBinding: ItemOrderReturnDetailPageBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )
}