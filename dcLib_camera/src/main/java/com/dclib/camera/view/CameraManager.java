package com.dclib.camera.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import com.dclib.camera.CameraActivity;

/**
 * 自定义相机管理者
 *
 * @author daichao
 */
public class CameraManager {

    public static void taskPhoto(Context context, TaskPhotoListener listener) {
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

    public interface TaskPhotoListener {
        void onSuccess(String filePath);

        void onError(int code, String error);
    }
}
