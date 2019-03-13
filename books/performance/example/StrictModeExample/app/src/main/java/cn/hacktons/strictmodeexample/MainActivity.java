package cn.hacktons.strictmodeexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.hacktons.strictmodeexample.mock.DiskIOSuspicious;
import cn.hacktons.strictmodeexample.mock.LeakActivitySuspicious;
import cn.hacktons.strictmodeexample.mock.NetworkSuspicious;
import cn.hacktons.strictmodeexample.mock.SlowSuspicious;
import cn.hacktons.strictmodeexample.mock.SuspiciousMock;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new SuspiciousMock()
                //.add(DiskIOSuspicious.class, this)
                //.add(NetworkSuspicious.class)
                .add(SlowSuspicious.class)
                .add(LeakActivitySuspicious.class, this)
                .catchAll(true)
                .execute();
    }
}