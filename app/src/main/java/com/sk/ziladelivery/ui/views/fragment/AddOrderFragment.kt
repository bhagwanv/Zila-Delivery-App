package com.sk.ziladelivery.ui.views.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.sk.ziladelivery.R
import com.sk.ziladelivery.data.api.ApiHelper
import com.sk.ziladelivery.data.api.RestClient
import com.sk.ziladelivery.data.model.AllTripModel
import com.sk.ziladelivery.data.model.CustomerInfo
import com.sk.ziladelivery.data.model.CustomerOrderInfo
import com.sk.ziladelivery.data.model.GetZilaTripResponse
import com.sk.ziladelivery.databinding.AddOrderFragmentBinding
import com.sk.ziladelivery.databinding.DialogConfirmationBinding
import com.sk.ziladelivery.listener.LisnerAllOrder
import com.sk.ziladelivery.listener.LisnerCustomerAllOrder
import com.sk.ziladelivery.ui.views.adapter.AllCustomersAdapter
import com.sk.ziladelivery.ui.views.main.MainActivity
import com.sk.ziladelivery.ui.views.viewmodels.AddOrderViewModel
import com.sk.ziladelivery.utilities.ProgressDialog
import com.sk.ziladelivery.utilities.Status
import com.sk.ziladelivery.utilities.Utils
import com.sk.ziladelivery.viewfactory.AddOrderFactory
import java.util.ArrayList

class AddOrderFragment : Fragment(), LisnerAllOrder, LisnerCustomerAllOrder {
    private var activity: MainActivity? = null
    private var mBinding: AddOrderFragmentBinding? = null
    private var addOrderViewModel: AddOrderViewModel? = null;
    private var zilaTripMasterId: Int? = null;
    private var customDialog: Dialog? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = AddOrderFragmentBinding.inflate(inflater, container, false)

        val bundle = arguments
        if (bundle != null) {
            zilaTripMasterId = bundle.getInt("zilaTripMasterId")
        }
        setupActionBar();
        initView()
        return mBinding!!.root
    }

    private fun initView() {
        addOrderViewModel = ViewModelProvider(
            viewModelStore,
            AddOrderFactory(ApiHelper(RestClient.getInstance().service))
        )[AddOrderViewModel::class.java]
        if (!Utils.checkInternetConnection(requireActivity())) {
            Utils.setToast(requireActivity(), getString(R.string.network_error))
        } else {
            getZilaTrip()
        }
        mBinding!!.swipeContainer.setOnRefreshListener {
            if (!Utils.checkInternetConnection(requireActivity())) {
                Utils.setToast(requireActivity(), getString(R.string.network_error))
            } else {
                getZilaTrip()
            }
            mBinding!!.swipeContainer.isRefreshing = false
        }

        mBinding!!.btnAddOrder.setOnClickListener {
            if (!Utils.checkInternetConnection(requireActivity())) {
                Utils.setToast(requireActivity(), getString(R.string.network_error))
            } else {
                //  addOrder()
                addOrderPopup()
            }
        }

    }

    private fun addOrderPopup() {
        val inflater = layoutInflater
        val mView = inflater.inflate(R.layout.add_order_popup, null)
        customDialog = Dialog(requireActivity(), R.style.CustomDialog)
        customDialog!!.setContentView(mView)
        val okBtn = mView.findViewById<LinearLayout>(R.id.okBtn)
        val cancelBtn = mView.findViewById<LinearLayout>(R.id.cancelBtn)
        val etOrderId = mView.findViewById<EditText>(R.id.etOrderID)
        okBtn.setOnClickListener { v: View? ->
            val parsedInt = etOrderId.text.toString().toIntOrNull()
            if (parsedInt != null) {
                customDialog!!.dismiss()
                addOrder(parsedInt)
            } else {
                // not a valid int
            }
        }
        cancelBtn.setOnClickListener { v: View? -> customDialog!!.dismiss() }
        customDialog!!.show()
    }

    private fun addOrder(orderId: Int) {
        addOrderViewModel?.addOrder(zilaTripMasterId!!, orderId)
            ?.observe(requireActivity()) { resource ->
                resource?.let {
                    when (it.status) {
                        Status.SUCCESS -> {
                            ProgressDialog.getInstance().dismiss()
                            it.data?.let { res ->
                                if (res.status!!) {
                                    Toast.makeText(activity, "${res.message}", Toast.LENGTH_SHORT)
                                        .show()
                                    getZilaTrip()
                                } else {
                                    Toast.makeText(activity, "${res.message}", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }

                        Status.ERROR -> {
                            ProgressDialog.getInstance().dismiss()
                            if (it.message == "401") {
                                // handleUnauthorizedError()
                            }
                        }

                        Status.LOADING -> {
                            ProgressDialog.getInstance().show(requireActivity())
                        }
                    }
                }
            }
    }

    private fun removeOrder(orderId: Int) {
        addOrderViewModel?.removeOrder(zilaTripMasterId!!, orderId)
            ?.observe(requireActivity()) { resource ->
                resource?.let {
                    when (it.status) {
                        Status.SUCCESS -> {
                            ProgressDialog.getInstance().dismiss()
                            it.data?.let { res ->
                                if (res.status!!) {
                                    Toast.makeText(activity, "${res.message}", Toast.LENGTH_SHORT)
                                        .show()
                                    getZilaTrip()
                                } else {
                                    Toast.makeText(activity, "${res.message}", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }

                        Status.ERROR -> {
                            ProgressDialog.getInstance().dismiss()
                            if (it.message == "401") {
                                // handleUnauthorizedError()
                            }
                        }

                        Status.LOADING -> {
                            ProgressDialog.getInstance().show(requireActivity())
                        }
                    }
                }
            }
    }

    private fun handleAddOrderResponse(allTripModel: JsonArray) {
        val typeToken = object : TypeToken<MutableList<AllTripModel>>() {}.type
        // allTripModelResponse = Gson().fromJson(allTripModel, typeToken)
    }

    private fun getZilaTrip() {
        zilaTripMasterId?.let {
            addOrderViewModel?.getOrder(it)?.observe(requireActivity()) { resource ->
                resource?.let {
                    when (it.status) {
                        Status.SUCCESS -> {
                            ProgressDialog.getInstance().dismiss()
                            it.data?.let { orderList ->
                                handleOrdersList(orderList)
                            }
                        }

                        Status.ERROR -> {
                            ProgressDialog.getInstance().dismiss()
                            if (it.message == "401") {
                                // handleUnauthorizedError()
                            }
                        }

                        Status.LOADING -> {
                            ProgressDialog.getInstance().show(requireActivity())
                        }
                    }
                }
            }
        }
    }

    private fun handleOrdersList(orderList: GetZilaTripResponse) {
        if (orderList.CustomerList.isNullOrEmpty()) {
            mBinding!!.tvNoTrip.visibility = View.VISIBLE
            mBinding!!.rvAllTrip.visibility = View.GONE
        } else {
            mBinding!!.tvNoTrip.visibility = View.GONE
            mBinding!!.rvAllTrip.visibility = View.VISIBLE
            val adapter = AllCustomersAdapter(requireActivity(), orderList.CustomerList, this, this)
            mBinding!!.rvAllTrip.adapter = adapter
        }
        mBinding!!.btnAddOrder.visibility = View.VISIBLE
    }

    private fun setupActionBar() {
        val drawer = requireActivity().findViewById<DrawerLayout>(R.id.container)
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)
        val layout = requireActivity().findViewById<RelativeLayout>(R.id.titlebar)
        val linearLayout = requireActivity().findViewById<LinearLayout>(R.id.ll_oder_id_view)
        val titleTextView = requireActivity().findViewById<TextView>(R.id.toolbar_title)
        val startTimerButton = requireActivity().findViewById<TextView>(R.id.start_timer)
        val assignmentIdTextView = requireActivity().findViewById<TextView>(R.id.assignmentid)
        val timerTextView = requireActivity().findViewById<TextView>(R.id.tv_timmer)
        val historyTextView = requireActivity().findViewById<TextView>(R.id.tv_history)

        layout.visibility = View.VISIBLE
        linearLayout.visibility = View.GONE
        assignmentIdTextView.visibility = View.GONE
        timerTextView.visibility = View.GONE
        historyTextView.visibility = View.GONE

        startTimerButton.apply {
            visibility = View.GONE
            text = "00:00:00"
        }

        titleTextView.apply {
            visibility = View.VISIBLE
            text = "Add Order"
        }

        MainActivity.myTripView?.apply {
            setTextColor(ContextCompat.getColor(requireContext(), R.color.Black))
            setBackgroundResource(0)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.font_size_18))
        }

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        toolbar.setNavigationOnClickListener {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START)
            } else {
                drawer.openDrawer(GravityCompat.START)
            }
        }
    }

    private fun confirmationDialog(
        title: String,
        orderId: Int
    ) {
        val dialogConfirmationBinding = DataBindingUtil.inflate<DialogConfirmationBinding>(
            layoutInflater, R.layout.dialog_confirmation, null, false
        )
        val dialog = Dialog(requireActivity(), R.style.CustomDialog)
        dialog.setContentView(dialogConfirmationBinding.root)
        dialog.setCancelable(true)
        dialogConfirmationBinding.tvTitleDesc.text =
            "Do you want to $title this order?"
        dialogConfirmationBinding.btNo.setOnClickListener { dialog.dismiss() }
        dialogConfirmationBinding.btYes.setOnClickListener {
            dialog.dismiss()
            removeOrder(orderId)
        }
        dialog.show()
    }

    override fun onButtonClick(allTripModel: CustomerInfo) {
        Log.d("TAG", "onButtonClick: uha pr 1111")
    }

    override fun onLisnerCustomerAllOrderClick(allTripModel: CustomerOrderInfo) {
        Log.d("TAG", "onButtonClick: uha pr 22222")
        confirmationDialog("Remove", allTripModel.OrderId!!);
    }


}