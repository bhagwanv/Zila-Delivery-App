package com.sk.ziladelivery.ui.views.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.sk.ziladelivery.R
import com.sk.ziladelivery.data.api.ApiHelper
import com.sk.ziladelivery.data.api.RestClient
import com.sk.ziladelivery.data.model.RearrangModel
import com.sk.ziladelivery.databinding.ActivityRearrangeBinding
import com.sk.ziladelivery.listener.ItemMoveCallback
import com.sk.ziladelivery.listener.StartDragListener
import com.sk.ziladelivery.ui.views.adapter.RearrangeAdapter
import com.sk.ziladelivery.ui.views.viewmodels.RearrangeViewModel
import com.sk.ziladelivery.utilities.Status
import com.sk.ziladelivery.utilities.Utils
import com.sk.ziladelivery.viewfactory.RearrangFactory


class RearrangeActivity : AppCompatActivity(), StartDragListener {
    private var mBinding: ActivityRearrangeBinding? = null
    private var rearrangeModelView: RearrangeViewModel? = null
    private var tripPlannerConfirmedMasterId = 0
    var touchHelper: ItemTouchHelper? = null
    private var updateRearrangeModel: ArrayList<RearrangModel>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRearrangeBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)
        initView()
        setActionBarConfiguration()
        if (intent.extras != null) {
            tripPlannerConfirmedMasterId = intent.getIntExtra("TripPlannerConfirmedMasterId", 0)

        }

        loadingData()
    }

    private fun initView() {
        rearrangeModelView = ViewModelProvider(viewModelStore, RearrangFactory(ApiHelper(RestClient.getInstance().service)))[RearrangeViewModel::class.java]
        mBinding!!.btSave.setOnClickListener {
            if (!Utils.checkInternetConnection(applicationContext)) {
                Utils.setToast(this, resources.getString(R.string.network_error))
            } else {
                if (updateRearrangeModel != null && updateRearrangeModel!!.size > 0) {
                    updateRearrangeData()
                }else{
                    Toast.makeText(this,"Please Rearrange order List",Toast.LENGTH_LONG).show()
                }
            }
        }

        mBinding!!.btSkip.setOnClickListener {
            skipRearrange()
        }
    }

    private fun skipRearrange() {
        rearrangeModelView!!.getSkipRearrange(tripPlannerConfirmedMasterId).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        Utils.hideProgressDialog(this)
                        resource.data?.let { rearrangeSkip ->
                            skipData(rearrangeSkip)
                        }
                    }
                    Status.ERROR -> {
                        Utils.hideProgressDialog(this)
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        Utils.showProgressDialog(this)
                    }
                }
            }
        }
    }

    private fun skipData(rearrangeSkip: Boolean) {
        if (rearrangeSkip){
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
    }

    private fun updateRearrangeData() {
        rearrangeModelView?.updateRearrange(updateRearrangeModel!!)?.observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        Utils.hideProgressDialog(this)
                        resource.data?.let { rearrangeDataList ->
                            if (rearrangeDataList) {
                                startActivity(Intent(applicationContext, MainActivity::class.java))
                                finish()
                            }
                        }
                    }
                    Status.ERROR -> {
                        Utils.hideProgressDialog(this)
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        Utils.showProgressDialog(this)
                    }
                }
            }
        }

    }

    private fun setActionBarConfiguration() {
        mBinding!!.tvActionBar.tvOrderno.text = "Order Details"
        mBinding!!.tvActionBar.ivBack.setOnClickListener { onBackPressed() }
    }

    private fun loadingData() {
        rearrangeModelView!!.getRearrange(tripPlannerConfirmedMasterId).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        Utils.hideProgressDialog(this)
                        resource.data?.let { rearrangeDataList ->

                            setListRearrange(rearrangeDataList)
                        }
                    }
                    Status.ERROR -> {
                        Utils.hideProgressDialog(this)
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        Utils.showProgressDialog(this)
                    }
                }
            }
        }
    }

    private fun setListRearrange(rearrangeDataList: ArrayList<RearrangModel>) {
        if (rearrangeDataList.size > 0) {
            val adapter = RearrangeAdapter(this, rearrangeDataList, this)
            mBinding!!.recyclerview.adapter = adapter
            val callback: ItemTouchHelper.Callback = ItemMoveCallback(adapter)
            touchHelper = ItemTouchHelper(callback)
            touchHelper!!.attachToRecyclerView(mBinding!!.recyclerview)
            mBinding!!.rlOrderList.visibility = View.VISIBLE
            mBinding!!.tvNoDataFound.visibility = View.GONE
        } else {
            mBinding!!.rlOrderList.visibility = View.GONE
            mBinding!!.tvNoDataFound.visibility = View.VISIBLE
        }
    }

    override fun requestDrag(
        viewHolder: RecyclerView.ViewHolder?,
        rearrangModel: ArrayList<RearrangModel>?
    ) {
        updateRearrangeModel = rearrangModel!!
        touchHelper!!.startDrag(viewHolder!!)
    }


}