package com.sk.ziladelivery.ui.views.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.sk.ziladelivery.R
import com.sk.ziladelivery.databinding.FragmentPickFromCustomerBinding
import com.sk.ziladelivery.listener.ButtonClick
import com.sk.ziladelivery.data.model.ReturnOrderListModel
import com.sk.ziladelivery.utilities.ApiResponse
import com.sk.ziladelivery.utilities.SharePrefs
import com.sk.ziladelivery.utilities.Status
import com.sk.ziladelivery.utilities.Utils
import com.sk.ziladelivery.ui.views.viewmodels.ReturnOrderListViewModel
import com.sk.ziladelivery.ui.views.adapter.ReturnOrderListAdapter
import org.json.JSONArray

/**
 * A simple [Fragment] subclass.
 */
class PickFromCustomerFragment : Fragment(), ButtonClick {
    private lateinit var mBinding: FragmentPickFromCustomerBinding
    private lateinit var activity: Activity
    private lateinit var viewModel: ReturnOrderListViewModel
    private var list: ArrayList<ReturnOrderListModel>? = null
    private var adapter: ReturnOrderListAdapter? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as Activity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_pick_from_customer,
            container,
            false
        )
        return mBinding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ReturnOrderListViewModel::class.java)

        mBinding.recyclerPickFromCustomer.layoutManager = LinearLayoutManager(activity)
//        recyclerPickFromCustomer.addOnItemTouchListener(RecyclerItemClickListener(activity, object : RecyclerItemClickListener.OnItemClickListener {
//            override fun onItemClick(view: View, position: Int) {}
//        }))

        viewModel.returnOrderListResponse().observe(
            this,
            Observer { apiResponse: ApiResponse -> consumeReturnOrderResponse(apiResponse) })
        viewModel.hitReturnOrderListAPI(
            SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID)
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 9 && resultCode == Activity.RESULT_OK) {
            list!!.clear()
            adapter!!.notifyDataSetChanged()
            viewModel.hitReturnOrderListAPI(
                SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID)
            )
        }
    }


    override fun onButtonClick(pos: Int) {
        /* startActivityForResult(Intent(activity, ReturnOrderDetailActivity::class.java).putExtra("list", list!![pos]), 9)
         Utils.leftTransaction(activity)*/
    }

    private fun consumeReturnOrderResponse(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING -> Utils.showProgressDialog(activity)
            Status.SUCCESS -> {
                Utils.hideProgressDialog(activity)
                renderSuccessResponse(apiResponse.data)
            }
            Status.ERROR -> {
                Utils.hideProgressDialog(activity)
                mBinding.tvEmpty.visibility = View.VISIBLE
                Utils.setToast(activity, resources.getString(R.string.errorString))
            }
            else -> {
            }
        }
    }

    private fun renderSuccessResponse(element: JsonElement?) {
        mBinding.tvEmpty.visibility = View.INVISIBLE
        try {
            if (element != null) {
                val array = JSONArray(element.toString())
                if (array.length() > 0) {
                    list = Gson().fromJson(
                        array.toString(),
                        object : TypeToken<ArrayList<ReturnOrderListModel?>?>() {}.type
                    )
                    adapter = ReturnOrderListAdapter(activity, list, this@PickFromCustomerFragment)
                    mBinding.recyclerPickFromCustomer.adapter = adapter
                } else {
                    mBinding.tvEmpty.visibility = View.VISIBLE
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            mBinding.tvEmpty.visibility = View.VISIBLE
        }
    }
}