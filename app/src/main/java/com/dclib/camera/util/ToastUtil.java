package com.dclib.camera.util;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtil {
    private static final Handler handler = new Handler(Looper.getMainLooper());
    private static Toast toast;
    private static boolean enableToast = true;

    /**
     * Whether to allow default pop-up prompts inside TUIKit
     */
    public static void setEnableToast(boolean enableToast) {
        ToastUtil.enableToast = enableToast;
    }

    public static void toastLongMessage(Context context, final String message) {
        toastMessage(context, message, true, Gravity.BOTTOM);
    }

    public static void toastLongMessageCenter(Context context, final String message) {
        toastMessage(context, message, true, Gravity.CENTER);
    }

    public static void toastShortMessage(Context context, final String message) {
        toastMessage(context, message, false, Gravity.BOTTOM);
    }

    public static void toastShortMessageCenter(Context context, final String message) {
        toastMessage(context, message, false, Gravity.CENTER);
    }

    public static void show(Context context, final String message, boolean isLong, int gravity) {
        toastMessage(context, message, isLong, gravity);
    }

    private static void toastMessage(Context context, final String message, boolean isLong, int gravity) {
        if (!enableToast) {
            return;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (toast != null) {
                    toast.cancel();
                    toast = null;
                }
                toast = Toast.makeText(context, message, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
                toast.setGravity(gravity, 0, 0);
                View view = toast.getView();
                if (view != null) {
                    TextView textView = view.findViewById(android.R.id.message);
                    if (textView != null) {
                        textView.setGravity(Gravity.CENTER);
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    toast.addCallback(new Toast.Callback() {
                        @Override
                        public void onToastHidden() {
                            super.onToastHidden();
                            toast = null;
                        }
                    });
                }
                toast.show();
            }
        });
    }
}
