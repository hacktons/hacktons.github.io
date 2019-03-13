package cn.hacktons.strictmodeexample;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by aven on 25/03/2018.
 */

public class SimpleUtils {

    public static void read2Log(InputStream inputStream) throws IOException {
        Log.i("Detect", "start read bytes");
        byte[] buffer = new byte[2048];
        int readLength = -1;
        while ((readLength = inputStream.read(buffer)) > 0) {
            Log.i("Detect", "read bytes:" + readLength);
        }
        Log.i("Detect", "finish read bytes");
    }

    public static void write2Stream(OutputStream outputStream) throws IOException {
        Log.i("Detect", "start write bytes");
        byte[] MOCK_DATA = ("StrictMode Introduce\n" +
                "StrictMode is a developer tool which detects things you might be doing by accident and brings them to your attention so you can fix them.\n" +
                "\n" +
                "StrictMode is most commonly used to catch accidental disk or network access on the application's main thread, where UI operations are received and animations take place. Keeping disk and network operations off the main thread makes for much smoother, more responsive applications. By keeping your application's main thread responsive, you also prevent ANR dialogs from being shown to users.\n" +
                "\n" +
                "Note that even though an Android device's disk is often on flash memory, many devices run a filesystem on top of that memory with very limited concurrency. It's often the case that almost all disk accesses are fast, but may in individual cases be dramatically slower when certain I/O is happening in the background from other processes. If possible, it's best to assume that such things are not fast.\n" +
                "\n" +
                "Example code to enable from early in your Application, Activity, or other application component's onCreate() method:").getBytes();
        outputStream.write(MOCK_DATA);
        outputStream.flush();
        Log.i("Detect", "finish write bytes");
    }

    public static class CacheManager{
        private Context context;
        private volatile static CacheManager sInstance;

        public static CacheManager getInstance(Context context) {
            if (sInstance == null) {
                synchronized (CacheManager.class) {
                    if (sInstance == null) {
                        sInstance = new CacheManager(context);
                    }
                }
            }
            return sInstance;
        }

        private CacheManager(Context context) {
            this.context = context;
        }

        public <T> void add(String key, T data){
            // TODO
        }

        public  void get(String key){
            // TODO
        }
    }
}
