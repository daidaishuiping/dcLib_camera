package com.dclib.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.FragmentActivity;

import com.dclib.camera.listener.CameraListener;
import com.dclib.camera.listener.ClickListener;
import com.dclib.camera.listener.ErrorListener;
import com.dclib.camera.util.FileUtil;
import com.dclib.camera.util.TUIBuild;
import com.dclib.camera.util.ToastUtil;
import com.dclib.camera.view.CameraView;

public class CameraActivity extends FragmentActivity {
    private static final String TAG = CameraActivity.class.getSimpleName();

    public static final int BUTTON_STATE_ONLY_CAPTURE = 0x101;
    public static final int BUTTON_STATE_ONLY_RECORDER = 0x102;
    public static final int BUTTON_STATE_BOTH = 0x103;
    private CameraView cameraView;
    private Context mContext;

    public static final String CAMERA_TYPE = "camera_type";

    private static CameraManager.TaskPhotoListener taskPhotoListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        mContext = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.chat_camera_activity_layout);
        cameraView = findViewById(R.id.camera_view);

        int feature = getIntent().getIntExtra(CAMERA_TYPE, BUTTON_STATE_BOTH);
        cameraView.setFeature(feature);
        if (feature == BUTTON_STATE_ONLY_CAPTURE) {
            cameraView.setTip(getString(R.string.tap_capture));
        } else if (feature == BUTTON_STATE_ONLY_RECORDER) {
            cameraView.setTip(getString(R.string.tap_video));
        }

        cameraView.setMediaQuality(CameraView.MEDIA_QUALITY_MIDDLE);
        cameraView.setErrorListener(new ErrorListener() {
            @Override
            public void onError(String errorMsg) {
                Log.e(TAG, "camera error " + errorMsg);
                Intent intent = new Intent();
                setResult(103, intent);
                if (taskPhotoListener != null) {
                    taskPhotoListener.onError(0, errorMsg);
                }
                finish();
            }
        });

        cameraView.setCameraListener(new CameraListener() {
            @Override
            public void onCaptureSuccess(String path) {
                setResultAndFinish(FileUtil.getUriFromPath(mContext, path), path);
            }

            @Override
            public void onRecordSuccess(String path) {
                setResultAndFinish(FileUtil.getUriFromPath(mContext, path), path);
            }
        });

        cameraView.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                CameraActivity.this.finish();
            }
        });
        cameraView.setRightClickListener(new ClickListener() {
            @Override
            public void onClick() {
                startSelectMedia();
            }
        });
        Log.i(TAG, "device " + TUIBuild.getDevice());
    }

    private void startSelectMedia() {
        Log.i(TAG, "startSelectMedia");
        ToastUtil.toastShortMessage(mContext, "ActivityResultResolver");

//        ActivityResultResolver.getSingleContent(
//                this, new String[]{ActivityResultResolver.CONTENT_TYPE_VIDEO, ActivityResultResolver.CONTENT_TYPE_IMAGE}, new TUIValueCallback<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        setResultAndFinish(uri);
//                    }
//
//                    @Override
//                    public void onError(int errorCode, String errorMessage) {
//                    }
//                });
    }

    private void setResultAndFinish(Uri uri, String filePath) {
        Intent intent = new Intent();
        intent.setData(uri);
        setResult(Activity.RESULT_OK, intent);
        if (taskPhotoListener != null) {
            taskPhotoListener.onSuccess(filePath);
        }
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume");
        cameraView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause");
        cameraView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
        cameraView.onDestroy();
        super.onDestroy();
        taskPhotoListener = null;
    }

    public static void setTaskPhotoListener(CameraManager.TaskPhotoListener taskPhotoListener) {
        CameraActivity.taskPhotoListener = taskPhotoListener;
    }
}
