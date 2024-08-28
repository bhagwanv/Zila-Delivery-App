package com.sk.ziladelivery.ui.views.adapter;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sk.ziladelivery.R;
import com.sk.ziladelivery.databinding.MultipleassginmentAdapterBinding;
import com.sk.ziladelivery.listener.MultipleAssignmentInterface;
import com.sk.ziladelivery.data.model.AcceptedAssginmentListModel;
import com.sk.ziladelivery.data.model.SortOrderModel;
import com.sk.ziladelivery.utilities.CustomRunnable;
import com.sk.ziladelivery.utilities.Utils;

import java.util.ArrayList;


public class MultipleAssignmentAdapter extends RecyclerView.Adapter<MultipleAssignmentAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    AppCompatActivity context;
    private ArrayList<AcceptedAssginmentListModel> assignmentAcceptPending;
    private MultipleAssignmentInterface multipleAssignmentInterface;
    private boolean isTimerstart = false;
    private MultipleassginmentAdapterBinding mBinding;

    public MultipleAssignmentAdapter(AppCompatActivity context, ArrayList<AcceptedAssginmentListModel> assignmentAcceptPending, MultipleAssignmentInterface multipleAssignmentInterface) {
        this.assignmentAcceptPending = assignmentAcceptPending;
        this.context = context;
        this.multipleAssignmentInterface = multipleAssignmentInterface;
    }

    public void setItemListCategory(ArrayList<AcceptedAssginmentListModel> assignmentAcceptPending, MultipleAssignmentInterface multipleAssignmentInterface) {
        this.assignmentAcceptPending = assignmentAcceptPending;
        this.multipleAssignmentInterface = multipleAssignmentInterface;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MultipleAssignmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        mBinding = DataBindingUtil.inflate(layoutInflater, R.layout.multipleassginment_adapter, viewGroup, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MultipleAssignmentAdapter.ViewHolder viewHolder, int i) {
        try {
            AcceptedAssginmentListModel assginmentListModel = assignmentAcceptPending.get(i);
            viewHolder.assignId.setText(context.getString(R.string.assignment_id) + assginmentListModel.getDeliveryIssuanceId());
            viewHolder.date.setText(context.getString(R.string.assignment_date) + Utils.getDateFormat(assginmentListModel.getAssignmentDate()));
            if (assginmentListModel.getIsDirectionExist()) {
                mBinding.llDtimeDetail.setVisibility(View.VISIBLE);
                mBinding.tvAssginDuration.setText((Utils.calculateTime(assginmentListModel.getAssignmentDuration() + assginmentListModel.getTotalUnloadingDuration())+" hr"));
                mBinding.tvAssginmentDistance.setText(Math.round(assginmentListModel.getAssignmentDistance() / 1000) + "KM");
                mBinding.tvReturnDistance.setText(Math.round(assginmentListModel.getReturnDistance() / 1000) + "KM");
                mBinding.tvReturnDuration.setText(Utils.calculateTime((assginmentListModel.getReturnDuration()))+" hr");
            } else {
                mBinding.llDtimeDetail.setVisibility(View.GONE);
               /* if (MyApplication.getInstance().noteRepository.isAssginIdExist(assginmentListModel.getDeliveryIssuanceId())) {
                    AcceptedAssginmentListModel model=MyApplication.getInstance().noteRepository.getdetail(assginmentListModel.getDeliveryIssuanceId());
                    mBinding.llDtimeDetail.setVisibility(View.VISIBLE);
                    mBinding.tvAssginDuration.setText("Completion time  : " + (Utils.calculateTime(model.getAssignmentDuration() + model.getTotalUnloadingDuration())));
                    mBinding.tvAssginmentDistance.setText("Going Distance : " + (model.getAssignmentDistance() / 1000) + "KM");
                    mBinding.tvReturnDistance.setText("Return Distance   :   " + (model.getReturnDistance() / 1000) + "KM");
                    mBinding.tvReturnDuration.setText("Return Duration   :   " + Utils.calculateTime((model.getReturnDuration())));
                }*/

            }

            if (assginmentListModel.getStartDateTime() != null) {
                viewHolder.tvTimerLayout.setVisibility(View.VISIBLE);
                viewHolder.llStart.setVisibility(View.GONE);
                viewHolder.mBinding.llDirection.setVisibility(View.VISIBLE);
               /* long currMillis = System.currentTimeMillis();
                SimpleDateFormat sdf1 = new SimpleDateFormat(Utils.myFormat, Locale.getDefault());
                sdf1.setTimeZone(TimeZone.getDefault());
                Date startTime = sdf1.parse(assginmentListModel.getStartDateTime());
                long startEpoch = startTime.getTime();
                System.out.println("startEpoch" + startEpoch);
                long millse = currMillis - startEpoch;
                System.out.println("millse" + millse);*/
                viewHolder.timer(Utils.getTimer(assginmentListModel.getStartDateTime()), viewHolder.tvtimer);

            } else {
                viewHolder.tvTimerLayout.setVisibility(View.GONE);
                viewHolder.llStart.setVisibility(View.VISIBLE);
                viewHolder.mBinding.llDirection.setVisibility(View.GONE);
//                viewHolder.timer("2019-07-09T15:17:15.8404991+05:30", viewHolder.tvTtimer);
            }

            viewHolder.llStart.setOnClickListener(v -> {
                viewHolder.mBinding.llDirection.setVisibility(View.VISIBLE);
                multipleAssignmentInterface.StartTimer(assginmentListModel.getDeliveryIssuanceId());

            });
            viewHolder.llOrderList.setOnClickListener(v -> {
                isTimerstart = assginmentListModel.getStartDateTime() != null;
                multipleAssignmentInterface.Details(assginmentListModel.getDeliveryIssuanceId(), isTimerstart, i);
            });

            viewHolder.mBinding.llDirection.setOnClickListener(view -> {
                double latitude = 0, longitutde = 0;
                latitude = Utils.getDoubleLat(context);
                longitutde = Utils.getDoubleLag(context);

                SortOrderModel sortOrderModel = new SortOrderModel(assginmentListModel.getDeliveryIssuanceId(), latitude, longitutde);
                multipleAssignmentInterface.shortPathData(sortOrderModel, i, assginmentListModel.getDeliveryIssuanceId());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return assignmentAcceptPending.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MultipleassginmentAdapterBinding mBinding;
        private TextView tvtimer, assignId, date;
        LinearLayout llStart, tvTimerLayout, llOrderList;
        private CustomRunnable customRunnable;
        Handler handler = new Handler();

        public ViewHolder(MultipleassginmentAdapterBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            assignId = mBinding.assignId;
            date = mBinding.date;
            tvtimer = mBinding.tvTimerItem;
            llStart = mBinding.llStart;
            tvTimerLayout = mBinding.tvTimerLayout;
            llOrderList = mBinding.llOrderList;
            customRunnable = new CustomRunnable(handler, tvtimer, 10000);
        }

        public void timer(long millse, TextView tvtimer) {
            try {
                handler.removeCallbacks(customRunnable);
                customRunnable.holder = tvtimer;
                customRunnable.millisUntilFinished = millse;
                handler.postDelayed(customRunnable, 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}