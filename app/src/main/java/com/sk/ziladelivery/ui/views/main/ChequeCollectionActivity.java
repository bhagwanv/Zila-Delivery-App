package com.sk.ziladelivery.ui.views.main;

import static com.google.firebase.messaging.Constants.TAG;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.sk.ziladelivery.R;
import com.sk.ziladelivery.data.api.RestClient;
import com.sk.ziladelivery.data.model.AssignmentID;
import com.sk.ziladelivery.data.model.BankNameModel;
import com.sk.ziladelivery.data.model.DeliveryIssuanceDetail;
import com.sk.ziladelivery.data.model.PostChequeCollectionModel;
import com.sk.ziladelivery.databinding.ChequemanagmentfragmentBinding;
import com.sk.ziladelivery.listener.ChequeImageListener;
import com.sk.ziladelivery.ui.views.adapter.ChequeCollectionAdapter;
import com.sk.ziladelivery.ui.views.viewmodels.SettleAssignmentViewModel;
import com.sk.ziladelivery.utilities.ApiResponse;
import com.sk.ziladelivery.utilities.CommonMethods;
import com.sk.ziladelivery.utilities.Logger;
import com.sk.ziladelivery.utilities.MyApplication;
import com.sk.ziladelivery.utilities.SharePrefs;
import com.sk.ziladelivery.utilities.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ChequeCollectionActivity extends AppCompatActivity implements ChequeImageListener {
    private ChequemanagmentfragmentBinding mBinding;
    private ArrayList<DeliveryIssuanceDetail> deliveryIssuanceDetailsList;
    private String historyModel;
    private String selectedAssignmentId;
    private ArrayList<PostChequeCollectionModel> list;
    private ArrayList<PostChequeCollectionModel> savelist;
    private ChequeCollectionAdapter adapter;
    // adapter views
    private ImageView imageView;
    private int position;
    private double Checkamount;
    public Dialog customDialog;
    private ArrayList<String> BankNameList = new ArrayList<>();
    public static final int REQUEST_IMAGE = 100;
    public static final int GALLERY_REQUST = 200;

    private String ChequeimgUrl = "";
    private Uri mCropImageUri;
    private String uploadFilePath;
    private String fCheckName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.chequemanagmentfragment);
        SharePrefs.getInstance(ChequeCollectionActivity.this).putBoolean(SharePrefs.UPDATE_DATA, true);
        initView();
    }

    private void initView() {
        try {
            list = new ArrayList<>();
            savelist = new ArrayList<>();
            mBinding.back.setOnClickListener(v -> ChequeCollectionActivity.this.onBackPressed());
            mBinding.recyclerCheque.setLayoutManager(new LinearLayoutManager(ChequeCollectionActivity.this));
            mBinding.recyclerCheque.addItemDecoration(new DividerItemDecoration(ChequeCollectionActivity.this, DividerItemDecoration.VERTICAL));
            boolean flag = SharePrefs.getInstance(ChequeCollectionActivity.this).getBoolean(SharePrefs.FLAG);
            if (flag) {
                ArrayList<AssignmentID> DeliveryIssuanceDcs;
                String CheckAmount = SharePrefs.getCashmanagmentSharedPreferences(ChequeCollectionActivity.this, SharePrefs.CHEQUE_AMOUNT);
                String Assignmodel = SharePrefs.getCashmanagmentSharedPreferences(ChequeCollectionActivity.this, SharePrefs.ASSIGN_MODEL);
                selectedAssignmentId = SharePrefs.getInstance(ChequeCollectionActivity.this).getString(SharePrefs.ASSIGN_ID);
                DeliveryIssuanceDcs = new Gson().fromJson(Assignmodel, new TypeToken<ArrayList<AssignmentID>>() {
                }.getType());
                if (CheckAmount != null) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<PostChequeCollectionModel>>() {
                    }.getType();
                    savelist = gson.fromJson(CheckAmount, type);
                    if (savelist == null) {
                        DeliveryIssuanceDcs = new Gson().fromJson(Assignmodel, new TypeToken<ArrayList<AssignmentID>>() {
                        }.getType());
                        setData(selectedAssignmentId + "", DeliveryIssuanceDcs);

                        for (int i = 0; i < deliveryIssuanceDetailsList.size(); i++) {
                            for (int j = 0; j < deliveryIssuanceDetailsList.get(i).getPaymentDetails().size(); j++) {
                                if (deliveryIssuanceDetailsList.get(i).getPaymentDetails().get(j).getPaymentFrom().equalsIgnoreCase("Cheque")) {
                                    Checkamount = Checkamount + deliveryIssuanceDetailsList.get(i).getPaymentDetails().get(j).getAmount();
                                    list.add(new PostChequeCollectionModel(deliveryIssuanceDetailsList.get(i).getPaymentDetails().get(j).getAmount(), deliveryIssuanceDetailsList.get(i).getPaymentDetails().get(j).getTransDate(), deliveryIssuanceDetailsList.get(i).getOrderId() + "", deliveryIssuanceDetailsList.get(i).getPaymentDetails().get(j).getAmount(), deliveryIssuanceDetailsList.get(i).getOrderId(), deliveryIssuanceDetailsList.get(i).getPaymentDetails().get(j).getTransRefNo(), deliveryIssuanceDetailsList.get(i).getPaymentDetails().get(j).getChequeImageUrl(), "", deliveryIssuanceDetailsList.get(i).getPaymentDetails().get(j).getChequeBankName()));
                                }
                            }
                        }
                    } else {
                        for (int i = 0; i < savelist.size(); i++) {
                            list.add(new PostChequeCollectionModel(savelist.get(i).getUsedChequeAmt(), savelist.get(i).getChequeDate(), savelist.get(i).getOrderid() + "", savelist.get(i).getChequeAmt(), Integer.parseInt(savelist.get(i).getOrderid()), savelist.get(i).getChequeNumber(), savelist.get(i).getChequeimagePath(), "", savelist.get(i).getChequeBankName()));
                            Checkamount = Checkamount + savelist.get(i).getChequeAmt();
                        }
                    }
                } else {
                    DeliveryIssuanceDcs = new Gson().fromJson(Assignmodel, new TypeToken<ArrayList<AssignmentID>>() {
                    }.getType());
                    setData(selectedAssignmentId + "", DeliveryIssuanceDcs);

                    for (int i = 0; i < deliveryIssuanceDetailsList.size(); i++) {
                        for (int j = 0; j < deliveryIssuanceDetailsList.get(i).getPaymentDetails().size(); j++) {
                            if (deliveryIssuanceDetailsList.get(i).getPaymentDetails().get(j).getPaymentFrom().equalsIgnoreCase("Cheque")) {
                                Checkamount = Checkamount + deliveryIssuanceDetailsList.get(i).getPaymentDetails().get(j).getAmount();
                                list.add(new PostChequeCollectionModel(0.0, deliveryIssuanceDetailsList.get(i).getPaymentDetails().get(j).getTransDate(), deliveryIssuanceDetailsList.get(i).getOrderId() + "", deliveryIssuanceDetailsList.get(i).getPaymentDetails().get(j).getAmount(), deliveryIssuanceDetailsList.get(i).getOrderId(), deliveryIssuanceDetailsList.get(i).getPaymentDetails().get(j).getTransRefNo(), deliveryIssuanceDetailsList.get(i).getPaymentDetails().get(j).getChequeImageUrl(), "", deliveryIssuanceDetailsList.get(i).getPaymentDetails().get(j).getChequeBankName()));
                            }
                        }

                    }
                }
                mBinding.chequePaidamnt.setText(String.format("₹ %s", Checkamount));

            } else {
                historyModel = SharePrefs.getCashmanagmentSharedPreferences(ChequeCollectionActivity.this, SharePrefs.HISTORY_MODEL);
                setHistoryData(historyModel);
            }
            SettleAssignmentViewModel settleAssignmentViewModel = ViewModelProviders.of(this).get(SettleAssignmentViewModel.class);

            settleAssignmentViewModel.getBankname().observe(this, apiResponse -> {
                if (apiResponse != null) {
                    consumeResponseForBankname(apiResponse);
                }
            });
            settleAssignmentViewModel.hitBankName();

            mBinding.btnSubmit.setOnClickListener(v -> {
                adapter.getListFromAdapter();

            });
            //onCropImageClick();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void consumeResponseForBankname(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                break;
            case SUCCESS:
                renderSuccessResponseForBankname(apiResponse.data);
                break;
            case ERROR:
                Utils.setToast(ChequeCollectionActivity.this, getResources().getString(R.string.errorString));
                break;
            default:
                break;
        }
    }

    private void renderSuccessResponseForBankname(JsonElement response) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull()) {

                Logger.d(CommonMethods.getTag(this), response.toString());
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    BankNameModel bankNameModel = new Gson().fromJson(obj.toString(), BankNameModel.class);
                    if (bankNameModel.isStatus()) {
                        BankNameList.add("Select Bank Name");
                        for (int i = 0; i < bankNameModel.getBankNameDc().size(); i++) {
                            String bankname = bankNameModel.getBankNameDc().get(i).getBankName();
                            BankNameList.add(bankname);
                        }
                        adapter = new ChequeCollectionAdapter(ChequeCollectionActivity.this, list, BankNameList, this);
                        mBinding.recyclerCheque.setAdapter(adapter);
                    } else {
                        Utils.setToast(ChequeCollectionActivity.this, obj.getString("Message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(ChequeCollectionActivity.this, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ChequeCollectionActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setHistoryData(String historyModel) {
        try {
            String CheckAmount = SharePrefs.getCashmanagmentSharedPreferences(ChequeCollectionActivity.this, SharePrefs.CHEQUE_AMOUNT);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<PostChequeCollectionModel>>() {
            }.getType();
            if (CheckAmount != null) {
                savelist = gson.fromJson(CheckAmount, type);
                if (savelist == null) {
                    JSONObject obj = new JSONObject(historyModel);
                    ArrayList<PostChequeCollectionModel> CollectionArrayList = gson.fromJson(obj.get("ChequeCollections").toString(), type);
                    for (int i = 0; i < CollectionArrayList.size(); i++) {
                        Checkamount = Checkamount + CollectionArrayList.get(i).getChequeAmt();
                        list.add(new PostChequeCollectionModel(CollectionArrayList.get(i).getChequeAmt(), CollectionArrayList.get(i).getChequeDate(), CollectionArrayList.get(i).getOrderid() + "", CollectionArrayList.get(i).getChequeAmt(), 0, CollectionArrayList.get(i).getChequeNumber(), CollectionArrayList.get(i).getChequeimagePath(), "", CollectionArrayList.get(i).getChequeBankName()));
                    }
                } else {
                    for (int i = 0; i < savelist.size(); i++) {
                        list.add(new PostChequeCollectionModel(savelist.get(i).getUsedChequeAmt(), savelist.get(i).getChequeDate(), savelist.get(i).getOrderid() + "", savelist.get(i).getChequeAmt(), Integer.parseInt(savelist.get(i).getOrderid()), savelist.get(i).getChequeNumber(), savelist.get(i).getChequeimagePath(), "", savelist.get(i).getChequeBankName()));
                        Checkamount = Checkamount + savelist.get(i).getChequeAmt();

                    }
                }
            } else {
                JSONObject obj = new JSONObject(historyModel);
                ArrayList<PostChequeCollectionModel> CollectionArrayList = gson.fromJson(obj.get("ChequeCollections").toString(), type);
                for (int i = 0; i < CollectionArrayList.size(); i++) {
                    Checkamount = Checkamount + CollectionArrayList.get(i).getChequeAmt();
                    list.add(new PostChequeCollectionModel(CollectionArrayList.get(i).getChequeAmt(), CollectionArrayList.get(i).getChequeDate(), CollectionArrayList.get(i).getOrderid() + "", CollectionArrayList.get(i).getChequeAmt(), 0, CollectionArrayList.get(i).getChequeNumber(), CollectionArrayList.get(i).getChequeimagePath(), "", CollectionArrayList.get(i).getChequeBankName()));
                }
            }
            mBinding.chequePaidamnt.setText(String.format("₹ %s", Checkamount));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setActionBarConfiguration() {
        final DrawerLayout drawer = ChequeCollectionActivity.this.findViewById(R.id.container);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        Toolbar toolbar = ChequeCollectionActivity.this.findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
    }

    private void setData(String assignmentID, ArrayList<AssignmentID> deliveryIssuanceDcs) {
        try {
            for (int i = 0; i < deliveryIssuanceDcs.size(); i++) {
                String AssignmentID = String.valueOf(deliveryIssuanceDcs.get(i).getDeliveryIssuanceId());
                if (AssignmentID.equalsIgnoreCase(assignmentID)) {
                    deliveryIssuanceDetailsList = deliveryIssuanceDcs.get(i).getDeliveryIssuanceDetailDcs();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onImageClick(ImageView imageView, int position) {
        this.imageView = imageView;
        this.position = position;
        // callForImage();
        fCheckName = list.get(position).getOrderid() + "_" + System.currentTimeMillis() + "CheckNo.jpg";
        requestWritePermission();
    }



    @Override
    public void onButtonClick(ArrayList<PostChequeCollectionModel> list, boolean save) {
        /*if (save) {*/
        boolean isboolean = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getChequeBankName() == null) {
                Toast.makeText(ChequeCollectionActivity.this, R.string.plz_select_Bank_name, Toast.LENGTH_SHORT).show();
                isboolean = false;
                break;
            } else if (list.get(i).getChequeBankName().equalsIgnoreCase("Select Bank Name")) {
                Toast.makeText(ChequeCollectionActivity.this, R.string.plz_select_Bank_name, Toast.LENGTH_SHORT).show();
                isboolean = false;
                break;

            } else if (list.get(i).getChequeDate().equalsIgnoreCase("")) {
                Toast.makeText(ChequeCollectionActivity.this, R.string.enter_check_date, Toast.LENGTH_SHORT).show();
                isboolean = false;
                break;
            } else if (list.get(i).getChequeimagePath().equalsIgnoreCase("")) {
                Toast.makeText(ChequeCollectionActivity.this, R.string.plz_upload_check_images, Toast.LENGTH_SHORT).show();
                isboolean = false;
                break;
            } else if (list.get(i).getUsedChequeAmt() == 0.0) {
                Toast.makeText(ChequeCollectionActivity.this, R.string.enter_cheque_amt, Toast.LENGTH_SHORT).show();
                isboolean = false;
                break;
            } else if (list.get(i).getChequeDate().equalsIgnoreCase("")) {
                Toast.makeText(ChequeCollectionActivity.this, R.string.enter_check_date, Toast.LENGTH_SHORT).show();
                isboolean = false;
                break;
            } else {
                isboolean = true;
            }
        }
        if (isboolean) {
            SubmitPopup();
        }
        /*} else {
            Toast.makeText(ChequeCollectionActivity.this, R.string.first_save, Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        //SharePrefs.setCashmanagmentSharedPreference(ChequeCollectionActivity.this, SharePrefs.CHEQUE_AMOUNT, null);

    }

    private void SubmitPopup() {
        LayoutInflater inflater = getLayoutInflater();
        if (ChequeCollectionActivity.this != null && inflater != null) {
            final View mView = inflater.inflate(R.layout.confirmation_popup, null);
            customDialog = new Dialog(ChequeCollectionActivity.this, R.style.CustomDialog);
            customDialog.setContentView(mView);
            TextView okBtn = mView.findViewById(R.id.btn_yes);
            TextView cancelBtn = mView.findViewById(R.id.btn_no);
            TextView textMesaage = mView.findViewById(R.id.tv_txt);
            textMesaage.setText(ChequeCollectionActivity.this.getResources().getString(R.string.check_summary));
            okBtn.setOnClickListener(v -> {
                Gson gson = new Gson();
                SharePrefs.setCashmanagmentSharedPreference(ChequeCollectionActivity.this, SharePrefs.CHEQUE_AMOUNT, gson.toJson(this.list));
                System.out.println("ChequeJson" + SharePrefs.getCashmanagmentSharedPreferences(ChequeCollectionActivity.this, SharePrefs.CHEQUE_AMOUNT));
                ChequeCollectionActivity.this.onBackPressed();
                customDialog.dismiss();
            });
            cancelBtn.setOnClickListener(v -> customDialog.dismiss());
            customDialog.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (resultCode == Activity.RESULT_OK) {
            mBinding.CropView.setVisibility(View.VISIBLE);
            mBinding.llMainView.setVisibility(View.GONE);
            Uri imageUri = CropImage.getPickImageResultUri(ChequeCollectionActivity.this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage,
            // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
            boolean requirePermissions = false;
            if (CropImage.isReadExternalStoragePermissionsRequired(ChequeCollectionActivity.this, imageUri)) {

                // request permissions and handle the result in onRequestPermissionsResult()
                requirePermissions = true;
                mCropImageUri = imageUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }
            }

            if (!requirePermissions) {
                mBinding.CropImageView.setImageUriAsync(imageUri);

            }
        }*/
        if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK) {
            uploadMultipart();
        } else if ((requestCode == GALLERY_REQUST && resultCode == Activity.RESULT_OK && null != data)) {
            Uri selectedImageUri = data.getData();
            String[] projection = {MediaStore.MediaColumns.DATA};
            Cursor cursor = managedQuery(selectedImageUri, projection, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            String selectedImagePath = cursor.getString(column_index);
            Bitmap bm;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(selectedImagePath, options);
            final int REQUIRED_SIZE = 200;
            int scale = 1;
            while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;
            options.inSampleSize = scale;
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeFile(selectedImagePath, options);
            uploadFilePath = SavedImages(bm);
            uploadMultipart();

        }

    }

   /* @Override
    public void onGetCroppedImageComplete(CropImageView view, Bitmap bitmap, Exception error) {
        if (error == null) {
            if (bitmap != null) {
                mBinding.CropImageView.setImageBitmap(bitmap);
            }
        } else {
            Log.e("Crop", "Failed to crop image", error);
            Toast.makeText(ChequeCollectionActivity.this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
        }
    }*/

   /* @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
        if (error != null) {
            Log.e("Crop", "Failed to load image for cropping", error);
            Toast.makeText(ChequeCollectionActivity.this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
        }
    }*/

    /*public void onCropImageClick() {
        mBinding.cropBtn.setOnClickListener(view -> {
//                mBinding.CropImageView.getCroppedImageAsync();
            Bitmap cropped = mBinding.CropImageView.getCroppedImage();
            imageView.setImageBitmap(cropped);
            SavedImages(cropped);
            mBinding.CropImageView.setImageBitmap(cropped);
            mBinding.CropView.setVisibility(View.GONE);
            mBinding.llMainView.setVisibility(View.VISIBLE);

        });
    }*/

    private void requestWritePermission() {
        String[] permissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES};
        }else{
            permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }
        Permissions.check(this/*context*/,  permissions, null, null, new PermissionHandler() {
            @Override
            public void onGranted() {
                CapturePhotoPopup();
            }

        });

    }

    private void CapturePhotoPopup() {
        LayoutInflater inflater = getLayoutInflater();
        final View mView = inflater.inflate(R.layout.capture_photo_view, null);
        Dialog dialog = new Dialog(this, R.style.CustomDialog);
        dialog.setContentView(mView);
        TextView takePhoto = mView.findViewById(R.id.take_photo);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromCamera();
                dialog.dismiss();
            }
        });
        TextView gallery = mView.findViewById(R.id.gallery);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLERY_REQUST);
                dialog.dismiss();
            }
        });
        TextView cancel = mView.findViewById(R.id.Cancel);
        cancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    public void pickFromCamera() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File photoFile;
            try {
                photoFile = createImageFile();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            Uri photoUri = FileProvider.getUriForFile(this, this.getPackageName() + ".provider", photoFile);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(pictureIntent, REQUEST_IMAGE);
    }

    private File createImageFile() {
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File myDir = new File(Environment.getExternalStorageDirectory() + "/ShopKirana");
        myDir.mkdirs();
        File file = new File(storageDir, fCheckName);
        uploadFilePath = file.getAbsolutePath();
        return file;
    }

    public String SavedImages(Bitmap bm) {
        Uri imageUri = null;
        OutputStream fos;
        String root = "";

        String directory = Environment.DIRECTORY_PICTURES;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            try {
                root = String.valueOf(Environment.getExternalStorageDirectory());
                ContentResolver resolver = this.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fCheckName);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, directory);
                imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                fos = resolver.openOutputStream(imageUri);
                boolean saved = bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
                Log.d(TAG, "SavedImages:" + root + File.separator + directory + File.separator + fCheckName);
                // uploadMultipart();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return uploadFilePath = root + File.separator + directory + File.separator + fCheckName;


        } else {
            try {
                String roott = getExternalFilesDir(null).toString();
                File myDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ShopKirana");
                myDir.mkdirs();
                File file = new File(myDir, fCheckName);
                if (file.exists())
                    file.delete();

                FileOutputStream out = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                uploadFilePath = roott + "/ShopKirana/" + fCheckName;
                //uploadMultipart();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return uploadFilePath;
        }
    }

    /**
     * Upload images
     **/
    public void uploadMultipart() {
        try {
            final File fileToUpload = new File(uploadFilePath);
            final Disposable subscribe = new Compressor(ChequeCollectionActivity.this)
                    .compressToFileAsFlowable(fileToUpload)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(file -> {
                        RequestBody fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        MultipartBody.Part part = MultipartBody.Part.createFormData("file", fileToUpload.getName(), fileReqBody);
                        UploadChequeImage(getimageResponse, part);
                    }, throwable -> {
                        throwable.printStackTrace();
                        showError(throwable.getMessage());
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showError(String errorMessage) {
        Toast.makeText(ChequeCollectionActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void UploadChequeImage(DisposableObserver observer, MultipartBody.Part image) {
        RestClient.getInstance().getService().UploadCheque(image)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> Utils.showProgressDialog(this))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String object) {
                        observer.onNext(object);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("TAG", "Error:" + e.toString());
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();

                    }
                });
    }

    private DisposableObserver<String> getimageResponse = new DisposableObserver<String>() {
        @Override
        public void onNext(String response) {
            try {
                Utils.hideProgressDialog(ChequeCollectionActivity.this);
                if (response != null) {
                    ChequeimgUrl = response.replace("\"", "");
                    Log.e("Success", "Success" + ChequeimgUrl);
                    list.get(position).setChequeimagePath(ChequeimgUrl);
                    // Glide.with(ChequeCollectionActivity.this).load(BuildConfig.apiEndpoint + ChequeimgUrl).into(imageView);
                    Picasso.get().load(SharePrefs.getInstance(MyApplication.getInstance()).getString(SharePrefs.BASEURL) + ChequeimgUrl).into(imageView);
                    Toast.makeText(ChequeCollectionActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();

                } else {
                    Log.e("Failed", "Failed ####  " + response);
                    Picasso.get().load(R.drawable.ic_add_image_icon).placeholder(R.drawable.ic_add_image_icon).into(imageView);
                    Toast.makeText(ChequeCollectionActivity.this, "Image Not Uploaded", Toast.LENGTH_SHORT).show();

                }

            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            Utils.hideProgressDialog(ChequeCollectionActivity.this);
        }

        @Override
        public void onComplete() {
            Utils.hideProgressDialog(ChequeCollectionActivity.this);
        }
    };
}