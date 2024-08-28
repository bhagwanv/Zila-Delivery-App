package com.sk.ziladelivery.ui.views.adapter

import android.app.Activity
import com.sk.ziladelivery.data.model.MyTaskModel
import com.sk.ziladelivery.listener.orderdetailClick
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sk.ziladelivery.R
import com.sk.ziladelivery.ui.views.viewmodels.MyTaskInfo
import com.sk.ziladelivery.listener.MyTaskListener
import android.content.Intent
import android.net.Uri
import com.sk.ziladelivery.ui.views.main.MainActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.sk.ziladelivery.ui.views.main.NewOrderPlaceActivity
import com.google.android.gms.maps.model.LatLng
import com.sk.ziladelivery.utilities.OrderCustomRunnable
import android.widget.TextView
import com.sk.ziladelivery.databinding.AssignmentDetailAdapterBinding
import com.sk.ziladelivery.utilities.DateUtils
import com.sk.ziladelivery.utilities.Utils
import java.lang.Exception
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MyTaskAdapter(
    private var context: Activity,
    private var myTaskList: ArrayList<MyTaskModel>?,
    private var type: String?,
    time: String?,
    private val orderdetailClick: orderdetailClick
) : RecyclerView.Adapter<MyTaskAdapter.ViewHolder>() {
    private var layoutInflater: LayoutInflater? = null
    private var time: String? = null
    fun setData(
        context: Activity,
        myTaskList: ArrayList<MyTaskModel>?,
        type: String?,
        time: String?
    ) {
        this.context = context
        this.myTaskList = myTaskList
        this.time = time
        this.type = type
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        return ViewHolder(
            DataBindingUtil.inflate<AssignmentDetailAdapterBinding>(
                layoutInflater!!, R.layout.assignment_detail_adapter, parent, false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        try {
            val myTaskModel = myTaskList!![viewHolder.adapterPosition]
            val info = MyTaskInfo()
            info.customerName = myTaskModel.customerName
            viewHolder.mBinding.llTrack.visibility = View.GONE
            viewHolder.mBinding.llAddress.visibility = View.VISIBLE
            if (type != null && type.equals("rejectAssign", ignoreCase = true)) {
                viewHolder.mBinding.llOrderDetail.visibility = View.VISIBLE
            } else {
                viewHolder.mBinding.llOrderDetail.visibility = View.GONE
            }

            info.orderId = myTaskModel.orderId.toString() + ""
            info.skCode = "SkCode: " + myTaskModel.skcode
            if (myTaskModel.createdDate != null && !myTaskModel.createdDate.isEmpty()) {
                info.date = DateUtils.getDateFormat(myTaskModel.createdDate)
            }
            info.shopName = myTaskModel.shopName
            info.address = myTaskModel.shippingAddress
            info.totalAmount =
                context.getString(R.string.rs) + " " + DecimalFormat("##.##").format(myTaskModel.totalAmount)
            info.itemCount = context.getString(R.string.item_no) + myTaskModel.orderDetailsCount
            viewHolder.bind(info)
            if (myTaskModel.orderDate != null) {
                try {
                    val currMillis = System.currentTimeMillis()
                    val sdf1 = SimpleDateFormat(Utils.myFormat, Locale.getDefault())
                    sdf1.timeZone = TimeZone.getDefault()
                    val c = Calendar.getInstance()
                    val startTime = sdf1.parse(myTaskModel.orderDate)
                    val startEpoch = startTime.time
                    c.time = startTime
                    c.add(Calendar.HOUR, 48)
                    val date = c.time
                    val endEpoch = date.time
                    println("endEpoch$endEpoch")
                    println("startEpoch$startEpoch")
                    println("startTime$startTime")
                    println("time$startTime")
                    val millse = endEpoch - currMillis
                    println("millse$millse")
                    //  viewHolder.timer(millse, viewHolder.timer);
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }
             viewHolder.mBinding.myTaskListener = object : MyTaskListener {
                override fun taskViewClicked() {
                    if (!Utils.checkInternetConnection(context)) {
                        Utils.setToast(context, context.resources.getString(R.string.network_error))
                    } else {
                        orderdetailClick.onlineLayHideUnHideClicked(true)
                       if (type != null && type.equals(
                                "rejectAssign",
                                ignoreCase = true
                            )
                        ) {
                            val intent = Intent(context, MainActivity::class.java)
                            val bundle = Bundle()
                            intent.putExtra("ORDER_ID", myTaskModel.orderId)
                            intent.putExtra("assignmentID", myTaskModel.deliveryIssuanceId)
                            intent.putExtra("SkCode", myTaskModel.skcode)
                            intent.putExtra("lat", myTaskModel.lat)
                            intent.putExtra("lg", myTaskModel.lg)
                            intent.putExtra("position", viewHolder.adapterPosition)
                            intent.putExtra("time", time)
                            intent.putExtra("CUSTOMER_NAME", myTaskModel.customerName)
                            intent.putExtra("MOBILE_NUMBER", myTaskModel.customerphonenum)
                            intent.putExtra("SHIPPING_ADDRESS", myTaskModel.shippingAddress)
                            intent.putExtra("Customerid", myTaskModel.customerId)
                            intent.putExtra("colorCode", myTaskModel.colorCode)
                            intent.putExtra("type", type)
                            context.startActivity(intent)
                        } else {
                            val intent = Intent(context, NewOrderPlaceActivity::class.java)
                            val bundle = Bundle()
                            intent.putExtra("ORDER_ID", myTaskModel.orderId)
                            intent.putExtra("assignmentID", myTaskModel.deliveryIssuanceId)
                            intent.putExtra("SkCode", myTaskModel.skcode)
                            intent.putExtra("lat", myTaskModel.lat)
                            intent.putExtra("lg", myTaskModel.lg)
                            intent.putExtra("position", viewHolder.adapterPosition)
                            intent.putExtra("time", time)
                            intent.putExtra("CUSTOMER_NAME", myTaskModel.customerName)
                            intent.putExtra("MOBILE_NUMBER", myTaskModel.customerphonenum)
                            intent.putExtra("SHIPPING_ADDRESS", myTaskModel.shippingAddress)
                            intent.putExtra("Customerid", myTaskModel.customerId)
                            intent.putExtra("colorCode", myTaskModel.colorCode)
                            intent.putExtra("type", type)
                            context.startActivity(intent)
                        }
                    }
                }

                override fun callBtnClicked() {
                    val intent = Intent(
                        Intent.ACTION_DIAL,
                        Uri.parse("tel:" + "+91" + myTaskModel.customerphonenum)
                    )
                    context.startActivity(intent)
                }

                override fun locationBtnClicked() {
                    try {
                        if (!Utils.checkInternetConnection(context)) {
                            Utils.setToast(
                                context,
                                context.resources.getString(R.string.network_error)
                            )
                        } else {
                            val latlong = LatLng(myTaskModel.lat, myTaskModel.lg)
                            if (myTaskModel.lat != 0.0 && myTaskModel.lg != 0.0) {
                                val navigation = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("google.navigation:q=" + myTaskModel.lat + "," + myTaskModel.lg)
                                )
                                context.startActivity(navigation)
                            } else {
                                val navigation = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("google.navigation:q=" + latlong.latitude + "," + latlong.longitude)
                                )
                                context.startActivity(navigation)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return if (myTaskList == null) 0 else myTaskList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    inner class ViewHolder(var mBinding: AssignmentDetailAdapterBinding) : RecyclerView.ViewHolder(
        mBinding.root
    ) {
        private val orderCustomRunnable: OrderCustomRunnable? = null
        var handler = Handler()
        var timer: TextView? = null
        fun timer(millse: Long, tvtimer: TextView?) {
            try {
                handler.removeCallbacks(orderCustomRunnable!!)
                orderCustomRunnable.holder = tvtimer
                orderCustomRunnable.millisUntilFinished = millse //Current time - received time
                handler.postDelayed(orderCustomRunnable, 1000)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun bind(item: MyTaskInfo?) {
            mBinding.myTaskInfo = item
            mBinding.executePendingBindings()
        }
    }

    init {
        notifyDataSetChanged()
    }
}