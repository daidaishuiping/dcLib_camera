package com.dclib.library.state;

import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;

import com.dclib.library.util.FileUtil;
import com.dclib.library.view.CameraView;

public class BrowserVideoState implements State {
    private static final String TAG = BrowserVideoState.class.getSimpleName();
    private CameraMachine machine;
    private String dataPath;

    public BrowserVideoState(CameraMachine machine) {
        this.machine = machine;
    }

    @Override
    public void cancel(SurfaceHolder holder, float screenProp) {
        Log.i(TAG, "cancel");
        if (!TextUtils.isEmpty(dataPath)) {
            FileUtil.deleteFile(dataPath);
        }
        dataPath = null;
        machine.getCameraView().stopPlayVideo();
        machine.getCameraView().resetState(CameraView.TYPE_VIDEO);
        machine.setState(machine.getPreviewState());
        machine.startPreview(holder, screenProp);
    }

    @Override
    public void confirm() {
        Log.i(TAG, "confirm");
        machine.getCameraView().confirmState(CameraView.TYPE_VIDEO);
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
