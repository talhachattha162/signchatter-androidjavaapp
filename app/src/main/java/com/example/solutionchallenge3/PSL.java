package com.example.solutionchallenge3;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.os.Bundle;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PSL#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PSL extends Fragment  implements SurfaceHolder.Callback{
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CAMERA_PERMISSION = 100;

    private Camera mCamera;
    private SurfaceView mPreview;
    private MediaRecorder mMediaRecorder;
    private boolean mIsRecording = false;
    private CountDownTimer mCountDownTimer;
    private Button mButtonRecord;
    private File mOutputFile;
    long seconds;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PSL() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PSL.
     */
    // TODO: Rename and change types and number of parameters
    public static PSL newInstance(String param1, String param2) {
        PSL fragment = new PSL();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parent=inflater.inflate(R.layout.fragment_p_s_l, container, false);
        mPreview = parent.findViewById(R.id.preview);
        mPreview.getHolder().addCallback(this);
        mButtonRecord = parent.findViewById(R.id.record_button);
        mButtonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsRecording) {
                    stopRecording();
                } else {
                    startRecording();
                }
            }
        });
        return parent;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        try {
            mCamera = Camera.open();
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.setDisplayOrientation(90);
        } catch (IOException e) {
            Log.e(TAG, "Error setting camera preview", e);
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if (mCamera != null) {
            try {
                mCamera.stopPreview();
            } catch (Exception e) {
                Log.e(TAG, "Error stopping camera preview", e);
            }

            try {
                mCamera.setPreviewDisplay(surfaceHolder);
                mCamera.startPreview();
            } catch (Exception e) {
                Log.e(TAG, "Error starting camera preview", e);
            }
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted, start camera preview
                mPreview.getHolder().addCallback(this);
            } else {
                // Camera permission not granted, show message and exit activity
//                finish();
                Toast.makeText(getContext(), "Permission not granted",Toast.LENGTH_SHORT);
            }
        }
    }
    private void startRecording() {
        if (mCamera == null) {
            return;
        }

        // Configure media recorder
        mMediaRecorder = new MediaRecorder();
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));
        mMediaRecorder.setOutputFile(getOutputFile());
        mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());

        // Start recording
        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            mIsRecording = true;
            startTimer();
        } catch (IOException e) {
            Log.e(TAG, "Error starting video recording", e);
        }
    }

    private void stopRecording() {
        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            mIsRecording = false;
            mButtonRecord.setText("Record");
            stopTimer();
            Toast.makeText(getContext(), getOutputFile().toString(), Toast.LENGTH_LONG).show();
//            finish();
            // Preview the recorded video
//            Intent intent = new Intent(this, VideoPreviewActivity.class);
//            intent.putExtra("video_path", getOutputFile().toString());
//            startActivity(intent);
        }
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(6000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                seconds = millisUntilFinished / 1000;
                mButtonRecord.setText(Long.toString(seconds));
                Log.d(TAG, "Seconds remaining: " + seconds);

            }

            @Override
            public void onFinish() {
                stopRecording();

            }
        }.start();
    }

    private void stopTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }

    private String getOutputFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "VID_" + timeStamp + ".mp4";
        mOutputFile = new File(getActivity().getExternalFilesDir(null), fileName);
        return mOutputFile.getAbsolutePath();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }
}