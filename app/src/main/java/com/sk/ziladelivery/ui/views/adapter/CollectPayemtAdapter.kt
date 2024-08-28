package com.sk.ziladelivery.ui.views.adapter
import android.app.Activity
import com.sk.ziladelivery.listener.CollectPaymentInterface
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sk.ziladelivery.R
import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.sk.ziladelivery.databinding.ItemCollectPaymentBinding
import com.sk.ziladelivery.data.model.CollectionPaymentModel
import com.sk.ziladelivery.utilities.TextUtils
import java.util.ArrayList

class CollectPayemtAdapter(private val context: Activity, var customerListDcs: ArrayList<CollectionPaymentModel.CustomerorderinfoEntity>, private val orderDetailInterface: CollectPaymentInterface, private val mainPosition: Int, private val paymentMode: String?) : RecyclerView.Adapter<CollectPayemtAdapter.ViewHolder>() {
    private var layoutInflater: LayoutInflater? = null
    private var isShowDetailsList = true
    private var isPaymentDone = true
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        return ViewHolder(
            DataBindingUtil.inflate(layoutInflater!!, R.layout.item_collect_payment, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val customerorderinfoEntity = customerListDcs[position]
        if (!TextUtils.isNullOrEmpty(paymentMode)) {
            holder.mBinding.tvRemoveOrder.visibility = View.VISIBLE
            isPaymentDone = true
        } else {
            holder.mBinding.tvRemoveOrder.visibility = View.VISIBLE
            isPaymentDone = false
        }
        holder.mBinding.itemCheckBox.isEnabled = !(paymentMode != null && paymentMode.equals("Payment Done", ignoreCase = true))

        holder.mBinding.tvItemId.text = "ID: " + customerorderinfoEntity.orderid
        holder.mBinding.tvOrderStatus.text = customerorderinfoEntity.status
        holder.mBinding.tvAmount.text =
            java.lang.String.valueOf(customerorderinfoEntity.remaningOrderAmount)
        holder.mBinding.tvTotalGrossAmountCollectPayment.text =
            java.lang.String.valueOf(customerorderinfoEntity.grossamount)
        holder.mBinding.rvItemDetailList.layoutManager = LinearLayoutManager(context)
        val orderDetailsAdapter =
            CollectChildItemAdapter(context, customerorderinfoEntity.orderDetailsModel!!)
        holder.mBinding.rvItemDetailList.adapter = orderDetailsAdapter

        holder.mBinding.itemCheckBox.isChecked = customerorderinfoEntity.isBoolWorkingStatus
        if (!TextUtils.isNullOrEmpty(customerorderinfoEntity.paymentFrom)) {
            holder.mBinding.tvRemoveOrder.visibility = View.GONE
        }
        holder.mBinding.ivDownItem.setOnClickListener {
            if (isShowDetailsList) {
                holder.mBinding.llRvItemDetailList.visibility = View.VISIBLE
                isShowDetailsList = false
            } else {
                holder.mBinding.llRvItemDetailList.visibility = View.GONE
                isShowDetailsList = true
            }
        }
        holder.mBinding.itemCheckBox.setOnClickListener {
            if (holder.mBinding.itemCheckBox.isChecked) {
                orderDetailInterface.checkBoxClicked(
                    true,
                    holder.adapterPosition,
                    mainPosition,
                    this@CollectPayemtAdapter
                )
            } else {
                orderDetailInterface.checkBoxClicked(
                    false,
                    holder.adapterPosition,
                    mainPosition,
                    this@CollectPayemtAdapter
                )
            }
        }
        holder.mBinding.tvRemoveOrder.setOnClickListener {
            orderDetailInterface.removeOrder(
                customerorderinfoEntity,
                position,isPaymentDone
            )
        }
    }

    override fun getItemCount(): Int {
        return customerListDcs.size
    }

    inner class ViewHolder(var mBinding: ItemCollectPaymentBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )
}