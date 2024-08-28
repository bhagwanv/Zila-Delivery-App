package com.sk.ziladelivery.ui.views.main

import androidx.appcompat.app.AppCompatActivity
import com.sk.ziladelivery.ui.views.viewmodels.ProductDetailsViewModel
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.sk.ziladelivery.R
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sk.ziladelivery.utilities.ApiResponse
import com.google.gson.JsonElement
import org.json.JSONObject
import com.sk.ziladelivery.data.model.OrderDetailsModel
import com.google.gson.Gson
import com.sk.ziladelivery.ui.views.adapter.ProductItemAdapter
import android.widget.Toast
import com.sk.ziladelivery.databinding.ActivityProductDetailsBinding
import com.sk.ziladelivery.utilities.ProgressDialog
import com.sk.ziladelivery.utilities.Status
import com.sk.ziladelivery.utilities.Utils
import java.lang.Exception

class ProductDetailsActivity : AppCompatActivity() {
    private var mBinding: ActivityProductDetailsBinding? = null
    private var productDetailsViewModel: ProductDetailsViewModel? = null
    private var orderId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_details)
        productDetailsViewModel = ViewModelProviders.of(this).get(
            ProductDetailsViewModel::class.java
        )
        mBinding!!.setOrderDetails(productDetailsViewModel)
        dataFromIntent
        setActionBarConfiguration()
        callApis()
        initView()
    }

    private fun initView() {
        productDetailsViewModel!!.GetProductDetailsObserverApi(orderId)
        mBinding!!.rvItemList.layoutManager = LinearLayoutManager(this)
    }

    private fun callApis() {
        productDetailsViewModel!!.productItems.observe(this) { apiResponse: ApiResponse? ->
            apiResponse?.let {
                consumeResponseForStatusUpdate(
                    it
                )
            }
        }
    }

    private fun setActionBarConfiguration() {
        mBinding!!.tvActionBar.tvOrderno.text = "Order Details"
        mBinding!!.tvActionBar.ivBack.setOnClickListener { view: View? -> onBackPressed() }
    }

    private val dataFromIntent: Unit
        private get() {
            orderId = intent.getIntExtra("ORDER_ID", 0)
        }

    private fun consumeResponseForStatusUpdate(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {
               ProgressDialog.getInstance().show(this)
            }
            Status.SUCCESS -> {
                ProgressDialog.getInstance().dismiss()
                assert(apiResponse.data != null)
                renderSuccessResponseForDetails(apiResponse.data)
            }
            Status.ERROR -> {
                ProgressDialog.getInstance().dismiss()
                Utils.setToast(this, resources.getString(R.string.errorString))
            }
            else -> {}
        }
    }

    private fun renderSuccessResponseForDetails(response: JsonElement?) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response!!.isJsonNull) {
                try {
                    val jsonObject = JSONObject(response.toString())
                    val orderDetailsModel =
                        Gson().fromJson(jsonObject.toString(), OrderDetailsModel::class.java)
                    if (orderDetailsModel.isStatus) {
                        mBinding!!.tvOrderId.text =
                            "Order ID : " + orderDetailsModel.orderDispatchedObj.orderid
                        mBinding!!.tvTotalAmount.text =
                            Math.round(orderDetailsModel.orderDispatchedObj.grossamount).toString()
                        mBinding!!.tvAmount.text =
                            Math.round(orderDetailsModel.orderDispatchedObj.totalamount).toString()
                        val productItemAdapter = ProductItemAdapter(
                            this,
                            orderDetailsModel.orderDispatchedObj.orderDetails
                        )
                        mBinding!!.rvItemList.adapter = productItemAdapter
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(this, resources.getString(R.string.errorString), Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}