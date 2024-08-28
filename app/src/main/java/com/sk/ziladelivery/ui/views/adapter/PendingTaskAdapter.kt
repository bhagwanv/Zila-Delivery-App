package com.sk.ziladelivery.ui.views.adapter

import android.content.Context
import com.sk.ziladelivery.data.model.DashBoardResponseModel.AssignmentlistEntity
import com.sk.ziladelivery.ui.views.fragment.DashBoardFragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import com.sk.ziladelivery.listener.NewAcceptRejectAssignmenClick
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sk.ziladelivery.R
import com.sk.ziladelivery.databinding.ItemPeddingListBinding
import com.sk.ziladelivery.utilities.Utils
import com.sk.ziladelivery.ui.views.viewmodels.PendingTaskModel
import java.util.ArrayList

class PendingTaskAdapter(
    private val context: Context,
    private val pendingDetailsList: ArrayList<AssignmentlistEntity>?,
    acceptInterface: DashBoardFragment
) : RecyclerView.Adapter<PendingTaskAdapter.ViewHolder>() {
    private var layoutInflater: LayoutInflater? = null
    private val onClickValue = true
    private val isShortItemClick = true
    private val acceptInterface: NewAcceptRejectAssignmenClick
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.context)
        }
        return ViewHolder(
            DataBindingUtil.inflate<ItemPeddingListBinding>(
                (layoutInflater)!!, R.layout.item_pedding_list, viewGroup, false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.mBinding.tvAssitId.text =
            pendingDetailsList!!.get(i).assignmentid.toString() + ""
        viewHolder.mBinding.tvTime.text = Utils.getDateFormat(
            pendingDetailsList.get(i).createdate
        )
        val amountRounded = Math.round(pendingDetailsList[i].amount * 100).toDouble() / 100
        viewHolder.mBinding.tvAmount.text = "Amount $amountRounded"
        viewHolder.mBinding.tvNoOrder.text =
            "No.Order " + Math.round(pendingDetailsList.get(i).nooforder.toFloat())
        if (!pendingDetailsList[i].assignmentStatus.equals("Assigned", ignoreCase = true)) {
            viewHolder.mBinding.tvStatus.visibility = View.VISIBLE
            viewHolder.mBinding.tvStatus.text = pendingDetailsList.get(i).assignmentStatus
            viewHolder.mBinding.llAccept.visibility = View.GONE
            viewHolder.mBinding.llReject.visibility = View.GONE
        } else {
            viewHolder.mBinding.tvStatus.visibility = View.GONE
            viewHolder.mBinding.llAccept.visibility = View.VISIBLE
            viewHolder.mBinding.llReject.visibility = View.GONE
        }
        viewHolder.mBinding.llAccept.setOnClickListener(View.OnClickListener {
            acceptInterface.acceptClicked(
                pendingDetailsList[i].assignmentid, "true"
            )
        })
        viewHolder.mBinding.llReject.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                acceptInterface.rejectClicked(pendingDetailsList[i].assignmentid, "flase")
            }
        })
        viewHolder.mBinding.tvView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                acceptInterface.viewAssignmentClicked(pendingDetailsList[i].assignmentid)
            }
        })
    }

    override fun getItemCount(): Int {
        return if (pendingDetailsList != null) pendingDetailsList.size else 0
    }

    inner class ViewHolder(var mBinding: ItemPeddingListBinding) : RecyclerView.ViewHolder(
        mBinding.root
    ) {
        var orderListRV: RecyclerView? = null
        fun bind(item: PendingTaskModel?) {
            mBinding.pendingTaskModel = item
            mBinding.executePendingBindings()
        }
    }

    init {
        this.acceptInterface = acceptInterface
    }
}