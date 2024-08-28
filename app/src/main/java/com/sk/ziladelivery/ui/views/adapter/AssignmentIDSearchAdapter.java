package com.sk.ziladelivery.ui.views.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sk.ziladelivery.R;
import com.sk.ziladelivery.databinding.SearchAssignmentAdapterBinding;
import com.sk.ziladelivery.listener.AcceptClickInterface;
import com.sk.ziladelivery.data.model.AssignmentAdapterModel;
import com.sk.ziladelivery.data.model.AssinmentDetail;
import com.sk.ziladelivery.data.model.Event;
import com.sk.ziladelivery.utilities.DateUtils;
import com.sk.ziladelivery.utilities.RxBus;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AssignmentIDSearchAdapter extends RecyclerView.Adapter<AssignmentIDSearchAdapter.ViewHolder> {
    Context context;
    List<AssinmentDetail> orderassignment;
    private LayoutInflater layoutInflater;
    SearchAssignmentAdapterBinding mBinding;
    AcceptClickInterface acceptInterface;
    Event event;
    public Disposable acceptAssignDes;

    public AssignmentIDSearchAdapter(Context context, List<AssinmentDetail> orderassignment, AcceptClickInterface acceptInterface) {
        this.context = context;
        this.orderassignment = orderassignment;
        this.acceptInterface = acceptInterface;

    }

    @NonNull
    @Override
    public AssignmentIDSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
         mBinding = DataBindingUtil.inflate(layoutInflater, R.layout.search_assignment_adapter, viewGroup, false);
        handleAcceptAssignMessage();
        return new AssignmentIDSearchAdapter.ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentIDSearchAdapter.ViewHolder viewHolder, int i) {
        try {
            AssinmentDetail model = orderassignment.get(i);
            AssignmentAdapterModel assignmentAdapterModel = new AssignmentAdapterModel();
            String str = model.getOrderIds();
            String[] arrOfStr = str.split(",");
            assignmentAdapterModel.setAssignmentID(context.getString(R.string.assignment_id) + String.valueOf(model.getDeliveryIssuanceId()));
            assignmentAdapterModel.setOrderNo(context.getString(R.string.oreder_no)+ String.valueOf(arrOfStr.length));
            assignmentAdapterModel.setDateTime(DateUtils.getDateFormat(model.getCreatedDate()));
            assignmentAdapterModel.setStatus(String.valueOf(model.getStatus()));

            if(model.getStatus().equalsIgnoreCase("Assigned"))
            {
               mBinding.linearlStatus.setVisibility(View.GONE);
               mBinding.rlAcceptReject.setVisibility(View.VISIBLE);
            }
            else
                {
                mBinding.linearlStatus.setVisibility(View.VISIBLE);
                mBinding.rlAcceptReject.setVisibility(View.GONE);
            }
            viewHolder.bind(assignmentAdapterModel);

            viewHolder.mBinding.btAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        acceptInterface.acceptClicked(model.getDeliveryIssuanceId(), "true");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                }
            });

            viewHolder.mBinding.llReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        acceptInterface.rejectClicked(model.getDeliveryIssuanceId(), "false");


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
/*
                    acceptInterface.rejectClicked(model.getDeliveryIssuanceId(), "false");
*/


                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {

        return orderassignment == null ? 0 : orderassignment.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SearchAssignmentAdapterBinding mBinding;
        RecyclerView orderListRV;

        public ViewHolder(SearchAssignmentAdapterBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }

        public void bind(AssignmentAdapterModel item) {
            mBinding.setAssignmentadapterModel(item);

        }

    }
    public void handleAcceptAssignMessage() {
        acceptAssignDes = RxBus.getInstance().getEvent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (o instanceof Event) {
                            event = (Event) o;
                        }
                    }
                });
    }
}
