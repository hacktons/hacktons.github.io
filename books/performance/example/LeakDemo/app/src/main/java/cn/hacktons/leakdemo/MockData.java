package cn.hacktons.leakdemo;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by aven on 13/04/2018.
 */

public class MockData {
    static ArrayList<byte[]> dataList = new ArrayList<>();

    int fetch() {
        Log.i("MockData", "fetch new data");
        int size = 4 * 1024 * 1024;
        byte[] bytes = new byte[size];
        dataList.add(bytes);
        return size;
    }
}
