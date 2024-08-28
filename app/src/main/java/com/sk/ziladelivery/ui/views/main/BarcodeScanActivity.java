package com.sk.ziladelivery.ui.views.main;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class BarcodeScanActivity extends AppCompatActivity {
    private int intentResult;
    String intenttype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // mBinding = DataBindingUtil.setContentView(this, R.layout.activity_bar_code_item);
        if (savedInstanceState != null) {
            Log.e("Stage", "1");

        } else {

            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setOrientationLocked(false);
            integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
            integrator.initiateScan();
        }
        Intent intent = getIntent();
        intenttype = intent.getStringExtra("type");
        if(intenttype.equalsIgnoreCase("payment"))
        {
            intentResult = intent.getIntExtra("orderID", 0);

        }
        else if(intenttype.equalsIgnoreCase("AssignmentBilling"))
        {
            intentResult = intent.getIntExtra("DeliveryIssuanceId", 0);

        }
        else if(intenttype.equalsIgnoreCase("online"))
        {
            intentResult = intent.getIntExtra("orderID", 0);

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
   /*     Bundle bundle = data.getExtras();
        System.out.println("bundle"+bundle);
        IntentResult result=null;*/
         IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {

            if (result.getContents() == null) {
                Log.d("QRCodeScanActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                finish();
            } else {

                Log.d("QRCodeScanActivity", "Scanned");
                String Scan = result.getContents().replaceFirst("^0+(?!$)", "");
                String Scan2 = result.getContents().replace("AS","00");
                String Scan3=  Scan2.replaceFirst("^0+(?!$)", "");
                if (Scan != null) {
                    if(intenttype.equalsIgnoreCase("payment"))
                    {
                        if (Scan.equalsIgnoreCase(String.valueOf(intentResult))) {
                            Intent intent = new Intent();
                            intent.putExtra("result", Scan);
                            System.out.println("Scan"+Scan);
                            setResult(2, intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Barcode Does not match", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                    else if(intenttype.equalsIgnoreCase("AssignmentBilling"))
                    {

                        if (Scan3.equalsIgnoreCase(String.valueOf(intentResult))) {
                            Intent intent = new Intent();
                            intent.putExtra("result", Scan3);
                            setResult(2, intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Barcode Does not match", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                    else if(intenttype.equalsIgnoreCase("search"))
                    {
                        Intent intent = new Intent();
                        intent.putExtra("result", Scan);
                        setResult(2, intent);
                        finish();
                    }else if(intenttype.equalsIgnoreCase("online"))
                    {
                        if (Scan.equalsIgnoreCase(String.valueOf(intentResult))) {
                            Intent intent = new Intent();
                            intent.putExtra("result", Scan);
                            System.out.println("Scan"+Scan);
                            setResult(3, intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Barcode Does not match", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }



                }


            }
        } else {
            Log.e("Stage", "3");
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
