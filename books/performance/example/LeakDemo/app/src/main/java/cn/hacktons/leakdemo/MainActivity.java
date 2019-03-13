package cn.hacktons.leakdemo;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MockData data = new MockData();
        int size = data.fetch();
        ((TextView) findViewById(R.id.label)).setText("data size:" + size);
        camera = Camera.open();
        ((Switch) findViewById(R.id.switch1)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        Camera.Parameters mParameters;
                        mParameters = camera.getParameters();
                        mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        camera.setParameters(mParameters);
                    } catch (Exception ex) {
                    }
                } else {
                    try {
                        Camera.Parameters mParameters;
                        mParameters = camera.getParameters();
                        mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        camera.setParameters(mParameters);
                    } catch (Exception ex) {
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        camera.startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        camera.stopPreview();
    }
}