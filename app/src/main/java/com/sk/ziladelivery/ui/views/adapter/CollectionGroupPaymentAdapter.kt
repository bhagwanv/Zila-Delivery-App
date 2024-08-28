package com.sk.ziladelivery.ui.views.adapter
import android.app.Activity
import com.sk.ziladelivery.data.model.CollectionPaymentModel.PaymentGroupwisesListModel
import com.sk.ziladelivery.listener.CollectPaymentInterface
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.sk.ziladelivery.R
import com.sk.ziladelivery.databinding.ItemCollectGroupBinding
import java.util.ArrayList

class CollectionGroupPaymentAdapter(
    private val context: Activity,
    private var groupwisesListModels: ArrayList<PaymentGroupwisesListModel>,
    private val orderDetailInterface: CollectPaymentInterface
) : RecyclerView.Adapter<CollectionGroupPaymentAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(
                    parent.context
                ), R.layout.item_collect_group, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = groupwisesListModels[position]
        if (model.paymentMode != null) {
            holder.mBinding.tvPaymentMode.text = model.paymentMode
            holder.mBinding.llButtonPayment.isClickable = false
            holder.mBinding.llButtonPayment.background = ContextCompat.getDrawable(context, R.drawable.button_border_line_green)
            holder.mBinding.tvPaymentMode.setTextColor(ContextCompat.getColor(context,R.color.green))
            holder.mBinding.btGreenCheck.background =ContextCompat.getDrawable(context, R.drawable.ic_green_bt)
        } else {
            holder.mBinding.tvPaymentMode.text = "Select Payment Option"
            holder.mBinding.llButtonPayment.isClickable = true
            holder.mBinding.tvPaymentMode.setTextColor(ContextCompat.getColor(context,R.color.gray))
            holder.mBinding.btGreenCheck.background = ContextCompat.getDrawable(context,R.drawable.ic_green_bt_gray)
            holder.mBinding.llButtonPayment.background = ContextCompat.getDrawable(context,R.drawable.button_border_line_gray)
            holder.mBinding.llButtonPayment.setOnClickListener {
                orderDetailInterface.selectPaymentType(
                    holder.adapterPosition,
                    model
                )
            }
        }
        val collectPayemtAdapter = CollectPayemtAdapter(
            context,
            model.customerorderinfo!!,
            orderDetailInterface,
            position,
            model.paymentMode
        )
        holder.mBinding.rvCollectionPayment.adapter = collectPayemtAdapter
    }

    override fun getItemCount(): Int {
        return groupwisesListModels.size
    }

    inner class ViewHolder(var mBinding: ItemCollectGroupBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )
}