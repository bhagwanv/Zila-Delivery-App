package com.sk.ziladelivery.ui.views.returnorder;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.sk.ziladelivery.BuildConfig;
import com.sk.ziladelivery.R;
import com.sk.ziladelivery.data.api.RestClient;
import com.sk.ziladelivery.databinding.ActivityReturnOrderDetailBinding;
import com.sk.ziladelivery.data.model.ReturnOrderItemModel;
import com.sk.ziladelivery.data.model.ReturnOrderListModel;
import com.sk.ziladelivery.utilities.ApiResponse;
import com.sk.ziladelivery.utilities.SharePrefs;
import com.sk.ziladelivery.utilities.Utils;
import com.sk.ziladelivery.ui.views.viewmodels.ReturnOrderDetailViewModel;
//import com.sk.skdelevery.views.adapter.ReturnOrderDetailAdapter;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@SuppressWarnings("ConstantConditions")
public class ReturnOrderDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private final int WRITE_PERMISSION = 111;
    private final int CAPTURE_IMAGE_CAMERA = 100;
    private ActivityReturnOrderDetailBinding mBinding;
    private ReturnOrderDetailViewModel viewModel;
    private ReturnOrderListModel model;
    private ArrayList<ReturnOrderItemModel> list;
   // private ReturnOrderDetailAdapter adapter;
    private String uploadFilePath = "", fileName = "", uploadImagePath = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_return_order_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null) {
            model = getIntent().getParcelableExtra("list");
        }

        viewModel = ViewModelProviders.of(this).get(ReturnOrderDetailViewModel.class);
        mBinding.setReturnOrderViewModel(viewModel);
        mBinding.setLifecycleOwner(this);

        initViews();

        viewModel.returnOrderItemListResponse().observe(this, this::consumeReturnOrderResponse);
        viewModel.returnRequestStatusResponse().observe(this, this::consumeReturnRequestStatusResponse);

        viewModel.hitReturnOrderItemListAPI(model.getKkRequstId());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.rightTransaction(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivImage:
                fileName = model.getKkRequstId() + "_" + System.currentTimeMillis() + "Return.jpg";
                requestWritePermission();
                break;
            case R.id.btnAccept:
                if (model.getStatus().equals("Pending to Pick")) {
                    validateFields();
                } else if (model.getStatus().equals("Pending to Pick From Warehouse")) {
                    mBinding.btnAccept.setEnabled(false);
                    acceptOrderWh();
                } else {
                    mBinding.btnAccept.setEnabled(false);
                    acceptOrderCus();
                }
                break;
            case R.id.btnCancel:
                mBinding.btnCancel.setEnabled(false);
                showCancelAlert();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_CAMERA && resultCode == RESULT_OK) {
            Uri selectedImage;
            selectedImage = Uri.parse(uploadFilePath);

            mBinding.ivImage.setImageURI(selectedImage);
            if (Utils.isNetworkAvailable(this)) {
                System.out.println("UploadMultipart: " + fileName);
                uploadMultipart();
            } else {
                Utils.setToast(this, getString(R.string.internet_connection));
            }
            Log.d("selectedImage", selectedImage+"");

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_PERMISSION && grantResults.length > 0) {
            requestWritePermission();
        }
    }


    private void initViews() {
        getSupportActionBar().setSubtitle("Order Id : " + model.getOrderId());
//        mBinding.tvOrderId.setText("" + model.getOrderId());
//        mBinding.tvDate.setText(Utils.getDateFormat(model.getModifiedDate()));
        mBinding.tvName.setText("Name: " + model.getName());
//        mBinding.tvShopName.setText(model.getShopName());
        mBinding.tvAddress.setText("Address: " + model.getShippingAddress());
        if (model.getRequestType() == 0) {
            mBinding.tvOrderType.setText("Return Order");
        } else {
            mBinding.tvOrderType.setText("Replace Order");
        }
        mBinding.tvOrderStatus.setText("Status: " + model.getStatus());
        if (model.getStatus().equals("Pending to Pick From Warehouse")) {
            mBinding.liCheckPoint.setVisibility(View.GONE);
            mBinding.btnCancel.setVisibility(View.GONE);
        } else if (model.getStatus().equals("Picked From Warehouse")) {
            mBinding.liCheckPoint.setVisibility(View.GONE);
        } else {
            mBinding.liCheckPoint.setVisibility(View.VISIBLE);
        }

        mBinding.rvReturnItems.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mBinding.ivImage.setOnClickListener(this);
        mBinding.btnAccept.setOnClickListener(this);
        mBinding.btnCancel.setOnClickListener(this);
    }

    private void requestWritePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                        && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, WRITE_PERMISSION);
                } else {
                    startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            .setData(Uri.fromParts("package", getPackageName(), null))
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            } else {
                pickFromCamera();
            }
        } else {
            pickFromCamera();
        }
    }

    public void pickFromCamera() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File photoFile;

            photoFile = createImageFile();
            Uri photoUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        startActivityForResult(pictureIntent, CAPTURE_IMAGE_CAMERA);
    }

    private File createImageFile() {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(Environment.getExternalStorageDirectory() + "/ShopKirana");
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(storageDir, fileName);
        uploadFilePath = file.getAbsolutePath();

        return file;
    }

    public void uploadMultipart() {
        File fileToUpload = new File(uploadFilePath);
        Disposable subscribe = new Compressor(this)
                .compressToFileAsFlowable(fileToUpload)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::uploadImagePath, throwable -> {
                    throwable.printStackTrace();
                    Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void uploadImagePath(File file) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Image Uploading.");
        progressDialog.show();

        RequestBody fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), fileReqBody);

        RestClient.getInstance().getService().uploadKKReturnImage(part)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
                        progressDialog.dismiss();
                        if (result != null) {
                            uploadImagePath = fileName;
                        } else {
                            Toast.makeText(getApplicationContext(), "Unable to Uploaded Image", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Unable to Uploaded Image", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void validateFields() {
        String status = "Pick From Customer";

        if (uploadImagePath.isEmpty()) {
            Utils.setToast(getApplicationContext(), "Please upload return item images");
        } else if (!mBinding.cbQty.isChecked()) {
            Utils.setToast(getApplicationContext(), "Select if quantity is As per request");
        } else if (!mBinding.cbExpire.isChecked()) {
            Utils.setToast(getApplicationContext(), "Select if product has not expired");
        } else if (!mBinding.cbInvoice.isChecked()) {
            Utils.setToast(getApplicationContext(), "Select if you have received invoice  of requested products");
        } else if (!mBinding.cbPacking.isChecked()) {
            Utils.setToast(getApplicationContext(), "Select if Item Packing & Quantity is Proper");
        } else {
            mBinding.btnAccept.setEnabled(false);

            viewModel.hitUpdateReturnOrderStatusAPI(model.getKkRequstId(), status,
                    SharePrefs.getInstance(getApplicationContext()).getInt(SharePrefs.PEOPLE_ID), "", uploadImagePath);
        }
    }

    private void acceptOrderWh() {
        String status = "Picked From Warehouse";

        viewModel.hitUpdateReturnOrderStatusAPI(model.getKkRequstId(), status,
                SharePrefs.getInstance(getApplicationContext()).getInt(SharePrefs.PEOPLE_ID),
                "", uploadImagePath);
    }

    private void acceptOrderCus() {
        String status = "Return To Customer";
        viewModel.hitUpdateReturnOrderStatusAPI(model.getKkRequstId(), status,
                SharePrefs.getInstance(getApplicationContext()).getInt(SharePrefs.PEOPLE_ID), "", uploadImagePath);
    }

    private void showCancelAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Update Order Status");
        builder.setMessage("Are you sure, You want to cancel this order");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            dialog.dismiss();
            String status;
            if (model.getStatus().equals("Picked From Warehouse")) {
                status = "Customer Refused Rejected Order";
            } else {
                status = "Cancelled";
            }
            viewModel.hitUpdateReturnOrderStatusAPI(model.getKkRequstId(), status,
                    SharePrefs.getInstance(getApplicationContext()).getInt(SharePrefs.PEOPLE_ID), "", uploadImagePath);
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss();
            mBinding.btnCancel.setEnabled(true);
        });

        builder.show();
    }


    private void consumeReturnOrderResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                Utils.showProgressDialog(this);
                break;
            case SUCCESS:
                Utils.hideProgressDialog();
                renderSuccessResponse(apiResponse.data);
                break;
            case ERROR:
                Utils.hideProgressDialog();
                Utils.setToast(getApplicationContext(), getResources().getString(R.string.errorString));
                mBinding.btnAccept.setEnabled(true);
                break;
            default:
                break;
        }
    }

    private void consumeReturnRequestStatusResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                Utils.showProgressDialog(this);
                break;
            case SUCCESS:
                Utils.hideProgressDialog(this);
                if (apiResponse.data.getAsBoolean()) {
                    Utils.setToast(getBaseContext(), "Your request submitted successfully");
                    Intent intent = new Intent();
                    setResult(Activity.RESULT_OK, intent);
                    onBackPressed();
                } else {
                    Utils.setToast(getBaseContext(), "Unable to submit your request");
                }
                break;
            case ERROR:
                Utils.hideProgressDialog(this);
                Utils.setToast(this, getResources().getString(R.string.errorString));
                break;
            default:
                break;
        }
        mBinding.btnAccept.setEnabled(true);
        mBinding.btnCancel.setEnabled(true);
    }

    private void renderSuccessResponse(JsonElement element) {
        try {
            if (element != null) {
                JSONArray array = new JSONArray(element.toString());
                list = new Gson().fromJson(array.toString(), new TypeToken<ArrayList<ReturnOrderItemModel>>() {
                }.getType());
               /* adapter = new ReturnOrderDetailAdapter(this, list);
                mBinding.rvReturnItems.setAdapter(adapter);*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}