package com.dclib.camera;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

/**
 * 自定义相机管理者
 *
 * @author daichao
 */
public class CameraManager {

    /**
     * 开始拍照
     */
    public static void startTakePhoto(Context context, TaskPhotoListener listener) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // 相机权限未授予
            listener.onError(-1, "请打开相机权限");
            return;
        }

        Intent intent = new Intent(context, CameraActivity.class);
        CameraActivity.setTaskPhotoListener(listener);
        context.startActivity(intent);
    }

    /**
     * 开始录像
     */
    public static void startVideoRecording(Context context, TaskPhotoListener listener) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // 相机权限未授予
            listener.onError(-1, "请打开相机权限");
            return;
        }

//        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
//                != PackageManager.PERMISSION_GRANTED) {
//            // 相机权限未授予
//            listener.onError(-1, "请打开录音权限");
//            return;
//        }

        Intent intent = new Intent(context, CameraActivity.class);
        intent.putExtra(CameraActivity.CAMERA_TYPE, CameraActivity.BUTTON_STATE_ONLY_RECORDER);
        CameraActivity.setTaskPhotoListener(listener);
        context.startActivity(intent);
    }

    public interface TaskPhotoListener {
        void onSuccess(String filePath);

        void onError(int code, String error);
    }
}
