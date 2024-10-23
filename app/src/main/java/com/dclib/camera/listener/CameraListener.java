package com.dclib.camera.listener;

public interface CameraListener {
    void onCaptureSuccess(String path);

    void onRecordSuccess(String path);
}
