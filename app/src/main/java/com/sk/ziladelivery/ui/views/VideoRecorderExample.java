package com.sk.ziladelivery.ui.views;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.sk.ziladelivery.R;

public class VideoRecorderExample extends AppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener {

    private static final String TAG = VideoRecorderExample.class.getSimpleName();

    private RelativeLayout rlRecord, rlVideo;
    private VideoView videoView;
    private ImageView btnStart;
    private TextView tvTime = null;

    private MediaRecorder mVideoRecorder = null;
    private Camera mCamera;
    private CountDownTimer timer;

    private Boolean mRecording = false;
    private String videoFilePath;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.videorecord);

        VideoView mVideoView = findViewById(R.id.videoRecorder);
        rlRecord = findViewById(R.id.rlRecord);
        rlVideo = findViewById(R.id.rlVideo);
        videoView = findViewById(R.id.videoView);
        tvTime = findViewById(R.id.tvTime);
        btnStart = findViewById(R.id.btnStart);
        Button btnOk = findViewById(R.id.btnOk);
        Button btnRetry = findViewById(R.id.btnRetry);

        final SurfaceHolder holder = mVideoView.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        btnStart.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        btnRetry.setOnClickListener(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        timer = new CountDownTimer(31000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvTime.setText("00:" + millisUntilFinished / 1000);

            }

            public void onFinish() {
                btnStart.callOnClick();
            }
        };
        requestPermissions();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStart:
                if (mRecording) {
                    btnStart.setImageResource(R.drawable.ic_record_circle);
                    try {
                        stopRecording();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    timer.cancel();
                    rlRecord.setVisibility(View.GONE);
                    rlVideo.setVisibility(View.VISIBLE);
                    playVideo();
                    tvTime.setText("00:00");
                } else {
                    btnStart.setImageResource(R.drawable.ic_record_stop_circle);
                    mVideoRecorder.start();
                    mRecording = true;
                    timer.start();
                }
                break;
            case R.id.btnRetry:
                recreate();
                break;
            case R.id.btnOk:
                Intent intent = new Intent();
                intent.putExtra("path", videoFilePath);
                intent.setData(Uri.parse(videoFilePath));
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            default:
                break;
        }
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        try {
            startRecording(holder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        Log.e(TAG, "W x H = " + width + "x" + height);
    }

    @Override
    protected void onDestroy() {
        try {
            stopRecording();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
        super.onDestroy();

    }


    private void requestPermissions() {
        String[] permissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO};
        } else {
            permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        }
        Permissions.check(this, permissions, null, null, new PermissionHandler() {
            @Override
            public void onGranted() {

            }
        });
    }

    private void startRecording(SurfaceHolder holder) throws Exception {
        if (mVideoRecorder != null) {
            mVideoRecorder.stop();
            mVideoRecorder.release();
            mVideoRecorder = null;
        }
        if (mCamera != null) {
            mCamera.reconnect();
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }

      //  videoFilePath = Environment.getExternalStorageDirectory().toString() + "/" + System.currentTimeMillis() + ".mp4";
        videoFilePath = getVideoFilePath();

        try {
            mCamera = Camera.open();
            mCamera.setPreviewDisplay(holder);
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(320, 240);
            mCamera.setDisplayOrientation(90);
            mCamera.setParameters(parameters);
            mCamera.startPreview();
            mCamera.unlock();

            mVideoRecorder = new MediaRecorder();
            mVideoRecorder.setCamera(mCamera);
            mVideoRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mVideoRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mVideoRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mVideoRecorder.setVideoSize(320, 240);
            mVideoRecorder.setVideoFrameRate(30);
            mVideoRecorder.setVideoEncodingBitRate(500000);
            mVideoRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mVideoRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mVideoRecorder.setAudioEncodingBitRate(8);
            mVideoRecorder.setAudioSamplingRate(8);
            mVideoRecorder.setMaxDuration(31000);
            mVideoRecorder.setOrientationHint(90);
            mVideoRecorder.setPreviewDisplay(holder.getSurface());
            mVideoRecorder.setOutputFile(videoFilePath);
            mVideoRecorder.prepare();
//            mVideoRecorder.start();
//            mRecording = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() throws Exception {
        mRecording = false;
        if (mVideoRecorder != null) {
            mVideoRecorder.stop();
            mVideoRecorder.release();
            mVideoRecorder = null;
        }
        if (mCamera != null) {
            mCamera.reconnect();
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    private String getVideoFilePath() {
        String fileName = System.currentTimeMillis() + ".mp4";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // For Android 10 (API level 29) and above
            return getExternalFilesDir(null) + "/" + fileName;
        } else {
            // For Android 9 (API level 28) and below
            return Environment.getExternalStorageDirectory() + "/" + fileName;
        }
    }

    private void playVideo() {
        System.out.println("vidpath " + videoFilePath);
//        videoView.setVideoURI(Uri.parse(videoFilePath));
        videoView.setVideoPath(videoFilePath);
        videoView.setMediaController(new MediaController(this));
        videoView.requestFocus();
        videoView.start();
    }
}
