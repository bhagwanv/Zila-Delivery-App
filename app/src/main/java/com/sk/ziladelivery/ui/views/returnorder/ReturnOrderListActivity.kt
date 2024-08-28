package com.sk.ziladelivery.ui.views.returnorder

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.sk.ziladelivery.R
import com.sk.ziladelivery.databinding.ActivityReturnOrderListBinding
import com.sk.ziladelivery.utilities.Utils
import com.sk.ziladelivery.ui.views.fragment.PickFromCustomerFragment
import com.sk.ziladelivery.ui.views.fragment.PickFromWarehouseFragment
import com.sk.trade.adapter.ViewPagerAdapter

class ReturnOrderListActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityReturnOrderListBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_return_order_list)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        setupViewPager()
        mBinding.tabs.setupWithViewPager(mBinding.viewPagerReturn)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Utils.rightTransaction(this)
    }


    private fun setupViewPager() {
        val list = ArrayList<Fragment>()
        list.add(PickFromCustomerFragment())
        list.add(PickFromWarehouseFragment())

        val titleList = ArrayList<String>()
        titleList.add("Pick From Customer")
        titleList.add("Pick From Warehouse")
        val adapter = ViewPagerAdapter(supportFragmentManager, list, titleList = titleList)
        mBinding.viewPagerReturn.adapter = adapter
    }
}