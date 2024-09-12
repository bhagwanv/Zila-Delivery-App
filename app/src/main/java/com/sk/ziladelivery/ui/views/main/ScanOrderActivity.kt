package com.sk.ziladelivery.ui.views.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.sk.ziladelivery.utilities.TextUtils
import com.sk.ziladelivery.utilities.Utils
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import com.sk.ziladelivery.R
import com.sk.ziladelivery.databinding.BarcodeEnterPopupBinding
import com.sk.ziladelivery.ui.views.viewmodels.CameraXViewModel

class ScanOrderActivity : AppCompatActivity() {
    private var previewView: PreviewView? = null
    private var view: View? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var cameraSelector: CameraSelector? = null
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private var previewUseCase: Preview? = null
    private var analysisUseCase: ImageAnalysis? = null
    private var result: String? = null
    private var intenttype: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_code_two)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val tvEnterCode = findViewById<TextView>(R.id.tvEnterCode)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (intent.extras != null) {
            intenttype = intent.getStringExtra("type")
        }

        view = findViewById(R.id.view_scan)
        setupCamera()

        tvEnterCode.setOnClickListener {
            enterBarCode()
        }
    }

    override fun onBackPressed() {
        val intent = Intent()
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_CAMERA_REQUEST) {
            if (isCameraPermissionGranted()) {
                bindCameraUseCases()
            } else {
                Log.e(TAG, "no camera permission")
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    private fun enterBarCode() {
        val barcodeBottomDialog = BottomSheetDialog(this, R.style.DialogStyle)
        val mBarcodeEnterPopupBinding: BarcodeEnterPopupBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.barcode_enter_popup, null, false)
        barcodeBottomDialog.setContentView(mBarcodeEnterPopupBinding.root)
        barcodeBottomDialog.window?.findViewById<View>(R.id.design_bottom_sheet)
            ?.setBackgroundColor(Color.TRANSPARENT)
        barcodeBottomDialog.setCancelable(true)

        mBarcodeEnterPopupBinding.tvSubmit.setOnClickListener {
            val etBarCode = mBarcodeEnterPopupBinding.etBarCode.text.toString().trim()
            if (TextUtils.isNullOrEmpty(etBarCode)) {
                Utils.setToast(this,"Please enter bar code")
            } else {
                barcodeBottomDialog.dismiss()
                val intent = Intent()
                intent.putExtra("SCANNED_CODE", etBarCode)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
        barcodeBottomDialog.show()
    }

    private fun setupCamera() {
        previewView = findViewById(R.id.preview_view)
        cameraSelector = CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()
        ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(CameraXViewModel::class.java)
            .processCameraProvider
            .observe(this) { provider: ProcessCameraProvider? ->
                cameraProvider = provider
                if (isCameraPermissionGranted()) {
                    bindCameraUseCases()
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.CAMERA),
                        PERMISSION_CAMERA_REQUEST
                    )
                }
            }
    }

    private fun bindCameraUseCases() {
        bindPreviewUseCase()
        bindAnalyseUseCase()
    }

    private fun screenAspectRatio(): Int {
        // Get screen metrics used to setup camera for full screen resolution
        val metrics = DisplayMetrics().also { view?.display?.getRealMetrics(it) }
        return aspectRatio(metrics.widthPixels, metrics.heightPixels)
    }

    private fun bindPreviewUseCase() {
        if (cameraProvider == null) {
            return
        }
        if (previewUseCase != null) {
            cameraProvider!!.unbind(previewUseCase)
        }

        previewUseCase = Preview.Builder()
            .setTargetAspectRatio(screenAspectRatio())
            .setTargetRotation(previewView?.display?.rotation ?: 0)
            .build()
        previewUseCase!!.setSurfaceProvider(previewView!!.surfaceProvider)

        try {
            cameraProvider!!.bindToLifecycle(
                this,
                cameraSelector!!,
                previewUseCase
            )
        } catch (illegalStateException: IllegalStateException) {
            Log.e(TAG, illegalStateException.message ?: "IllegalStateException")
        } catch (illegalArgumentException: IllegalArgumentException) {
            Log.e(TAG, illegalArgumentException.message ?: "IllegalArgumentException")
        }
    }

    private fun bindAnalyseUseCase() {
        val barcodeScanner: BarcodeScanner = BarcodeScanning.getClient()

        if (cameraProvider == null) {
            return
        }
        if (analysisUseCase != null) {
            cameraProvider!!.unbind(analysisUseCase)
        }

        analysisUseCase = ImageAnalysis.Builder()
            .setTargetAspectRatio(screenAspectRatio())
            .setTargetRotation(previewView?.display?.rotation ?: 0)
            .build()

        // Initialize our background executor 
        val cameraExecutor = Executors.newSingleThreadExecutor()

        analysisUseCase?.setAnalyzer(
            cameraExecutor
        ) { imageProxy ->
            processImageProxy(barcodeScanner, imageProxy)
        }

        try {
            cameraProvider!!.bindToLifecycle(
                this,
                cameraSelector!!,
                analysisUseCase
            )
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    private fun processImageProxy(
        barcodeScanner: BarcodeScanner,
        imageProxy: ImageProxy
    ) {
        val inputImage =
            InputImage.fromMediaImage(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)
        barcodeScanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                barcodes.forEach {
                    result = it.displayValue
                    if (result != null) {
                        val intent = Intent()
                        intent.putExtra("SCANNED_CODE", result)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    } else {
                        Log.d("QRCodeScanActivity", "Cancelled scan")
                        Toast.makeText(applicationContext, "Cancelled", Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
            }
            .addOnFailureListener {
                Log.e(TAG, "bhagwan " + it.message)
            }.addOnCompleteListener {
                // When the image is from CameraX analysis use case, must call image.close() on received
                // images when finished using them. Otherwise, new images may not be received or the camera
                // may stall.
                imageProxy.close()
            }
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            baseContext,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val TAG = ScanOrderActivity::class.java.simpleName
        private const val PERMISSION_CAMERA_REQUEST = 1

        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
    }
}