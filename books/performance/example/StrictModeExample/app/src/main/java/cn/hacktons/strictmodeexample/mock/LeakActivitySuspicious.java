package cn.hacktons.strictmodeexample.mock;

import android.content.Context;
import android.content.Intent;

import java.lang.ref.WeakReference;

import cn.hacktons.strictmodeexample.LeakActivity;

/**
 * Created by aven on 26/03/2018.
 */

public class LeakActivitySuspicious implements ISuspicious {
    WeakReference<Context> mReference;

    public LeakActivitySuspicious(Context context) {
        this.mReference = new WeakReference<Context>(context);
    }

    @Override
    public void onDoingSomething() {
        Context context = mReference.get();
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, LeakActivity.class));
        context.startActivity(new Intent(context, LeakActivity.class));
    }
}
