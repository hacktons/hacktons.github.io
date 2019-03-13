package cn.hacktons.strictmodeexample;

import android.app.Application;
import android.os.Debug;
import android.os.Handler;
import android.os.StrictMode;

/**
 * Created by aven on 22/03/2018.
 */

public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEVELOP_MODE) {
//            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                    .detectDiskReads()
//                    .detectDiskWrites()
//                    .detectNetwork()
//                    .detectCustomSlowCalls()
//                    // API 23
//                    .detectResourceMismatches()
//                    // API 26
//                    //.detectUnbufferedIo()
//                    .penaltyLog()
//                    .build());
//            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                    .detectActivityLeaks()
//                    .detectLeakedClosableObjects()
//                    .detectLeakedRegistrationObjects()
//                    .detectLeakedSqlLiteObjects()
//                    // API 18
//                    .detectFileUriExposure()
//                    // API 23
//                    .detectCleartextNetwork()
//                    // API 26
//                    //.detectUntaggedSockets()
//                    //.detectContentUriWithoutPermission()
//                    .penaltyLog()
//                    .build());
            new Handler().postAtFrontOfQueue(new Runnable() {
                @Override
                public void run() {
                    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
                    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
                }
            });
        }
    }
}
