package com.dclib.library.state;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.dclib.library.util.FileUtil;
import com.dclib.library.view.CameraInterface;
import com.dclib.library.view.CameraView;

public class PreviewState implements State {
    public static final String TAG = "PreviewState";

    private CameraMachine machine;

    public PreviewState(CameraMachine machine) {
        this.machine = machine;
    }

    @Override
    public void startPreview(SurfaceHolder holder, float screenProp) {
        Log.i(TAG, "startPreview");
        CameraInterface.getInstance().doStartPreview(holder, screenProp);
    }

    @Override
    public void stop() {
        Log.i(TAG, "stop");
        CameraInterface.getInstance().doStopPreview();
    }

    @Override
    public void focus(float x, float y, CameraInterface.FocusCallback callback) {
        Log.i(TAG, "preview state focus");
        if (machine.getCameraView().handlerFocus(x, y)) {
            CameraInterface.getInstance().handleFocus(machine.getContext(), x, y, callback);
        }
    }

    @Override
    public void switchCamera(SurfaceHolder holder, float screenProp) {
        Log.i(TAG, "switchCamera");
        CameraInterface.getInstance().switchCamera(holder, screenProp);
    }

    @Override
    public void capture() {
        Log.i(TAG, "capture");
        CameraInterface.getInstance().takePicture(new CameraInterface.TakePictureCallback() {
            @Override
            public void captureResult(Bitmap bitmap, boolean isVertical) {
                String path = FileUtil.generateImageFilePath(machine.getContext());
                boolean result = FileUtil.saveBitmap(path, bitmap);
                if (!result) {
                    Log.e(TAG, "take picture save bitmap failed");
                }
                machine.getCameraView().showPicture(bitmap, isVertical);
                machine.getBrowserPictureState().setDataPath(path);
                machine.setState(machine.getBrowserPictureState());
            }
        });
    }

    @Override
    public void startRecord(Surface surface, float screenProp) {
        Log.i(TAG, "startRecord");
        CameraInterface.getInstance().startRecord(machine.getContext(), surface, screenProp);
    }

    @Override
    public void stopRecord(final boolean isShort, long time) {
        Log.i(TAG, "stopRecord");
        CameraInterface.getInstance().stopRecord(isShort, new CameraInterface.StopRecordCallback() {
            @Override
            public void recordResult(String path) {
                if (isShort) {
                    FileUtil.deleteFile(path);
                    machine.getCameraView().resetState(CameraView.TYPE_SHORT);
                } else {
                    machine.getBrowserVideoState().setDataPath(path);
                    machine.getCameraView().playVideo(path);
                    machine.setState(machine.getBrowserVideoState());
                }
            }

            @Override
            public void recordFailed(String path) {
                FileUtil.deleteFile(path);
                machine.getCameraView().resetState(CameraView.TYPE_VIDEO);
            }
        });
    }

    @Override
    public void zoom(float zoom, int type) {
        Log.i(TAG, "zoom");
        CameraInterface.getInstance().setZoom(zoom, type);
    }

    @Override
    public void setFlashMode(String mode) {
        Log.i(TAG, "setFlashMode");
        CameraInterface.getInstance().setFlashMode(mode);
    }
}
