package com.sk.ziladelivery.ui.views.main

import androidx.appcompat.app.AppCompatActivity
import com.sk.ziladelivery.ui.views.viewmodels.CustomerRattingViewModel
import com.sk.ziladelivery.data.model.RatingModel
import com.sk.ziladelivery.data.model.CustomerRattingModel
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.sk.ziladelivery.R
import androidx.lifecycle.ViewModelProviders
import com.sk.ziladelivery.data.model.UserRatingDetailDc
import android.content.Intent
import android.view.View
import android.widget.TextView
import com.google.android.flexbox.FlexboxLayout
import android.view.ViewGroup
import android.widget.ImageView
import com.sk.ziladelivery.databinding.ActivityCustomerRatingBinding
import com.sk.ziladelivery.utilities.Constant
import com.sk.ziladelivery.utilities.ProgressDialog
import com.sk.ziladelivery.utilities.TextUtils
import com.sk.ziladelivery.utilities.Utils
import com.squareup.picasso.Picasso
import java.util.ArrayList

class CustomerRatingActivity : AppCompatActivity() {
    var mBinding: ActivityCustomerRatingBinding? = null
    private var customerRattingViewModel: CustomerRattingViewModel? = null
    private var agentVisit = ""
    private var ratingList: ArrayList<RatingModel>? = null
    private var ivFace1: ImageView? = null
    private var ivFace2: ImageView? = null
    private var ivFace3: ImageView? = null
    private var ivFace4: ImageView? = null
    private var ivFace5: ImageView? = null
    private var clickedPos = -1
    var ratingModels: CustomerRattingModel? = null
    private var TripPlannerConfirmedDetailId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_customer_rating)
        customerRattingViewModel = ViewModelProviders.of(this).get(
            CustomerRattingViewModel::class.java
        )
        mBinding!!.setRatingViewModel(customerRattingViewModel)
        initView()
    }

    private fun clickEvet() {
        ivFace1!!.setOnClickListener { v: View? ->
            clickedPos = 0
            ivFace1!!.setImageResource(R.drawable.ic_face_o1)
            ivFace2!!.setImageResource(R.drawable.ic_face2)
            ivFace3!!.setImageResource(R.drawable.ic_face3)
            ivFace4!!.setImageResource(R.drawable.ic_face4)
            ivFace5!!.setImageResource(R.drawable.ic_face5)
            updateViewsM(mBinding!!.fbAgent, 0)
        }
        ivFace2!!.setOnClickListener { v: View? ->
            clickedPos = 1
            ivFace2!!.setImageResource(R.drawable.ic_face_o2)
            ivFace1!!.setImageResource(R.drawable.ic_face1)
            ivFace3!!.setImageResource(R.drawable.ic_face3)
            ivFace4!!.setImageResource(R.drawable.ic_face4)
            ivFace5!!.setImageResource(R.drawable.ic_face5)
            updateViewsM(mBinding!!.fbAgent, 1)
        }
        ivFace3!!.setOnClickListener { v: View? ->
            clickedPos = 2
            ivFace3!!.setImageResource(R.drawable.ic_face_o3)
            ivFace1!!.setImageResource(R.drawable.ic_face1)
            ivFace2!!.setImageResource(R.drawable.ic_face2)
            ivFace4!!.setImageResource(R.drawable.ic_face4)
            ivFace5!!.setImageResource(R.drawable.ic_face5)
            updateViewsM(mBinding!!.fbAgent, 2)
        }
        ivFace4!!.setOnClickListener { v: View? ->
            clickedPos = 3
            ivFace4!!.setImageResource(R.drawable.ic_face_o4)
            ivFace1!!.setImageResource(R.drawable.ic_face1)
            ivFace2!!.setImageResource(R.drawable.ic_face2)
            ivFace3!!.setImageResource(R.drawable.ic_face3)
            ivFace5!!.setImageResource(R.drawable.ic_face5)
            updateViewsM(mBinding!!.fbAgent, 3)
        }
        ivFace5!!.setOnClickListener { v: View? ->
            clickedPos = 4
            ivFace5!!.setImageResource(R.drawable.ic_face_o5)
            ivFace1!!.setImageResource(R.drawable.ic_face1)
            ivFace2!!.setImageResource(R.drawable.ic_face2)
            ivFace3!!.setImageResource(R.drawable.ic_face3)
            ivFace4!!.setImageResource(R.drawable.ic_face4)
            updateViewsM(mBinding!!.fbAgent, 4)
        }
        mBinding!!.btnSubmit.setOnClickListener { v: View? ->
            if (clickedPos == -1) {
                Utils.setToast(this@CustomerRatingActivity, "Select rate Customer")
            }  else {
                if (ratingList!!.size > 0) {
                    val dcList = ArrayList<UserRatingDetailDc>()
                    for (detailDc in ratingList!![clickedPos].getRatingDetails()) {
                        if (detailDc.isSelect) {
                            dcList.add(detailDc)
                        }
                    }
                    if (dcList.size == 0) {
                        Utils.setToast(applicationContext, "Please Select Ratting")
                    } else {
//                        Utils.showProgressDialog(this)
                        ratingList!![clickedPos].rating = clickedPos + 1
                        ratingList!![clickedPos].shopVisited = agentVisit
                        ratingList!![clickedPos].userId =
                            ratingModels!!.deliveryDboyRatingOrderModel.customerId
                        ratingList!![clickedPos].orderId = TripPlannerConfirmedDetailId
                        ratingList!![clickedPos].setRatingDetails(dcList)
                        customerRattingViewModel!!.getSubmitRattingLiveData(ratingList!![clickedPos])
                            .observe(this) { aBoolean: Boolean? ->
                                startActivity(Intent(applicationContext, MainActivity::class.java))
                                finishAffinity()
                                Utils.hideProgressDialog(this@CustomerRatingActivity)
                            }
                    }
                }
            }
        }
    }

    private fun initView() {
        TripPlannerConfirmedDetailId =
            intent.getIntExtra(Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id, 0)
        ivFace1 = mBinding!!.ivFace1
        ivFace2 = mBinding!!.ivFace2
        ivFace3 = mBinding!!.ivFace3
        ivFace4 = mBinding!!.ivFace4
        ivFace5 = mBinding!!.ivFace5
        val timeArray = resources.getStringArray(R.array.salesman_times)
        mBinding!!.fbVisit.removeAllViews()
        val viewList: MutableList<TextView> = ArrayList()
        for (i in timeArray.indices) {
            val params = FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(15, 10, 15, 10)
            val textView = TextView(this)
            textView.layoutParams = params
            textView.background = resources.getDrawable(R.drawable.rectangle_grey)
            textView.setPadding(40, 20, 40, 20)
            textView.setTextColor(resources.getColor(R.color.drak_text_color))
            textView.text = "" + timeArray[i]
            viewList.add(textView)
            textView.setOnClickListener { view: View ->
                agentVisit = textView.text.toString()
                for (view1 in viewList) {
                    view1.background = resources.getDrawable(R.drawable.rectangle_grey)
                    view1.setTextColor(resources.getColor(R.color.drak_text_color))
                }
                textView.setTextColor(resources.getColor(R.color.colorLightBlueHeader))
                view.background = resources.getDrawable(R.drawable.rectangle_orange)
            }
            if (i == 0) {
                textView.setTextColor(resources.getColor(R.color.colorLightBlueHeader))
                textView.background = resources.getDrawable(R.drawable.rectangle_orange)
                agentVisit = timeArray[i]
            } else {
                textView.setTextColor(resources.getColor(R.color.drak_text_color))
            }
            mBinding!!.fbVisit.addView(textView)
        }
        if (!Utils.checkInternetConnection(this@CustomerRatingActivity)) {
            Utils.setToast(applicationContext, resources.getString(R.string.network_error))
        } else {
            callRattingApi(TripPlannerConfirmedDetailId)
        }
        clickEvet()
    }

    private fun callRattingApi(i: Int) {
        ProgressDialog.getInstance().show(this)
        customerRattingViewModel!!.getRatting(i)
            .observe(this) { ratingModel: CustomerRattingModel? ->
                ProgressDialog.getInstance().dismiss()
                if (ratingModel != null) {
                    ratingModels = ratingModel
                    if (ratingModels!!.deliveryDboyRatingOrderModel != null) {
                        if (!TextUtils.isNullOrEmpty(ratingModels!!.deliveryDboyRatingOrderModel.shopimage)) {
                            Picasso.get()
                                .load(ratingModels!!.deliveryDboyRatingOrderModel.shopimage)
                                .placeholder(R.drawable.ic_baseline_account_circle_24)
                                .error(R.drawable.profile_round)
                                .into(mBinding!!.ivImage)
                        }
                        mBinding!!.tvheadername.text =
                            ratingModels!!.deliveryDboyRatingOrderModel.shopName
                        mBinding!!.tvName.text =
                            ratingModels!!.deliveryDboyRatingOrderModel.shopName
                        mBinding!!.tvShopname.text =
                            ratingModels!!.deliveryDboyRatingOrderModel.shippingAddress
                    }
                    ratingList = ArrayList()
                    ratingList!!.addAll(ratingModels!!.ratingModels)
                    updateViewsM(mBinding!!.fbAgent, 0)
                }
            }
    }

    private fun updateViewsM(flexboxLayout: FlexboxLayout, pos: Int) {
        flexboxLayout.removeAllViews()
        if (ratingList != null && ratingList!!.size > 0) {
            val viewList: MutableList<TextView> = ArrayList()
            for (i in ratingList!![pos].getRatingDetails().indices) {
                val params = FlexboxLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(15, 10, 15, 10)
                val textView = TextView(this)
                textView.id = i
                textView.layoutParams = params
                textView.background = resources.getDrawable(R.drawable.rectangle_grey)
                textView.setPadding(40, 20, 40, 20)
                textView.text = "" + ratingList!![pos].getRatingDetails()[i].detail
                textView.tag = 0
                textView.setTextColor(resources.getColor(R.color.drak_text_color))
                viewList.add(textView)
                textView.setOnClickListener { v: View ->
                    viewList[textView.id].tag = if (v.tag as Int == 0) 1 else 0
                    for (view1 in viewList) {
                        if (view1.tag as Int == 1) {
                            view1.setTextColor(resources.getColor(R.color.colorLightBlueHeader))
                            ratingList!![pos].getRatingDetails()[view1.id].isSelect = true
                            view1.background = resources.getDrawable(R.drawable.rectangle_orange)
                        } else {
                            view1.setTextColor(resources.getColor(R.color.drak_text_color))
                            ratingList!![pos].getRatingDetails()[view1.id].isSelect = false
                            view1.background = resources.getDrawable(R.drawable.rectangle_grey)
                        }
                    }
                }
                flexboxLayout.addView(textView)
            }
        }
    }
}