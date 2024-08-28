package com.sk.ziladelivery.ui.views.adapter

import android.app.Activity
import com.sk.ziladelivery.listener.OrderDetailInterface
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sk.ziladelivery.R
import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.sk.ziladelivery.databinding.ItemOrderDetailPageBinding
import com.sk.ziladelivery.data.model.TripOrderStatusUpdateModel
import java.util.ArrayList

class OrderDetailsAdapter(
    private val context: Activity,
    private var customerListDcs: ArrayList<TripOrderStatusUpdateModel.CustomerorderinfoEntity>,
    private val orderDetailInterface: OrderDetailInterface) : RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>() {
    private var layoutInflater: LayoutInflater? = null
    private var isShowDetailsList = true
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        return ViewHolder(
            DataBindingUtil.inflate(
                layoutInflater!!,
                R.layout.item_order_detail_page,
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

        if (customerOrderInfoEntity.isETAOrderHide) {
            holder.mBinding.btnReAttempt.visibility = View.VISIBLE
            holder.mBinding.btnCancelOrder.visibility = View.VISIBLE
            holder.mBinding.btnReDispatch.visibility = View.VISIBLE
        } else {
            holder.mBinding.btnReAttempt.visibility = View.VISIBLE
            holder.mBinding.btnCancelOrder.visibility = View.VISIBLE
            holder.mBinding.btnReDispatch.visibility = View.VISIBLE
        }

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

        if (customerListDcs[position].orderReDispatchCount <= customerListDcs[position].reDispatchCount) {
            holder.mBinding.btnReDispatch.visibility = View.GONE
        } else {
            holder.mBinding.btnReDispatch.visibility = View.VISIBLE

            /*if (customerOrderInfoEntity.isETAOrderHide) {
                holder.mBinding.btnReDispatch.visibility = View.VISIBLE
            } else {
                holder.mBinding.btnReDispatch.visibility = View.VISIBLE
            }*/

        }

        if (customerListDcs[position].orderReDispatchCount <= customerListDcs[position].reAttemptCount) {
            holder.mBinding.btnReAttempt.visibility = View.GONE
        } else {
            holder.mBinding.btnReAttempt.visibility = View.VISIBLE
        }

        viewMoreItemDetails(holder)
        if (customerOrderInfoEntity.status == "Delivery Redispatch" || customerOrderInfoEntity.status == "Delivery Canceled" || customerOrderInfoEntity.status == "Delivered" || customerOrderInfoEntity.status == "ReAttempt") {
            holder.mBinding.itemCheckBox.visibility = View.INVISIBLE
            holder.mBinding.ivDownItem.visibility = View.INVISIBLE


        } else if (customerOrderInfoEntity.workingStatus == 6) { //Order Cancel
            holder.mBinding.itemCheckBox.visibility = View.INVISIBLE
            holder.mBinding.btnReDispatch.visibility = View.GONE
            holder.mBinding.btnReAttempt.visibility = View.GONE
            holder.mBinding.ivDownItem.visibility = View.VISIBLE

        } else if (customerOrderInfoEntity.workingStatus == 7) { //Order Redispatch
            holder.mBinding.itemCheckBox.visibility = View.INVISIBLE
            holder.mBinding.btnCancelOrder.visibility = View.INVISIBLE
            holder.mBinding.btnReAttempt.visibility = View.INVISIBLE
            holder.mBinding.btnReDispatch.visibility = View.VISIBLE


        } else if (customerOrderInfoEntity.workingStatus == 8) { //Order ReAttemp
            holder.mBinding.itemCheckBox.visibility = View.INVISIBLE
            holder.mBinding.btnCancelOrder.visibility = View.INVISIBLE
            holder.mBinding.btnReAttempt.visibility = View.VISIBLE
            holder.mBinding.ivDownItem.visibility = View.VISIBLE
            holder.mBinding.btnReDispatch.visibility = View.INVISIBLE


        } /*else if (customerOrderInfoEntity.workingStatus == 0) {
            holder.mBinding.itemCheckBox.visibility = View.VISIBLE
            holder.mBinding.btnCancelOrder.visibility = View.VISIBLE
            holder.mBinding.btnReDispatch.visibility = View.VISIBLE
            holder.mBinding.btnReAttempt.visibility = View.VISIBLE

            *//*if (customerOrderInfoEntity.isETAOrderHide) {
                holder.mBinding.btnCancelOrder.visibility = View.VISIBLE
                holder.mBinding.itemCheckBox.visibility = View.VISIBLE
            } else {
                holder.mBinding.itemCheckBox.visibility = View.VISIBLE
                holder.mBinding.btnCancelOrder.visibility = View.VISIBLE
            }*//*
            holder.mBinding.ivDownItem.visibility = View.VISIBLE

        }*/ else {
            //status ----> shipped or customerOrderInfoEntity.workingStatus == 0

            holder.mBinding.itemCheckBox.visibility = View.VISIBLE
            holder.mBinding.ivDownItem.visibility = View.VISIBLE
            holder.mBinding.btnReDispatch.visibility = View.VISIBLE
            holder.mBinding.btnCancelOrder.visibility = View.VISIBLE

            if (customerOrderInfoEntity.isScaleUpPaymentOverdue) {
                holder.mBinding.itemCheckBox.visibility = View.INVISIBLE
                holder.mBinding.tvOrderStatusMessage.visibility = View.VISIBLE
                holder.mBinding.tvOrderStatusMessage.text = "स्केलअप पेमेंट देय होने के कारण डिलीवरी नहीं होगी|"
            } else {
                holder.mBinding.itemCheckBox.visibility = View.VISIBLE
            }

            if (customerListDcs[position].orderReDispatchCount <= customerListDcs[position].reDispatchCount) {
                holder.mBinding.btnReDispatch.visibility = View.GONE
            } else {
                holder.mBinding.btnReDispatch.visibility = View.VISIBLE
                /*   if (customerOrderInfoEntity.isETAOrderHide) {
                       holder.mBinding.btnReDispatch.visibility = View.VISIBLE
                   } else {
                       holder.mBinding.btnReDispatch.visibility = View.VISIBLE
                   }*/

            }

            if (customerListDcs[position].orderReDispatchCount <= customerListDcs[position].reAttemptCount) {
                holder.mBinding.btnReAttempt.visibility = View.GONE
            } else {
                holder.mBinding.btnReAttempt.visibility = View.VISIBLE
            }
        }

        if (customerOrderInfoEntity.isOnilnePayment && customerOrderInfoEntity.workingStatus == 0) {
            holder.mBinding.tvOrderPaymentMode.visibility = View.GONE
            if (customerOrderInfoEntity.workingStatus == 7) {
                holder.mBinding.btnCancelOrder.visibility = View.VISIBLE
            } else {
                holder.mBinding.btnCancelOrder.visibility = View.VISIBLE
            }

            /*if (customerOrderInfoEntity.isETAOrderHide) {
                holder.mBinding.btnCancelOrder.visibility = View.VISIBLE
                holder.mBinding.tvOrderPaymentMode.visibility = View.VISIBLE
            } else {
                holder.mBinding.tvOrderPaymentMode.visibility = View.GONE
                if (customerOrderInfoEntity.workingStatus == 7) {
                    holder.mBinding.btnCancelOrder.visibility = View.VISIBLE
                } else {
                    holder.mBinding.btnCancelOrder.visibility = View.VISIBLE
                }
            }*/
        }

       /* if (!customerOrderInfoEntity.isDeliveryCancelledEnable) {
            holder.mBinding.btnCancelOrder.visibility = View.VISIBLE
        }*/
        holder.mBinding.ivDownItem.setOnClickListener {
            if (isShowDetailsList) {
                holder.mBinding.llRvItemDetailList.visibility = View.VISIBLE
                isShowDetailsList = false
                holder.mBinding.ivDownItem.rotation = 180f
            } else {
                viewMoreItemDetails(holder)
            }
        }
        holder.mBinding.itemCheckBox.setOnClickListener {
            orderDetailInterface.checkBoxClicked(
                holder.mBinding.itemCheckBox.isChecked,
                position
            )
        }
        /*holder.mBinding.tvItemId.setOnClickListener {
            holder.mBinding.itemCheckBox.isChecked = !holder.mBinding.itemCheckBox.isChecked
            orderDetailInterface.checkBoxClicked(holder.mBinding.itemCheckBox.isChecked, position)
        }*/
        holder.mBinding.btnCancelOrder.setOnClickListener {
            orderDetailInterface.onButtonClick(
                "Cancel Order", position,
                customerOrderInfoEntity.orderid, customerOrderInfoEntity
            )
        }
        holder.mBinding.btnReDispatch.setOnClickListener {
            orderDetailInterface.onButtonClick(
                "Re Dispatch", position, customerOrderInfoEntity.orderid, customerOrderInfoEntity
            )
        }

        holder.mBinding.btnReAttempt.setOnClickListener {
            Log.e("TAG", "onBindViewHolder: " + customerOrderInfoEntity.toString())
            orderDetailInterface.onButtonClick(
                "Re Attempt", position, customerOrderInfoEntity.orderid, customerOrderInfoEntity
            )
        }

    }

    private fun viewMoreItemDetails(holder: ViewHolder) {
        holder.mBinding.llRvItemDetailList.visibility = View.GONE
        isShowDetailsList = true
        holder.mBinding.ivDownItem.rotation = 0f
    }

    override fun getItemCount(): Int {
        return customerListDcs.size
    }

    inner class ViewHolder(var mBinding: ItemOrderDetailPageBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )
}