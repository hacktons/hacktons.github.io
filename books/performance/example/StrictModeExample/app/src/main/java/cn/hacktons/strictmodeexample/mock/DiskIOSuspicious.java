package cn.hacktons.strictmodeexample.mock;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import static cn.hacktons.strictmodeexample.SimpleUtils.read2Log;
import static cn.hacktons.strictmodeexample.SimpleUtils.write2Stream;

/**
 * Created by aven on 25/03/2018.
 */

public class DiskIOSuspicious implements ISuspicious {
    private WeakReference<Context> reference;

    public DiskIOSuspicious(Context context) {
        this.reference = new WeakReference<Context>(context.getApplicationContext());
    }

    @Override
    public void onDoingSomething() {
        Context context = reference.get();
        if (context == null) {
            return;
        }
        File file = new File(context.getCacheDir(), "hello-world");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            write2Stream(outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileInputStream inputStream = new FileInputStream(file);
            read2Log(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
