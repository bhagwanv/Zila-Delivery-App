package com.sk.ziladelivery.ui.views.fragment

import android.content.Context
import com.sk.ziladelivery.listener.AssginmentSettleViewDetailInterface
import com.sk.ziladelivery.ui.views.main.MainActivity
import com.sk.ziladelivery.ui.views.viewmodels.AssginmentSettleViewModel
import com.sk.ziladelivery.ui.views.adapter.AssginmentsettleAdapter
import com.sk.ziladelivery.data.model.AssginmentSettleResponseModel
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.sk.ziladelivery.R
import com.google.gson.JsonElement
import org.json.JSONArray
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.ViewModelProviders
import androidx.drawerlayout.widget.DrawerLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.sk.ziladelivery.data.model.AssginmentSettleResponseModel.DBoyAssignmentDeposits
import com.sk.ziladelivery.databinding.FragmentMultipleAssignmentBinding
import com.sk.ziladelivery.ui.views.main.MainActivity.Companion.startBreak
import com.sk.ziladelivery.utilities.*
import java.lang.Exception
import java.util.ArrayList

class AssginmentSettleFragment : Fragment(), AssginmentSettleViewDetailInterface {
    private var mBinding: FragmentMultipleAssignmentBinding? = null
    private var activity: MainActivity? = null
    var AssignmentViewModel: AssginmentSettleViewModel? = null
    var adapter: AssginmentsettleAdapter? = null
    var model: ArrayList<AssginmentSettleResponseModel>? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_multiple_assignment,
            container,
            false
        )
        initialization()
        return mBinding!!.getRoot()
    }

    private fun consumeResponseAssignment(apiResponseObj: ApiResponse) {
        try {
            when (apiResponseObj.status) {
                Status.LOADING -> mBinding!!.progressBid.visibility = View.VISIBLE
                Status.SUCCESS -> {
                    mBinding!!.progressBid.visibility = View.GONE
                    renderSuccessResponseAssignment(apiResponseObj.data)
                }
                Status.ERROR -> {
                    mBinding!!.progressBid.visibility = View.GONE
                    Utils.setToast(activity, resources.getString(R.string.errorString))
                }
                else -> {}
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun renderSuccessResponseAssignment(response: JsonElement?) {
        try {
            if (Utils.isJSONValid(response.toString())) {
                if (response!!.isJsonArray) {
                    Logger.e(CommonMethods.getTag(this), response.toString())
                    try {
                        val obj = JSONArray(response.toString())
                        model = Gson().fromJson(
                            obj.toString(),
                            object : TypeToken<ArrayList<AssginmentSettleResponseModel?>?>() {}.type
                        )
                        if (model!!.size > 0) {
                            mBinding!!.tvMyTask.visibility = View.GONE
                            mBinding!!.rvMyTask.visibility = View.VISIBLE
                            adapter = AssginmentsettleAdapter(activity, model, this)
                            mBinding!!.rvMyTask.adapter = adapter
                        } else {
                            mBinding!!.tvMyTask.visibility = View.VISIBLE
                            mBinding!!.rvMyTask.visibility = View.GONE
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    Toast.makeText(
                        activity,
                        resources.getString(R.string.errorString),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(activity, response.toString(), Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initialization() {
        setActionBarConfiguration()
        mBinding!!.rvMyTask.layoutManager = LinearLayoutManager(getActivity())
        mBinding!!.rvMyTask.setHasFixedSize(true)
        mBinding!!.SearchBar.visibility = View.GONE
        AssignmentViewModel = ViewModelProviders.of(requireActivity()).get(
            AssginmentSettleViewModel::class.java
        )
        AssignmentViewModel!!.assignment.observe(requireActivity()) { apiResponse: ApiResponse? ->
            apiResponse?.let {
                consumeResponseAssignment(
                    it
                )
            }
        }
        if (!Utils.checkInternetConnection(activity)) {
            Utils.setToast(activity, resources.getString(R.string.network_error))
        } else {
            AssignmentViewModel!!.hitAssignmentApi(
                SharePrefs.getInstance(getActivity()).getInt(SharePrefs.PEOPLE_ID)
            )
        }
    }

    private fun setActionBarConfiguration() {
        val drawer = requireActivity().findViewById<DrawerLayout>(R.id.container)
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)
        toolbar.visibility = View.VISIBLE
        val tittleTextView = requireActivity().findViewById<TextView>(R.id.toolbar_title)
        val assignmentid = requireActivity().findViewById<TextView>(R.id.assignmentid)
        assignmentid.visibility = View.GONE
        tittleTextView.visibility = View.VISIBLE
        tittleTextView.text = "Assignment Settle"
        val starttimer = requireActivity().findViewById<TextView>(R.id.start_timer)
        val tvHistory = requireActivity().findViewById<TextView>(R.id.tv_history)
        starttimer.visibility = View.GONE
        tvHistory.visibility = View.VISIBLE
        startBreak!!.setVisibility(View.GONE)
        tvHistory.setText(R.string.history)
        val timer = requireActivity().findViewById<TextView>(R.id.tv_timer)
        timer.visibility = View.GONE
        toolbar.setNavigationOnClickListener { v: View? ->
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START)
            } else {
                drawer.openDrawer(GravityCompat.START)
            }
        }
        tvHistory.setOnClickListener {
            activity?.switchContentWithStack(
                AssginmentSettleHistoryFragment()
            )
        }
        activity?.toggle!!.syncState()
    }

    override fun viewDetailClick(id: Int) {
        val bundle = Bundle()
        val fragment: Fragment = AssginmentSettleDetailFragment()
        for (i in model!!.indices) {
            if (model!![i].id == id) {
                bundle.putSerializable("list", model!![i].dBoyAssignmentDeposits)
                bundle.putInt("id", model!![i].id)
                bundle.putString("pdfurl", model!![i].isUNSignOffUrl)
                break
            }
        }
        fragment.arguments = bundle
        activity?.switchContentWithStack(fragment)
    }

    override fun saveCommentClick(deliveryIssuanceId: ArrayList<DBoyAssignmentDeposits>) {}
}