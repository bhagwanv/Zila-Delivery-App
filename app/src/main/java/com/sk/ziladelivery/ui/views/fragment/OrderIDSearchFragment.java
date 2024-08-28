package com.sk.ziladelivery.ui.views.fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.sk.ziladelivery.R;
import com.sk.ziladelivery.databinding.OrderidSearchBinding;
import com.sk.ziladelivery.data.model.OrderValueModel;
import com.sk.ziladelivery.data.model.SearchOrderIDResponseModel;
import com.sk.ziladelivery.utilities.ApiResponse;
import com.sk.ziladelivery.utilities.CommonMethods;
import com.sk.ziladelivery.utilities.DateUtils;
import com.sk.ziladelivery.utilities.Logger;
import com.sk.ziladelivery.utilities.SharePrefs;
import com.sk.ziladelivery.utilities.Utils;
import com.sk.ziladelivery.ui.views.viewmodels.AssignmentSearchViewModel;
import com.sk.ziladelivery.ui.views.main.BarcodeScanActivity;
import com.sk.ziladelivery.ui.views.main.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderIDSearchFragment extends Fragment {
    private Context context;
    OrderidSearchBinding mBinding;
    AssignmentSearchViewModel assignmentSearchViewModel;
    private String searchString = "";
    RecyclerView rvassginmentSearch;
    MainActivity activity;
    String barcoderesult;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity =(MainActivity) context;
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 2) {
            try {
                barcoderesult = data.getStringExtra("result");
                mBinding.searchTxt.setText(barcoderesult);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.orderid_search, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        assignmentSearchViewModel = ViewModelProviders.of(this).get(AssignmentSearchViewModel.class);
        mBinding.searchTxt.setCursorVisible(true);
        mBinding.searchTxt.setHint("Search Order");
        mBinding.searchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchString = editable.toString().trim();
                /***
                 * Search API call
                 * **/
                int peopleId = SharePrefs.getInstance(getActivity()).getInt(SharePrefs.PEOPLE_ID);
                    if (!Utils.checkInternetConnection(getActivity()))
                    {
                        Utils.setToast(getActivity(), getResources().getString(R.string.network_error));
                    }
                    else
                        {
                        assignmentSearchViewModel.hitOrderIDSearchApi(searchString,peopleId);
                    }

            }
        });

        assignmentSearchViewModel.OrderIDsearchResponse().observe(this, new Observer<ApiResponse>() {
            @Override
            public void onChanged(@Nullable ApiResponse apiResponse) {
                consumeResponse(apiResponse);
            }
        });
    }
    private void initView() {
        mBinding.barcodeScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, BarcodeScanActivity.class);
                intent.putExtra("type", "search");
                startActivityForResult(intent, 2);
            }
        });

    }
    private void consumeResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                mBinding.progressBid.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                mBinding.progressBid.setVisibility(View.GONE);
                renderSuccessResponse(apiResponse.data);
                break;

            case ERROR:
                mBinding.progressBid.setVisibility(View.GONE);
               // Utils.setToast(getActivity(), getResources().getString(R.string.errorString));
                break;

            default:
                break;
        }

    }
    private void renderSuccessResponse(JsonElement response) {
        try {
            if (Utils.isJSONValid(response.toString())) {
                if (!response.isJsonNull()) {
                    Logger.e(CommonMethods.getTag(this), response.toString());
                    try {
                        JSONObject obj = new JSONObject(response.toString());
                        SearchOrderIDResponseModel searchResponseModel = new Gson().fromJson(obj.toString(), SearchOrderIDResponseModel.class);
                        if (searchResponseModel.isStatus())
                        {
                            mBinding.status.setVisibility(View.VISIBLE);
                            OrderValueModel orderValueModel = new OrderValueModel();
                            orderValueModel.setAssignmentID(activity.getString(R.string.assignment_id)+ String.valueOf(searchResponseModel.getOrderdata().getDeliveryIssuanceId()));
                            orderValueModel.setDateTime(DateUtils.getDateFormat((searchResponseModel.getOrderdata().getCreatedDate())));
                            orderValueModel.setOrderNo(activity.getString(R.string.OrderID) + String.valueOf(searchResponseModel.getOrderdata().getOrderId()));
                            orderValueModel.setStatus(String.valueOf(searchResponseModel.getOrderdata().getStatus()));
                            orderValueModel.setOrderCount(activity.getString(R.string.oreder_no)  + String.valueOf(searchResponseModel.getOrderdata().getOrderCount()));
                            bind(orderValueModel);

                        } else {
                            mBinding.status.setVisibility(View.GONE);
                            Utils.setToast(getActivity(), getResources().getString(R.string.no_data_error));

                        }

                    } catch (JSONException e) {
                        Logger.e(CommonMethods.getTag(this), e.toString());
                    }
                } else {
                   // Toast.makeText(getActivity(), getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();

            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }
    public void bind(OrderValueModel item) {
        mBinding.setOrdervalueModel(item);

    }


}


