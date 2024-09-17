package com.sk.ziladelivery.ui.views.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.TextViewBindingAdapter.setTextSize
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.sk.ziladelivery.R
import com.sk.ziladelivery.data.api.ApiHelper
import com.sk.ziladelivery.data.api.RestClient
import com.sk.ziladelivery.data.model.AllTripModel
import com.sk.ziladelivery.data.model.CustomerInfo
import com.sk.ziladelivery.data.model.CustomerOrderInfo
import com.sk.ziladelivery.data.model.GetZilaTripResponse
import com.sk.ziladelivery.data.model.TokenResponse
import com.sk.ziladelivery.data.model.ZilaCreateTrip
import com.sk.ziladelivery.databinding.AddOrderFragmentBinding
import com.sk.ziladelivery.databinding.DialogConfirmationBinding
import com.sk.ziladelivery.listener.LisnerAllOrder
import com.sk.ziladelivery.listener.LisnerCustomerAllOrder
import com.sk.ziladelivery.ui.views.adapter.AllCustomersAdapter
import com.sk.ziladelivery.ui.views.adapter.AllTripAdapter
import com.sk.ziladelivery.ui.views.main.LoginActivity
import com.sk.ziladelivery.ui.views.main.MainActivity
import com.sk.ziladelivery.ui.views.main.ScanOrderActivity
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
    private var zilaTripMasterId = 0;
    private var customDialog: Dialog? = null
    private lateinit var scanOrderActivityLauncher: ActivityResultLauncher<Intent>
    private var allTripModelResponse: MutableList<AllTripModel> = mutableListOf()
    private lateinit var tvTripIDTextView: TextView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as? MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = AddOrderFragmentBinding.inflate(inflater, container, false)
        setupActionBar()
        initView()
        return binding!!.root
    }

    override fun onResume() {
        super.onResume()
        activity?.activityFlag = this.javaClass.simpleName
    }

    private fun initView() {
        addOrderViewModel = ViewModelProvider(
            this,
            AddOrderFactory(ApiHelper(RestClient.getInstance().service))
        )[AddOrderViewModel::class.java]

        scanOrderActivityLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                var invoiceNUmber = data?.getStringExtra("SCANNED_CODE")
                Log.e("scannedCode", "scannedCode: $invoiceNUmber")
                getInvoice(invoiceNUmber)

            }
        }


        if (Utils.checkInternetConnection(requireActivity())) {
            fetchAllTripIDs()
            // createTrip()
            //getZilaTrip()
        } else {
            Utils.setToast(requireActivity(), getString(R.string.network_error))
        }

        binding?.apply {
            swipeContainer.setOnRefreshListener {
                if (Utils.checkInternetConnection(requireActivity())) {
                    fetchAllTripIDs()
                    //getZilaTrip()
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

    private fun createTrip() {
        val createTripModel = CreateTripModel(
            10,
            12,
            1,
            SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID)
        )
        addOrderViewModel?.createTrip(createTripModel)?.observe(requireActivity()) { resource ->
            resource?.let {
                when (it.status) {
                    Status.SUCCESS -> {
                        ProgressDialog.getInstance().dismiss()
                        it.data?.let { allTripModel ->
                            if (it.data.Status) {
                                fetchAllTripIDs()
                            } else {
                                Utils.setToast(activity, allTripModel.Message)
                            }

                        }
                    }

                    Status.ERROR -> {
                        ProgressDialog.getInstance().dismiss()
                        if (it.message == "401") {
                            handleUnauthorizedError()
                        }
                    }

                    Status.LOADING -> {
                        ProgressDialog.getInstance().show(requireActivity())
                    }
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
                            if (it.data?.Status == true) {
                                SharePrefs.getInstance(requireActivity())
                                    .putLong(
                                        SharePrefs.ALL_TRIP_SLECTED,
                                        zilaTripMasterId!!.toLong()
                                    )
                                Utils.setToast(activity, it.data?.Message)
                                activity?.switchContentWithStack(DashBoardFragment())
                            } else {
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
        val intent = Intent(activity, ScanOrderActivity::class.java)
        scanOrderActivityLauncher.launch(intent)
    }

    private fun getInvoice(invoiceNUmber: String?) {
        invoiceNUmber?.let {
            addOrderViewModel.getInvoice(it).observe(viewLifecycleOwner) { resource ->
                resource?.let {
                    when (it.status) {
                        Status.SUCCESS -> {
                            ProgressDialog.getInstance().dismiss()
                            it.data?.let { orderIDList ->
                                if (orderIDList.Status) {
                                    addOrder(orderIDList.Data)
                                    //Utils.setToast(activity,orderIDList.Message)
                                } else {
                                    // Utils.setToast(activity,orderIDList.Message)
                                }

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

    private fun addOrder(orderId: Int) {
        zilaTripMasterId?.let {
            addOrderViewModel.addOrder(
                it,
                orderId,
                SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID)
            ).observe(viewLifecycleOwner) { resource ->
                resource?.let {
                    when (it.status) {
                        Status.SUCCESS -> {
                            ProgressDialog.getInstance().dismiss()
                            if (it.data!!.status!!) {
                                fetchAllTripIDs()
                                if (it.data!!.message != null) {
                                    Utils.setToast(activity, it.data!!.message)
                                }
                                //getZilaTrip()
                            } else {
                                if (it.data!!.message != null) {
                                    Utils.setToast(activity, it.data!!.message)
                                }
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


    private fun removeOrder(orderId: Int) {
        zilaTripMasterId?.let {
            addOrderViewModel.removeOrder(
                it,
                orderId,
                SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID)
            ).observe(viewLifecycleOwner) { resource ->
                resource?.let {
                    when (it.status) {
                        Status.SUCCESS -> {
                            ProgressDialog.getInstance().dismiss()
                            fetchAllTripIDs()
                            //getZilaTrip()
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

                val adapter = rvAllTrip.adapter as? AllCustomersAdapter
                if (adapter == null) {
                    rvAllTrip.adapter = AllCustomersAdapter(
                        requireActivity(),
                        orderList.CustomerList,
                        this@AddOrderFragment,
                        this@AddOrderFragment
                    )
                } else {
                    adapter.updateData(orderList.CustomerList)  // Update adapter data
                }

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
            tvTripIDTextView = activity.findViewById<TextView>(R.id.tvTripID)

            layout.visibility = View.VISIBLE
            linearLayout.visibility = View.GONE
            assignmentIdTextView.visibility = View.GONE
            timerTextView.visibility = View.GONE
            historyTextView.visibility = View.GONE
            tvTripIDTextView.visibility = View.VISIBLE


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
                setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    resources.getDimension(R.dimen.font_size_18)
                )
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

    private fun fetchAllTripIDs() {
        addOrderViewModel?.getAllTripID(
            SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID)
        )?.observe(requireActivity()) { resource ->
            resource?.let {
                when (it.status) {
                    Status.SUCCESS -> {
                        ProgressDialog.getInstance().dismiss()
                        it.data?.let { allTripModel ->
                            handleAllTripResponse(allTripModel)
                        }
                    }

                    Status.ERROR -> {
                        ProgressDialog.getInstance().dismiss()
                        if (it.message == "401") {
                            handleUnauthorizedError()
                        }
                    }

                    Status.LOADING -> {
                        ProgressDialog.getInstance().show(requireActivity())
                    }
                }
            }
        }
    }

    private fun handleUnauthorizedError() {
        val token = SharePrefs.getInstance(activity).getString(SharePrefs.TOKEN_NAME)
        if (token.isNullOrEmpty()) {
            PreferenceManager.getDefaultSharedPreferences(activity).edit().clear().apply()
            SharePrefs.getInstance(activity).putBoolean(SharePrefs.LOGGED, false)
            val intent = Intent(activity, LoginActivity::class.java)
            intent.putExtra("Type", "ResetPasswordActivity")
            startActivity(intent)
            requireActivity().finish()
        } else {
            refreshAuthToken()
        }
    }

    private fun refreshAuthToken() {
        val username = SharePrefs.getInstance(activity).getString(SharePrefs.TOKEN_NAME)
        val password = SharePrefs.getInstance(activity).getString(SharePrefs.TOKEN_PASSWORD)
        requestAuthToken("password", username, password)
    }

    private fun requestAuthToken(grantType: String?, username: String?, password: String?) {
        addOrderViewModel?.getTokenData(grantType, username, password)
            ?.observe(requireActivity()) { resource ->
                resource?.let {
                    when (it.status) {
                        Status.SUCCESS -> it.data?.let { tokenResponse ->
                            handleTokenResponse(tokenResponse)
                        }

                        Status.ERROR -> Unit
                        Status.LOADING -> Unit
                    }
                }
            }
    }

    private fun handleTokenResponse(tokenResponse: TokenResponse) {
        SharePrefs.getInstance(activity).putString(SharePrefs.TOKEN, tokenResponse.access_token)
        startActivity(Intent(activity, MainActivity::class.java))
        requireActivity().finish()
    }

    private fun handleAllTripResponse(allTripModel: JsonArray) {
        val typeToken = object : TypeToken<MutableList<AllTripModel>>() {}.type
        allTripModelResponse = Gson().fromJson(allTripModel, typeToken)
        if (allTripModelResponse.isNotEmpty()) {
            allTripModelResponse.forEach {
                if (it.isFreezed) {
                    SharePrefs.getInstance(requireActivity())
                        .putLong(SharePrefs.ALL_TRIP_SLECTED, it.zilaTripMasterId.toLong())
                    activity?.addFragment(DashBoardFragment(), false, null)

                } else {
                    zilaTripMasterId = allTripModelResponse[0].zilaTripMasterId
                    tvTripIDTextView.text = "TripId:${zilaTripMasterId}"

                }
            }
            getZilaTrip()

        } else {
            zilaTripMasterId=0
            tvTripIDTextView.text = "TripId:${zilaTripMasterId}"
            binding!!.tvNoTrip.visibility = View.VISIBLE
            binding!!.rvAllTrip.visibility = View.GONE
            binding!!.btnFinalized.visibility = View.GONE
        }


        /* if (allTripModelResponse.isNullOrEmpty()) {
             mBinding!!.tvNoTrip.visibility = View.VISIBLE
             mBinding!!.btnCreateTrip.visibility = View.VISIBLE
             mBinding!!.rvAllTrip.visibility = View.GONE
         } else {
             mBinding!!.tvNoTrip.visibility = View.GONE
             mBinding!!.btnCreateTrip.visibility = View.GONE
             mBinding!!.rvAllTrip.visibility = View.INVISIBLE

             if (mBinding!!.rvAllTrip.adapter == null) {
                 val adapter = AllTripAdapter(requireActivity(), allTripModelResponse, this)
                 mBinding!!.rvAllTrip.adapter = adapter
             } else {
                 (mBinding!!.rvAllTrip.adapter as AllTripAdapter).updateData(allTripModelResponse)
             }
         }*/
    }
}