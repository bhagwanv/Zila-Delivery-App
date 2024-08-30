package com.sk.ziladelivery.ui.views.fragment

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sk.ziladelivery.R
import com.sk.ziladelivery.data.api.ApiHelper
import com.sk.ziladelivery.data.api.RestClient
import com.sk.ziladelivery.databinding.AddOrderFragmentBinding
import com.sk.ziladelivery.ui.views.main.MainActivity
import com.sk.ziladelivery.ui.views.viewmodels.AddOrderViewModel
import com.sk.ziladelivery.utilities.ProgressDialog
import com.sk.ziladelivery.utilities.SharePrefs
import com.sk.ziladelivery.utilities.Status
import com.sk.ziladelivery.utilities.Utils
import com.sk.ziladelivery.viewfactory.AddOrderFactory

class AddOrderFragment : Fragment() {
    private var activity: MainActivity? = null
    private var mBinding: AddOrderFragmentBinding? = null
    private var  addOrderViewModel:AddOrderViewModel?=null;
    private var zilaTripMasterId: Int? = null;

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
        addOrderViewModel = ViewModelProvider(viewModelStore, AddOrderFactory(ApiHelper(RestClient.getInstance().service)))[AddOrderViewModel::class.java]
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
                addOrder()
            }
        }

    }

    private fun addOrder() {
        addOrderViewModel?.addOrder(zilaTripMasterId!!,1223)?.observe(requireActivity()) { resource ->
            resource?.let {
                when (it.status) {
                    Status.SUCCESS -> {
                        ProgressDialog.getInstance().dismiss()
                        it.data?.let { orderList ->
                            //handleAllTripResponse(allTripModel)
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

    private fun getZilaTrip() {
        zilaTripMasterId?.let {
            addOrderViewModel?.getOrder(it)?.observe(requireActivity()) { resource ->
                resource?.let {
                    when (it.status) {
                        Status.SUCCESS -> {
                            ProgressDialog.getInstance().dismiss()
                            it.data?.let { orderList ->
                                //handleAllTripResponse(allTripModel)
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


}