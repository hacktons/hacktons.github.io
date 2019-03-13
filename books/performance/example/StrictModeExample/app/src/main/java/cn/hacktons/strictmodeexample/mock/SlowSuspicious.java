package cn.hacktons.strictmodeexample.mock;

import android.graphics.Bitmap;
import android.os.StrictMode;
import android.util.Log;

/**
 * Created by aven on 25/03/2018.
 */

public class SlowSuspicious implements ISuspicious {
    private static final int THRESHOLD = 16; // ms
    private static final int WIDTH = 1080;
    private static final int HEIGHT = 1920;

    @Override
    public void onDoingSomething() {
        long start = System.currentTimeMillis();
        Bitmap bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                bitmap.setPixel(x, y, randomPixel(x, y));
            }
        }
        long end = System.currentTimeMillis();
        long diff = end - start;
        Log.i("Detect", "time diff=" + diff);
        if (diff > THRESHOLD) {
            Log.i("Detect", "notify slow method" );
            StrictMode.noteSlowCall("SlowSuspicious");
        }
    }

    private int randomPixel(int x, int y) {
        return (int) (Math.random() * 0xff);
    }
}
