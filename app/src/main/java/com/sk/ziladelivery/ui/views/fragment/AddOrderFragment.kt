package com.sk.ziladelivery.ui.views.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sk.ziladelivery.R
import com.sk.ziladelivery.data.api.ApiHelper
import com.sk.ziladelivery.data.api.RestClient
import com.sk.ziladelivery.data.model.CustomerInfo
import com.sk.ziladelivery.data.model.CustomerOrderInfo
import com.sk.ziladelivery.data.model.GetZilaTripResponse
import com.sk.ziladelivery.data.model.ZilaCreateTrip
import com.sk.ziladelivery.databinding.AddOrderFragmentBinding
import com.sk.ziladelivery.databinding.DialogConfirmationBinding
import com.sk.ziladelivery.listener.LisnerAllOrder
import com.sk.ziladelivery.listener.LisnerCustomerAllOrder
import com.sk.ziladelivery.ui.views.adapter.AllCustomersAdapter
import com.sk.ziladelivery.ui.views.main.MainActivity
import com.sk.ziladelivery.ui.views.viewmodels.AddOrderViewModel
import com.sk.ziladelivery.utilities.DateUtils
import com.sk.ziladelivery.utilities.ProgressDialog
import com.sk.ziladelivery.utilities.SharePrefs
import com.sk.ziladelivery.utilities.Status
import com.sk.ziladelivery.utilities.Utils
import com.sk.ziladelivery.viewfactory.AddOrderFactory

class AddOrderFragment : Fragment(), LisnerAllOrder, LisnerCustomerAllOrder {
    private var activity: MainActivity? = null
    private var binding: AddOrderFragmentBinding? = null
    private lateinit var addOrderViewModel: AddOrderViewModel
    private var zilaTripMasterId: Int? = null
    private var customDialog: Dialog? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as? MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = AddOrderFragmentBinding.inflate(inflater, container, false)
        zilaTripMasterId = arguments?.getInt("zilaTripMasterId")
        setupActionBar()
        initView()
        return binding!!.root
    }

    private fun initView() {
        addOrderViewModel = ViewModelProvider(this, AddOrderFactory(ApiHelper(RestClient.getInstance().service)))[AddOrderViewModel::class.java]

        if (Utils.checkInternetConnection(requireActivity())) {
            getZilaTrip()
        } else {
            Utils.setToast(requireActivity(), getString(R.string.network_error))
        }

        binding?.apply {
            swipeContainer.setOnRefreshListener {
                if (Utils.checkInternetConnection(requireActivity())) {
                    getZilaTrip()
                } else {
                    Utils.setToast(requireActivity(), getString(R.string.network_error))
                }
                swipeContainer.isRefreshing = false
            }

            btnAddOrder.setOnClickListener {
                if (Utils.checkInternetConnection(requireActivity())) {
                    showAddOrderPopup()
                } else {
                    Utils.setToast(requireActivity(), getString(R.string.network_error))
                }
            }

            btnFinalized.setOnClickListener {
                if (Utils.checkInternetConnection(requireActivity())) {
                    callFinalizedApi()
                } else {
                    Utils.setToast(requireActivity(), getString(R.string.network_error))
                }
            }
        }
    }

    private fun callFinalizedApi() {
        zilaTripMasterId?.let {
            val model = ZilaCreateTrip(
                it,
                0,
                DateUtils.getDateTime(),
                1,
                1,
                1,
                SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID),
                DateUtils.giveDate()
            )
            addOrderViewModel.zilaCreateTrip(model).observe(viewLifecycleOwner) { resource ->
                resource?.let {
                    when (it.status) {
                        Status.SUCCESS -> {
                            ProgressDialog.getInstance().dismiss()
                            if (it.data?.Status == true){
                                Utils.setToast(activity, it.data?.Message)
                                activity?.switchContentWithStack(DashBoardFragment())
                            }else{
                                Utils.setToast(activity, it.data?.Message)
                            }
                        }
                        Status.ERROR -> {
                            ProgressDialog.getInstance().dismiss()
                        }
                        Status.LOADING -> {
                            ProgressDialog.getInstance().show(requireActivity())
                        }
                    }
                }
            }
        }
    }

    private fun showAddOrderPopup() {
        val mView = layoutInflater.inflate(R.layout.add_order_popup, null)
        customDialog = Dialog(requireActivity(), R.style.CustomDialog).apply {
            setContentView(mView)
        }

        mView.findViewById<LinearLayout>(R.id.okBtn).setOnClickListener {
            val orderId = mView.findViewById<EditText>(R.id.etOrderID).text.toString().toIntOrNull()
            orderId?.let {
                customDialog?.dismiss()
                addOrder(it)
            } ?: run {
                // Handle invalid order ID case if needed
            }
        }

        mView.findViewById<LinearLayout>(R.id.cancelBtn).setOnClickListener {
            customDialog?.dismiss()
        }
        customDialog?.show()
    }

    private fun addOrder(orderId: Int) {
        zilaTripMasterId?.let {
            addOrderViewModel.addOrder(it, orderId).observe(viewLifecycleOwner) { resource ->
                resource?.let {
                    when (it.status) {
                        Status.SUCCESS -> {
                            ProgressDialog.getInstance().dismiss()
                            Utils.setToast(activity, it.data?.message)
                            getZilaTrip()

                        }
                        Status.ERROR -> {
                            ProgressDialog.getInstance().dismiss()
                        }
                        Status.LOADING -> {
                            ProgressDialog.getInstance().show(requireActivity())
                        }
                    }
                }
            }
        }
    }

    private fun removeOrder(orderId: Int) {
        zilaTripMasterId?.let {
            addOrderViewModel.removeOrder(it, orderId).observe(viewLifecycleOwner) { resource ->
                resource?.let {
                    when (it.status) {
                        Status.SUCCESS -> {
                            ProgressDialog.getInstance().dismiss()
                            Utils.setToast(activity, it.data?.message)
                            getZilaTrip()
                        }
                        Status.ERROR -> {
                            ProgressDialog.getInstance().dismiss()
                        }
                        Status.LOADING -> {
                            ProgressDialog.getInstance().show(requireActivity())
                        }
                    }
                }
            }
        }
    }

    private fun getZilaTrip() {
        zilaTripMasterId?.let {
            addOrderViewModel.getOrder(it).observe(viewLifecycleOwner) { resource ->
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
        binding?.apply {
            if (orderList.CustomerList.isNullOrEmpty()) {
                tvNoTrip.visibility = View.VISIBLE
                rvAllTrip.visibility = View.GONE
                btnFinalized.visibility = View.GONE
            } else {
                tvNoTrip.visibility = View.GONE
                rvAllTrip.visibility = View.VISIBLE
                btnFinalized.visibility = View.VISIBLE
                rvAllTrip.adapter =
                    AllCustomersAdapter(requireActivity(), orderList.CustomerList, this@AddOrderFragment, this@AddOrderFragment)
                rvAllTrip.adapter!!.notifyDataSetChanged()
            }
            btnAddOrder.visibility = View.VISIBLE
        }
    }
    private fun setupActionBar() {
        activity?.let { activity ->
            val drawer = activity.findViewById<DrawerLayout>(R.id.container)
            val toolbar = activity.findViewById<Toolbar>(R.id.toolbar)
            val layout = activity.findViewById<RelativeLayout>(R.id.titlebar)
            val linearLayout = activity.findViewById<LinearLayout>(R.id.ll_oder_id_view)
            val titleTextView = activity.findViewById<TextView>(R.id.toolbar_title)
            val startTimerButton = activity.findViewById<TextView>(R.id.start_timer)
            val assignmentIdTextView = activity.findViewById<TextView>(R.id.assignmentid)
            val timerTextView = activity.findViewById<TextView>(R.id.tv_timmer)
            val historyTextView = activity.findViewById<TextView>(R.id.tv_history)

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
                text = "  Add Order"
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
    }

    private fun confirmationDialog(
        title: String,
        orderId: Int
    ) {
        val dialogBinding = DataBindingUtil.inflate<DialogConfirmationBinding>(
            layoutInflater, R.layout.dialog_confirmation, null, false
        )

        customDialog = Dialog(requireActivity(), R.style.CustomDialog).apply {
            setContentView(dialogBinding.root)
            setCancelable(true)
        }

        dialogBinding.apply {
            tvTitleDesc.text = "Do you want to $title this order?"
            btNo.setOnClickListener { customDialog?.dismiss() }
            btYes.setOnClickListener {
                customDialog?.dismiss()
                removeOrder(orderId)
            }
        }

        customDialog?.show()
    }


    override fun onButtonClick(allTripModel: CustomerInfo) {
        // Implement the button click handling logic here.
        Utils.setToast(requireActivity(), "Button clicked with model: ${allTripModel}")
    }

    override fun onLisnerCustomerAllOrderClick(allTripModel: CustomerOrderInfo) {
        confirmationDialog("Remove", allTripModel.OrderId ?: return)
    }


}