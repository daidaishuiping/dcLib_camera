package com.dclib.camera.state;

import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;

import com.dclib.camera.util.FileUtil;
import com.dclib.camera.view.CameraView;

public class BrowserPictureState implements State {
    private static final String TAG = BrowserPictureState.class.getSimpleName();
    private CameraMachine machine;
    private String dataPath;

    public BrowserPictureState(CameraMachine machine) {
        this.machine = machine;
    }

    @Override
    public void cancel(SurfaceHolder holder, float screenProp) {
        Log.i(TAG, "cancel");
        if (!TextUtils.isEmpty(dataPath)) {
            FileUtil.deleteFile(dataPath);
        }
        dataPath = null;
        machine.getCameraView().resetState(CameraView.TYPE_PICTURE);
        machine.setState(machine.getPreviewState());
        machine.startPreview(holder, screenProp);
    }

    @Override
    public void confirm() {
        Log.i(TAG, "confirm");
        machine.getCameraView().confirmState(CameraView.TYPE_PICTURE);
        machine.setState(machine.getPreviewState());
    }

    @Override
    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    @Override
    public String getDataPath() {
        return dataPath;
    }
}
