package com.zila.storelastmile.ui.views.main;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.zila.storelastmile.R;
import com.zila.storelastmile.databinding.HistoryDetailBinding;
import com.zila.storelastmile.data.model.OrderDetail;
import com.zila.storelastmile.utilities.LocaleHelper;
import com.zila.storelastmile.ui.views.viewmodels.ItemDetailsInfo;
import com.zila.storelastmile.ui.views.adapter.HistoryItemDetailsAdapter;

import java.util.ArrayList;
import java.util.List;

public class HistoryDetailActivity extends AppCompatActivity implements HistoryItemDetailsAdapter.QunatityInterface {
    HistoryDetailBinding mBinding;
    List<OrderDetail> itemLists = new ArrayList<>();
    OrderDetail orderDetail;
    HistoryItemDetailsAdapter adapter;
    ItemDetailsInfo itemDetailsInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.history_detail);
        initialization();


    }

    private void initialization() {
        ItemDetailsInfo itemDetailsInfo = new ItemDetailsInfo();
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        itemDetailsInfo.setOrderid(getString(R.string.OrderID)+String.valueOf(intent.getIntExtra("orderid", 0)));
        itemDetailsInfo.setShopname(getString(R.string.shopname)+intent.getStringExtra("shopname"));
        itemDetailsInfo.setCheckamount(String.valueOf(intent.getDoubleExtra("checkamount", 0)));
        itemDetailsInfo.setCashamount(String.valueOf(intent.getDoubleExtra("cashamount", 0)));
        itemDetailsInfo.setRecivedAmount(String.valueOf(intent.getDoubleExtra("recivedAmount", 0)));
        itemDetailsInfo.setStatus(intent.getStringExtra("status"));
        itemDetailsInfo.setComments(intent.getStringExtra("comments"));
        itemDetailsInfo.setGrossAmount(String.valueOf(intent.getDoubleExtra("grossamnt",0)));
        itemDetailsInfo.setElectronicPaymentNo(String.valueOf(intent.getStringExtra("electronicPaymentNo")));
        itemDetailsInfo.setElectronicAmount(String.valueOf(intent.getDoubleExtra("electronicAmount",0)));
        itemLists = (List<OrderDetail>) args.getSerializable("Arraylist");
        mBinding.rvItem.setLayoutManager(new LinearLayoutManager(HistoryDetailActivity.this, LinearLayoutManager.VERTICAL, false));
        mBinding.rvItem.setHasFixedSize(true);
        adapter = new HistoryItemDetailsAdapter(HistoryDetailActivity.this, itemLists, this);
        mBinding.rvItem.setAdapter(adapter);
        bind(itemDetailsInfo);
    }

    public void bind(ItemDetailsInfo item) {
        mBinding.setItemDetailsInfo(item);
        mBinding.executePendingBindings();
    }

    @Override
    public void totalQunatity(int quatity) {
        mBinding.totalQuantity.setText(String.valueOf(quatity));
    }
    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

}
