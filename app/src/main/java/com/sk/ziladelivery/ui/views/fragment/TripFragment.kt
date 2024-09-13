package com.sk.ziladelivery.ui.views.fragment

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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
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
import com.sk.ziladelivery.data.model.StartAssignmentPostModel
import com.sk.ziladelivery.data.model.TokenResponse
import com.sk.ziladelivery.databinding.FragmentTripBinding
import com.sk.ziladelivery.listener.LisnerAllTrip
import com.sk.ziladelivery.ui.views.adapter.AllTripAdapter
import com.sk.ziladelivery.ui.views.main.LoginActivity
import com.sk.ziladelivery.ui.views.main.MainActivity
import com.sk.ziladelivery.ui.views.viewmodels.AllTripViewModel
import com.sk.ziladelivery.utilities.*
import com.sk.ziladelivery.viewfactory.AllTripFactory

class TripFragment : Fragment(), LisnerAllTrip {

    private var activity: MainActivity? = null
    private var mBinding: FragmentTripBinding? = null
    private var allTripViewModel: AllTripViewModel? = null
    private var allTripModelResponse: MutableList<AllTripModel> = mutableListOf()  // Initialize with empty list

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentTripBinding.inflate(inflater, container, false)
        setupActionBar()
        initializeView()
        return mBinding!!.root
    }

    private fun initializeView() {
        MainActivity.startBreak?.visibility = View.GONE

        allTripViewModel = ViewModelProvider(
            viewModelStore,
            AllTripFactory(ApiHelper(RestClient.getInstance().service))
        )[AllTripViewModel::class.java]

        if (!Utils.checkInternetConnection(requireActivity())) {
            Utils.setToast(requireActivity(), getString(R.string.network_error))
        } else {
            fetchAllTripIDs()
        }

        mBinding!!.swipeContainer.setOnRefreshListener {
            if (!Utils.checkInternetConnection(requireActivity())) {
                Utils.setToast(requireActivity(), getString(R.string.network_error))
            } else {
                fetchAllTripIDs()
            }
            mBinding!!.swipeContainer.isRefreshing = false
        }

        mBinding!!.btnCreateTrip.setOnClickListener {
            AlertDialog.Builder((getActivity())!!)
                .setTitle("Alert")
                .setMessage("Are you sure you want to Create the trip ?")
                .setPositiveButton(android.R.string.yes) { dialog, whichButton ->
                    if (!Utils.checkInternetConnection(requireActivity())) {
                        Utils.setToast(requireActivity(), getString(R.string.network_error))
                    } else {
                        createTrip()
                    }
                }
                .setNegativeButton(
                    android.R.string.no
                ) { dialogInterface, i ->
                    dialogInterface.dismiss()
                }.show()

        }
    }

    private fun createTrip() {
        val createTripModel = CreateTripModel(
            10,
            12,
            1,
            SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID)
        )
        allTripViewModel?.createTrip(createTripModel)?.observe(requireActivity()) { resource ->
            resource?.let {
                when (it.status) {
                    Status.SUCCESS -> {
                        ProgressDialog.getInstance().dismiss()
                        it.data?.let { allTripModel ->
                            if(it.data.Status){
                                fetchAllTripIDs()
                            }else{
                                Utils.setToast(activity,allTripModel.Message)
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

    private fun fetchAllTripIDs() {
        allTripViewModel?.getAllTripID(
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

    private fun handleAllTripResponse(allTripModel: JsonArray) {
        val typeToken = object : TypeToken<MutableList<AllTripModel>>() {}.type
        allTripModelResponse = Gson().fromJson(allTripModel, typeToken)

        if (allTripModelResponse.isNullOrEmpty()) {
            mBinding!!.tvNoTrip.visibility = View.VISIBLE
            mBinding!!.btnCreateTrip.visibility = View.VISIBLE
            mBinding!!.rvAllTrip.visibility = View.GONE
        } else {
            mBinding!!.tvNoTrip.visibility = View.GONE
            mBinding!!.btnCreateTrip.visibility = View.GONE
            mBinding!!.rvAllTrip.visibility = View.VISIBLE

            if (mBinding!!.rvAllTrip.adapter == null) {
                val adapter = AllTripAdapter(requireActivity(), allTripModelResponse, this)
                mBinding!!.rvAllTrip.adapter = adapter
            } else {
                (mBinding!!.rvAllTrip.adapter as AllTripAdapter).updateData(allTripModelResponse)
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
        allTripViewModel?.getTokenData(grantType, username, password)
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
            text = "All Trips"
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

    override fun onButtonClick(allTripModel: AllTripModel) {
        if (allTripModel.isFreezed) {
            SharePrefs.getInstance(requireActivity())
                .putLong(SharePrefs.ALL_TRIP_SLECTED, allTripModel.zilaTripMasterId.toLong())
            activity?.switchContentWithStack(DashBoardFragment())
        } else {
            val fragment = AddOrderFragment().apply {
                arguments = Bundle().apply {
                    putInt("zilaTripMasterId", allTripModel.zilaTripMasterId)
                }
            }
            activity?.switchContentWithStack(fragment);
        }
    }
}

