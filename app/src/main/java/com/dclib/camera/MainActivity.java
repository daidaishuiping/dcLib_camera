package com.dclib.camera;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dclib.camera.util.ToastUtil;
import com.dclib.camera.view.CameraManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ToastUtil.toastShortMessage(MainActivity.this, "测试");
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                startActivity(intent);

                CameraManager.taskPhoto(MainActivity.this, new CameraManager.TaskPhotoListener() {
                    @Override
                    public void onSuccess(String filePath) {
                        ToastUtil.toastShortMessage(MainActivity.this, filePath);
                    }

                    @Override
                    public void onError(int code, String error) {
                        Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}