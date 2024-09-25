package com.sk.ziladelivery.ui.views.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.sk.ziladelivery.R
import com.sk.ziladelivery.data.model.SelectAllResponceModel
import com.sk.ziladelivery.data.model.UnloadItemListModel
import com.sk.ziladelivery.data.model.UnloadModel
import com.sk.ziladelivery.databinding.ActivityUnloadItemsBinding
import com.sk.ziladelivery.listener.UnloadItemInterface
import com.sk.ziladelivery.ui.views.adapter.UnloadItemAdapter
import com.sk.ziladelivery.ui.views.viewmodels.UnloadItemViewModel
import com.sk.ziladelivery.utilities.*
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class UnloadItemActivity : AppCompatActivity(), UnloadItemInterface, View.OnClickListener {
    private var mBinding: ActivityUnloadItemsBinding? = null
    private var unloadItemViewModel: UnloadItemViewModel? = null
    private var tripPlannerConfirmedDetailId = 0
    private var time: String? = null
    private var type: String? = null
    var unloadItemModel: UnloadModel? = null
    private var handler: Handler? = null
    private var customRunnable: CustomRunnable? = null
    var unloadItemAdapter: UnloadItemAdapter? = null
    private var isPaymentBtVisible = false
    private var filteredlist: ArrayList<UnloadItemListModel>? = null
    private val myModelList = ArrayList<Int>()
    private val sigalUnlodList = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_unload_items)
        unloadItemViewModel = ViewModelProvider(this)[UnloadItemViewModel::class.java]
        dataFromIntent
        setActionBarConfiguration()
        callApis()
        initView()
    }

    private fun initView() {

        mBinding!!.btUnloaditem.setOnClickListener(this)
        unloadItemViewModel!!.getUnloadItemeObserver(tripPlannerConfirmedDetailId)
        mBinding!!.rvItemList.layoutManager = LinearLayoutManager(this)
        mBinding!!.etSearchCust.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (unloadItemModel!!.unloadItemList!!.size > 0) {
                    if (s.toString().isNotEmpty()) {
                        filter(s.toString())
                    } else {
                        unloadItemAdapter!!.updateList(unloadItemModel!!.unloadItemList!!)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })
        mBinding!!.checkboxAll.isChecked=true

        mBinding!!.checkboxAll.setOnClickListener {
            if (mBinding!!.checkboxAll.isChecked) {
                myModelList.clear()
                for (item in unloadItemModel!!.unloadItemList!!) {
                    item.isUnloaded = true
                    myModelList.add(item.itemMultiMRPId)
                }
                collectPaymentBt()
                unloadItemAdapter!!.notifyDataSetChanged()
                val model = SelectAllResponceModel(true, tripPlannerConfirmedDetailId, myModelList)
                unloadItemViewModel!!.getCheckUnloadItemeObserver(model)

            } else {
                myModelList.clear()
                for (item in unloadItemModel!!.unloadItemList!!) {
                    item.isUnloaded = false
                    myModelList.add(item.itemMultiMRPId)
                }
                collectPaymentBt()
                unloadItemAdapter!!.notifyDataSetChanged()
                val model = SelectAllResponceModel(false, tripPlannerConfirmedDetailId, myModelList)
                unloadItemViewModel!!.getCheckUnloadItemeObserver(model)
            }


        }
    }

    private fun filter(text: String) {
        filteredlist = ArrayList()
        for (item in unloadItemModel!!.unloadItemList!!) {
            if (item.itemname!!.lowercase(Locale.getDefault())
                    .contains(text.lowercase(Locale.getDefault()))
            ) {
                filteredlist!!.add(item)
            }
        }
        if (filteredlist!!.isEmpty()) {
            unloadItemAdapter!!.updateList(filteredlist!!)
            mBinding!!.llItemNameView.visibility = View.GONE
            mBinding!!.tvNotDataFound.visibility = View.VISIBLE
        } else {
            mBinding!!.tvNotDataFound.visibility = View.GONE
            mBinding!!.llItemNameView.visibility = View.VISIBLE
            unloadItemAdapter!!.updateList(filteredlist!!)
        }
    }

    private fun callApis() {
        unloadItemViewModel!!.unloadItemsDataLiveData.observe(this) { apiResponse: ApiResponse? ->
            apiResponse?.let {
                consumeResponseForStatusUpdate(
                    it
                )
            }
        }
        unloadItemViewModel!!.checkUnloadItemsLiveData.observe(this) { apiResponse: ApiResponse? ->
            apiResponse?.let {
                consumeResponseForCheckUnload(
                    it
                )
            }
        }
    }

    private fun setActionBarConfiguration() {
        if (type == null) {
            mBinding!!.tvTimmer.visibility = View.VISIBLE
            timer(time, mBinding!!.tvTimmer)
        } else {
            mBinding!!.tvTimmer.visibility = View.GONE
        }
        mBinding!!.ivBack.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    OrderDetailActivity::class.java
                ).putExtra("TripPlannerConfirmedDetailId", tripPlannerConfirmedDetailId)
                    .putExtra("time", time)
            )
            finish()
        }
    }

    private val dataFromIntent: Unit
        private get() {
            if (intent.extras != null) {
                time = intent.getStringExtra("time")
                tripPlannerConfirmedDetailId = intent.getIntExtra(Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id, 0)
                type = intent.getStringExtra("type")
            }
        }

    private fun consumeResponseForStatusUpdate(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {
                ProgressDialog.getInstance().show(this)
            }

            Status.SUCCESS -> {
                ProgressDialog.getInstance().dismiss()
                assert(apiResponse.data != null)
                renderSuccessResponseForUnloadItem(apiResponse.data)
            }

            Status.ERROR -> ProgressDialog.getInstance().dismiss()
            else -> {}
        }
    }

    private fun renderSuccessResponseForUnloadItem(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                try {
                    val jsonObject = JSONObject(response.toString())
                    unloadItemModel =
                        Gson().fromJson(jsonObject.toString(), UnloadModel::class.java)
                    if (unloadItemModel!!.unloadItemList!!.size > 0) {
                        unloadItemAdapter =
                            UnloadItemAdapter(this, unloadItemModel!!.unloadItemList!!, this)
                        mBinding!!.rvItemList.adapter = unloadItemAdapter
                        mBinding!!.tvTotolItemCount.text =
                            "Total Item " + unloadItemModel!!.unloadItemTotalModel!!.totalItem
                        mBinding!!.tvQtyCount.text =
                            "Total QTY " + unloadItemModel!!.unloadItemTotalModel!!.totalQty
                        mBinding!!.tvTotalPaybleAmount.text =
                            unloadItemModel!!.unloadItemTotalModel!!.totalAmount.toString()

                        for (i in unloadItemModel!!.unloadItemList!!.indices) {
                            if (unloadItemModel!!.unloadItemList!![i].isUnloaded) {
                                isPaymentBtVisible = true
                            }
                        }
                        // collectPaymentBt();
                        if (isPaymentBtVisible) {
                            mBinding!!.btUnloaditem.background =
                                resources.getDrawable(R.drawable.button_bg_blue)
                            mBinding!!.btUnloaditem.isClickable = true
                            mBinding!!.btUnloaditem.isEnabled = true
                        } else {
                            mBinding!!.btUnloaditem.background =
                                resources.getDrawable(R.drawable.button_bg_drawable)
                            mBinding!!.btUnloaditem.isClickable = false
                            mBinding!!.btUnloaditem.isEnabled = false
                        }
                    } else {
                        //Toast.makeText(this, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
                    }
                    if(mBinding!!.checkboxAll.isChecked){
                        myModelList.clear()
                        if(unloadItemModel != null) {
                            for (item in unloadItemModel!!.unloadItemList!!) {
                                item.isUnloaded = true
                                myModelList.add(item.itemMultiMRPId)
                            }
                            collectPaymentBt()
                            unloadItemAdapter!!.notifyDataSetChanged()
                            val model = SelectAllResponceModel(true, tripPlannerConfirmedDetailId, myModelList)
                            unloadItemViewModel!!.getCheckUnloadItemeObserver(model)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                //  Toast.makeText(this, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun consumeResponseForCheckUnload(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {
                ProgressDialog.getInstance().show(this)
            }

            Status.SUCCESS -> {
                ProgressDialog.getInstance().dismiss()
                renderSuccessResponseForCheckUnloadItem(apiResponse.data)
            }

            Status.ERROR -> ProgressDialog.getInstance().dismiss()
            else -> {}
        }
    }

    private fun renderSuccessResponseForCheckUnloadItem(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                try {

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun collectPaymentBt() {
        var isCheckAll = true
        for (i in unloadItemModel!!.unloadItemList!!.indices) {
            if (!unloadItemModel!!.unloadItemList!![i].isUnloaded) {
                isCheckAll = false
                break
            }
        }
        if (isCheckAll) {
            mBinding!!.btUnloaditem.background = resources.getDrawable(R.drawable.button_bg_blue)
            mBinding!!.btUnloaditem.isClickable = true
            mBinding!!.btUnloaditem.isEnabled = true
        } else {
            mBinding!!.btUnloaditem.background =
                resources.getDrawable(R.drawable.button_bg_drawable)
            mBinding!!.btUnloaditem.isClickable = false
            mBinding!!.btUnloaditem.isEnabled = false
        }
    }

    override fun checkBoxClicked(
        isChecked: Boolean,
        unloadItemListModel: UnloadItemListModel,
        postion: Int
    ) {
        if (isChecked) {
            unloadItemModel!!.unloadItemList!![postion].isUnloaded = true
            sigalUnlodList.clear()
            sigalUnlodList.add(unloadItemListModel.itemMultiMRPId)
            unloadItemViewModel!!.getCheckUnloadItemeObserver(
                SelectAllResponceModel(
                    true,
                    unloadItemListModel.tripPlannerConfirmedDetailId,
                    sigalUnlodList
                )
            )
        } else {
            sigalUnlodList.clear()
            sigalUnlodList.add(unloadItemListModel.itemMultiMRPId)
            unloadItemViewModel!!.getCheckUnloadItemeObserver(
                SelectAllResponceModel(
                    false,
                    unloadItemListModel.tripPlannerConfirmedDetailId,
                    sigalUnlodList
                )
            )

        }
        collectPaymentBt()
        unloadItemAdapter?.notifyDataSetChanged()
    }

    fun timer(time: String?, timer: TextView?) {
        var millse: Long = 0
        try {
            val currMillis = System.currentTimeMillis()
            val sdf1 = SimpleDateFormat(Utils.myFormat, Locale.getDefault())
            sdf1.timeZone = TimeZone.getDefault()
            if (time != null) {
                val startTime = sdf1.parse(time)!!
                val startEpoch = startTime.time
                millse = currMillis - startEpoch
            }
            if (handler == null) {
                handler = Handler()
            }
            if (customRunnable != null) {
                customRunnable = null
            }
            customRunnable = CustomRunnable(handler, timer, 10000)
            handler!!.removeCallbacks(customRunnable!!)
            customRunnable!!.holder = timer
            customRunnable!!.millisUntilFinished = millse //Current time - received time
            handler!!.postDelayed(customRunnable!!, 1000)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.bt_unloaditem -> {
                startActivity(
                    Intent(
                        this,
                        CollectPaymentActivity::class.java
                    ).putExtra(
                        Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id,
                        tripPlannerConfirmedDetailId
                    ).putExtra("time", time)
                )
                finish()
            }
        }
    }
}

