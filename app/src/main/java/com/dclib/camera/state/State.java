package com.dclib.camera.state;

import android.view.Surface;
import android.view.SurfaceHolder;

import com.dclib.camera.view.CameraInterface;

public interface State {
    default void startPreview(SurfaceHolder holder, float screenProp) {}

    default void stop() {}

    default void focus(float x, float y, CameraInterface.FocusCallback callback) {}

    default void switchCamera(SurfaceHolder holder, float screenProp) {}

    default void restart() {}

    default void capture() {}

    default void startRecord(Surface surface, float screenProp) {}

    default void stopRecord(boolean isShort, long time) {}

    default void cancel(SurfaceHolder holder, float screenProp) {}

    default void confirm() {}

    default void zoom(float zoom, int type) {}

    default void setFlashMode(String mode) {}

    default void setDataPath(String dataPath) {}
    
    default String getDataPath() {
        return null;
    }
}
