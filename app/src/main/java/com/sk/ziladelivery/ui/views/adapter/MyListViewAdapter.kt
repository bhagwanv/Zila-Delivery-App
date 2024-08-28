package com.sk.ziladelivery.ui.views.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sk.ziladelivery.R
import com.sk.ziladelivery.data.model.MyTripOrderResponseModel
import com.sk.ziladelivery.databinding.MyTripAdapterBinding
import com.sk.ziladelivery.listener.MyPopupViewListener
import com.sk.ziladelivery.ui.views.main.CollectPaymentActivity
import com.sk.ziladelivery.ui.views.main.OrderDetailActivity
import com.sk.ziladelivery.ui.views.main.UnloadItemActivity
import com.sk.ziladelivery.ui.views.fragment.unloadReturnItem.OrderDetailReturnActivity
import com.sk.ziladelivery.ui.views.viewmodels.MyTaskInfo
import com.sk.ziladelivery.utilities.Constant
import com.sk.ziladelivery.utilities.TextUtils
import java.util.*


class MyListViewAdapter(
    private val context: Activity,
    private var orderList: ArrayList<MyTripOrderResponseModel?>,
    private val myPopupViewListener: MyPopupViewListener,
    private val TripPlannerConfirmedDetailIdDashbord: Int,

    ) :
    RecyclerView.Adapter<MyListViewAdapter.ViewHolder>() {


    fun updateList(list: ArrayList<MyTripOrderResponseModel?>) {
        orderList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.my_trip_adapter,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val model = orderList[i]
        holder.mBinding.llTrack.visibility = View.VISIBLE
        val info = MyTaskInfo()
        if (!TextUtils.isNullOrEmpty(orderList[i]!!.skcode)) {
            info.skCode = "Sk Code : " + orderList[i]!!.skcode
        }
        if (!TextUtils.isNullOrEmpty(orderList[i]!!.cRMTags)) {
            info.crmTags = orderList[i]!!.cRMTags
        }
        info.shopName = orderList[i]!!.shopname
        info.isSkip = orderList[i]!!.isSkip
        if (orderList[i]!!.isSkip) {
            holder.mBinding.tvSkipCustomer.visibility = View.VISIBLE
            holder.mBinding.tvSkipCustomer.text = "Skipped"
        }

        if (orderList[i]!!.customerId == 0) {
            holder.mBinding.cardView.visibility = View.GONE
            holder.mBinding.llTrack.visibility = View.GONE
            holder.mBinding.cardViewWare.visibility = View.VISIBLE
        } else {
            holder.mBinding.cardViewWare.visibility = View.GONE
            holder.mBinding.cardView.visibility = View.VISIBLE
            holder.mBinding.llTrack.visibility = View.VISIBLE

            if (!TextUtils.isNullOrEmpty(orderList[i]!!.customeraddress)) {
                info.address = orderList[i]!!.customeraddress.toString()
            }
            if (!TextUtils.isNullOrEmpty(orderList[i]!!.warehouseaddress.toString())) {
                info.warehouseAddress = orderList[i]!!.warehouseaddress.toString()
            }
        }

        if (model!!.isGeneralOrder && model!!.isReturnOrder) {
            holder.mBinding.btnDelivered.text = "Continue"
            holder.mBinding.btnReturn.visibility = View.VISIBLE
            holder.mBinding.btnDelivered.visibility = View.VISIBLE
        } else if (model!!.isReturnOrder) {
            holder.mBinding.btnReturn.visibility = View.VISIBLE
            holder.mBinding.btnDelivered.visibility = View.GONE
        } else if (model!!.isGeneralOrder) {
            holder.mBinding.btnDelivered.text = "Continue"
            holder.mBinding.btnReturn.visibility = View.GONE
            holder.mBinding.btnDelivered.visibility = View.VISIBLE
        } else if (orderList[i]!!.customerTripStatus == 0) {
            holder.mBinding.btnDelivered.text = "Delivered"
            holder.mBinding.btnReturn.visibility = View.GONE
            holder.mBinding.btnDelivered.visibility = View.VISIBLE
        } else {
            holder.mBinding.btnDelivered.text = "Continue"
            holder.mBinding.btnReturn.visibility = View.GONE
            holder.mBinding.btnDelivered.visibility = View.VISIBLE
        }

        if (!model!!.isReturnOrder) {
            holder.mBinding.btnReturn.visibility = View.GONE
        }

        if (!model!!.isGeneralOrder) {
            holder.mBinding.btnDelivered.visibility = View.GONE
        }

        var isComeplted = false
        if (model?.orderlist != null) {
            for (a in model.orderlist!!.indices) {
                if (model.orderlist!![a].status == "Delivered" ||
                    model.orderlist!![a].status == "Delivery Canceled" ||
                    model.orderlist!![a].status == "Delivery Redispatch"
                ) {
                    isComeplted = true
                    break
                }
            }
            if (isComeplted) {
                holder.mBinding.tvCompleteStatus.visibility = View.VISIBLE
            } else {
                holder.mBinding.tvCompleteStatus.visibility = View.GONE
            }
        }

        /*  if (orderList[i]!!.customerTripStatus == 0) {
              holder.mBinding.btnDelivered.text = "Delivered"
          } else {
              holder.mBinding.btnDelivered.text = "Continue"
          }*/

        if (TripPlannerConfirmedDetailIdDashbord == orderList[i]!!.tripPlannerConfirmedDetailId) {
            if (orderList[i]!!.isAnyTripRunning) {
                holder.mBinding.ivPopupMenu.visibility = View.VISIBLE
            }
            if (!orderList[i]!!.isVisible) {
                var isDelivered = false
                if (orderList[i]!!.orderlist != null) {
                    for (a in orderList[i]!!.orderlist!!.indices) {
                        if (orderList[i]!!.orderlist!![a].status == "Delivered" ||
                            orderList[i]!!.orderlist!![a].status == "Delivery Canceled" ||
                            orderList[i]!!.orderlist!![a].status == "Delivery Redispatch"
                        ) {
                            isDelivered = true
                            break
                        }
                    }
                    if (isDelivered) {
                        holder.mBinding.ivPopupMenu.visibility = View.GONE
                    } else {
                        holder.mBinding.ivPopupMenu.visibility = View.VISIBLE
                    }
                }
            } else {
                holder.mBinding.ivPopupMenu.visibility = View.GONE
            }
        } else {
            holder.mBinding.ivPopupMenu.visibility = View.GONE
        }
        if (orderList[i]!!.isNotLastMileTrip && !orderList[i]!!.isAnyTripRunning && !orderList[i]!!.isLocationEnabled && (orderList[i]!!.tripTypeEnum == 1 || orderList[i]!!.tripTypeEnum == 2)) {
            holder.mBinding.ivPopupMenu.visibility = View.VISIBLE
            holder.mBinding.btnDelivered.visibility = View.VISIBLE
        } else {
      //      holder.mBinding.btnDelivered.text = "Continue"
            if (!orderList[i]!!.isNotLastMileTrip && !orderList[i]!!.isLocationEnabled && (orderList[i]!!.tripTypeEnum == 0 || orderList[i]!!.tripTypeEnum == 3)) {
                holder.mBinding.btnDelivered.visibility = View.GONE
                holder.mBinding.btnReturn.visibility = View.GONE
            } else {
                holder.mBinding.btnDelivered.text = "Continue"
            }
        }
        if (orderList[i]!!.isProcess) {
            holder.mBinding.ivPopupMenu.visibility = View.GONE
            holder.mBinding.btnDelivered.visibility = View.GONE
        }

        val Completionhrs = orderList[i]!!.ordercompletiontime / 60
        val Completionmin = orderList[i]!!.ordercompletiontime % 60
        val UnLoadinghrs = orderList[i]!!.unloadingtime / 60
        val UnLoadingmin = orderList[i]!!.unloadingtime % 60
        info.completionTime = "$Completionhrs.$Completionmin Hrs"
        info.unLoadingTime = "$UnLoadinghrs.$UnLoadingmin Hrs"
        info.itemCount = "No.Item: " + orderList[i]!!.noofitems
        holder.bind(info)
        holder.mBinding.rvOrder.layoutManager = LinearLayoutManager(context)

        if (orderList[i]!!.recordingUrl != null) {
            holder.mBinding.tvVoice.visibility = View.VISIBLE
        } else {
            holder.mBinding.tvVoice.visibility = View.GONE
        }
        holder.mBinding.tvVoice.setOnClickListener {
            myPopupViewListener.callVoiceRecoding(
                holder.adapterPosition,
                orderList[holder.adapterPosition],
                holder.mBinding.tvVoice,
                holder.mBinding.imPlayIcon
            )
        }

        holder.mBinding.ivPopupMenu.setOnClickListener {
            myPopupViewListener.callPopupClicked(
                holder.adapterPosition,
                orderList[holder.adapterPosition], orderList[i]!!.orderlist
            )
        }
        holder.mBinding.btnDelivered.setOnClickListener {
            if (orderList[i]!!.customerTripStatus == Constant.UNLOADING) {
                unloading(orderList[i]!!.tripPlannerConfirmedDetailId)
            } else if (orderList[i]!!.customerTripStatus == Constant.REACHED_DISTINATION) {
                reachedDistination(orderList[i]!!.tripPlannerConfirmedDetailId)
            } else if (orderList[i]!!.customerTripStatus == Constant.COLLECTING_PAYMENT) {
                collectingPayment(orderList[i]!!.tripPlannerConfirmedDetailId)
            } else if (orderList[i]!!.customerTripStatus == Constant.VERIFYING_OTP) {
                verifyingOTP(orderList[i]!!.tripPlannerConfirmedDetailId)
            } else if (orderList[i]!!.customerTripStatus == Constant.NOTIFY_DELIVERY_CANCELLED) {
                notiyDeliveryCancle(orderList[i]!!.tripPlannerConfirmedDetailId)
            } else if (orderList[i]!!.customerTripStatus == Constant.REDISPATCH_ANNORDER_CANCEL_VERIFYINN_OTP) {
                redispatchAnoderCancleVerifyingOTP(orderList[i]!!.tripPlannerConfirmedDetailId)
            } else if (orderList[i]!!.customerTripStatus == Constant.PENDING) {
                penddingStatus(orderList[i]!!.tripPlannerConfirmedDetailId)
            } else if (orderList[i]!!.customerTripStatus == Constant.REATTEMPT_TRIP) {
                reAttemptVerifyingOTP((orderList[i]!!.tripPlannerConfirmedDetailId))
            }
        }

        holder.mBinding.btnReturn.setOnClickListener {
            unloadReturn(orderList[i]!!.tripPlannerConfirmedDetailId)
        }

        if (orderList.size > 0) {
            val myTripOrderAdapter =
                MyTripOrderAdapter(context, orderList[i]!!.orderlist)
            holder.mBinding.rvOrder.adapter = myTripOrderAdapter
        }
    }

    private fun redispatchAnoderCancleVerifyingOTP(tripPlannerConfirmedDetailId: Int) {
        val intent = Intent(context, OrderDetailActivity::class.java)
        intent.putExtra(
            "NotifyDeliveryCancelled",
            Constant.REDISPATCH_ANNORDER_CANCEL_VERIFYINN_OTP
        )
        intent.putExtra(
            Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id,
            tripPlannerConfirmedDetailId
        )
        context.startActivity(intent)
        context.finish()
    }

    private fun reAttemptVerifyingOTP(tripPlannerConfirmedDetailId: Int) {
        val intent = Intent(context, OrderDetailActivity::class.java)
        intent.putExtra(
            Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id,
            tripPlannerConfirmedDetailId
        )
        context.startActivity(intent)
        context.finish()
    }

    private fun penddingStatus(tripPlannerConfirmedDetailId: Int) {
        val intent = Intent(context, OrderDetailActivity::class.java)
        intent.putExtra(
            Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id,
            tripPlannerConfirmedDetailId
        )
        context.startActivity(intent)
        context.finish()
    }

    private fun notiyDeliveryCancle(tripPlannerConfirmedDetailId: Int) {
        val intent = Intent(context, OrderDetailActivity::class.java)
        intent.putExtra("NotifyDeliveryCancelled", Constant.NOTIFY_DELIVERY_CANCELLED)
        intent.putExtra(
            Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id,
            tripPlannerConfirmedDetailId
        )
        context.startActivity(intent)
        context.finish()
    }

    private fun verifyingOTP(tripPlannerConfirmedDetailId: Int) {
        val intent = Intent(context, CollectPaymentActivity::class.java)
        intent.putExtra("CustomerTripStatus", Constant.VERIFYING_OTP)
        intent.putExtra(
            Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id,
            tripPlannerConfirmedDetailId
        )
        context.startActivity(intent)
        context.finish()
    }

    private fun collectingPayment(tripPlannerConfirmedDetailId: Int) {
        val intent = Intent(context, CollectPaymentActivity::class.java)
        intent.putExtra(
            Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id,
            tripPlannerConfirmedDetailId
        )
        context.startActivity(intent)
        context.finish()
    }

    private fun reachedDistination(tripPlannerConfirmedDetailId: Int) {
        val intent = Intent(context, OrderDetailActivity::class.java)
        intent.putExtra(
            Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id,
            tripPlannerConfirmedDetailId
        )
        context.startActivity(intent)
        context.finish()
    }

    private fun unloading(tripPlannerConfirmedDetailId: Int) {
        val intent = Intent(context, UnloadItemActivity::class.java)
        intent.putExtra(
            Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id,
            tripPlannerConfirmedDetailId
        )
        context.startActivity(intent)
        context.finish()
    }

    private fun unloadReturn(tripPlannerConfirmedDetailId: Int) {
        val intent = Intent(context, OrderDetailReturnActivity::class.java)
        intent.putExtra(
            Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id,
            tripPlannerConfirmedDetailId
        )
        context.startActivity(intent)
        context.finish()
    }

    override fun getItemCount(): Int {
        return if (orderList == null) 0 else orderList!!.size
    }

    inner class ViewHolder(var mBinding: MyTripAdapterBinding) : RecyclerView.ViewHolder(
        mBinding.root
    ) {
        fun bind(item: MyTaskInfo?) {
            mBinding.myTaskInfo = item
            mBinding.executePendingBindings()
        }
    }

}