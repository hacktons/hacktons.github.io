package cn.hacktons.strictmodeexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LeakActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leak);
        SimpleUtils.CacheManager.getInstance(this).add("hello", "Hello World");
    }
}
