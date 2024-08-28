package com.sk.ziladelivery.ui.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sk.ziladelivery.R;
import com.sk.ziladelivery.data.model.DeliveryIssuanceModel;

import java.util.ArrayList;

public class AssignmentAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<DeliveryIssuanceModel> deliveryIssuanceModels;
    LayoutInflater inflater;


    public AssignmentAdapter(Context context, ArrayList<DeliveryIssuanceModel> items) {
        this.context = context;
        this.deliveryIssuanceModels = items;
        inflater = (LayoutInflater.from(context));
    }

    public void setAssignmentData(ArrayList<DeliveryIssuanceModel> items){
        this.deliveryIssuanceModels = items;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        if (deliveryIssuanceModels != null)
            return deliveryIssuanceModels.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.items_assignment, null);
        DeliveryIssuanceModel supplierModel = deliveryIssuanceModels.get(i);
        TextView SupplierNameTV = view.findViewById(R.id.tv_assignment);
        SupplierNameTV.setText(supplierModel.getDeliveryIssuanceId());
        return view;
    }
}
