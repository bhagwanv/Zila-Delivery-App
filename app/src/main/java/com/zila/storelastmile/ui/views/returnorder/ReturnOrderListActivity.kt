package com.zila.storelastmile.ui.views.returnorder

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.zila.storelastmile.R
import com.zila.storelastmile.databinding.ActivityReturnOrderListBinding
import com.zila.storelastmile.utilities.Utils
import com.zila.storelastmile.ui.views.fragment.PickFromCustomerFragment
import com.zila.storelastmile.ui.views.fragment.PickFromWarehouseFragment
import com.zila.trade.adapter.ViewPagerAdapter

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