package com.sk.ziladelivery.ui.views.main;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.vision.barcode.Barcode;
import com.sk.ziladelivery.R;
import com.sk.ziladelivery.barcode.BarcodeReader;
import com.sk.ziladelivery.databinding.ActivityScanCodeBinding;

@SuppressWarnings("ConstantConditions")
public class ScanCodeActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {
    private ActivityScanCodeBinding mBinding;
    private int intentResult;
    String intenttype, result;
    int ScanCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_scan_code);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        if (getIntent().getExtras() != null) {
            intenttype = intent.getStringExtra("type");
            if (intenttype.equalsIgnoreCase("payment")) {
                intentResult = intent.getIntExtra("orderID", 0);
                ScanCount = intent.getIntExtra("scanCount", 0);

            } else if (intenttype.equalsIgnoreCase("AssignmentBilling")) {
                intentResult = intent.getIntExtra("DeliveryIssuanceId", 0);

            } else if (intenttype.equalsIgnoreCase("online")) {
                intentResult = intent.getIntExtra("orderID", 0);

            }
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        View view_line = findViewById(R.id.view_line);
        TranslateAnimation animation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, .20f,
                TranslateAnimation.ABSOLUTE, .20f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, .68f);
        animation.setDuration(4000);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setInterpolator(new LinearInterpolator());
        view_line.startAnimation(animation);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScanned(final Barcode barcode) {
        // barcodeReader.playBeep();
        runOnUiThread(() -> {
            System.out.println("" + barcode.displayValue);

            if (getIntent().getExtras() != null) {
                result = barcode.displayValue;
                if (result == null) {
                    Log.d("QRCodeScanActivity", "Cancelled scan");
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                    finish();
                } else {

                    Log.d("QRCodeScanActivity", "Scanned");
                    String Scan = result.replaceFirst("^0+(?!$)", "");
                    String Scan2 = result.replace("AS", "00");
                    String Scan3 = Scan2.replaceFirst("^0+(?!$)", "");
                    if (Scan != null) {
                        System.out.println("Scan" + Scan);
                        if (intenttype.equalsIgnoreCase("payment")) {
                            if (Scan.equalsIgnoreCase(String.valueOf(intentResult))) {
                                Intent intent = new Intent();
                                intent.putExtra("result", Scan);
                                System.out.println("Scan" + Scan);
                                setResult(2, intent);
                                finish();
                            } else {

                                if (ScanCount > 2) {
                                    Toast.makeText(this, "Barcode Does not Match", Toast.LENGTH_SHORT).show();
                                    BarcodePopup();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Barcode Does not match", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                            }

                        } else if (intenttype.equalsIgnoreCase("AssignmentBilling")) {

                            if (Scan3.equalsIgnoreCase(String.valueOf(intentResult))) {
                                Intent intent = new Intent();
                                intent.putExtra("result", Scan3);
                                setResult(2, intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Barcode Does not match", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        } else if (intenttype.equalsIgnoreCase("search")) {
                            Intent intent = new Intent();
                            intent.putExtra("result", Scan);
                            setResult(2, intent);
                            finish();
                        } else if (intenttype.equalsIgnoreCase("online")) {
                            if (Scan.equalsIgnoreCase(String.valueOf(intentResult))) {
                                Intent intent = new Intent();
                                intent.putExtra("result", Scan);
                                System.out.println("Scan" + Scan);
                                setResult(3, intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Barcode Does not match", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                    }
                }
            }
        });
    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }

    @Override
    public void onCameraPermissionDenied() {

    }

    private void BarcodePopup() {
        final View mView = getLayoutInflater().inflate(R.layout.activity_scan_barcode, null);
        Dialog customDialog = new Dialog(this, R.style.CustomDialog);
        customDialog.setContentView(mView);
        Button okBtn = mView.findViewById(R.id.btnsubmit);
        EditText etbarcode = mView.findViewById(R.id.orderid);
        okBtn.setOnClickListener(v -> {
            String barcode = etbarcode.getText().toString();
            String Scan = barcode.replaceFirst("^0+(?!$)", "");
            if (Scan.equalsIgnoreCase(String.valueOf(intentResult))) {
                Intent intent = new Intent();
                intent.putExtra("result", Scan);
                System.out.println("Scan" + Scan);
                setResult(2, intent);
                finish();
                customDialog.dismiss();
            } else {
                Toast.makeText(this, "Barcode Does not Match with Order ID", Toast.LENGTH_SHORT).show();
            }


        });
        customDialog.show();
    }

}