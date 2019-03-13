package cn.hacktons.strictmodeexample.mock;

import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aven on 25/03/2018.
 */
public class SuspiciousMock {
    private List<ISuspicious> suspiciousList = new ArrayList<>();
    private boolean catchAll = false;

    public SuspiciousMock add(Class<? extends ISuspicious> clz, Object... params) {
        if (clz == null) {
            return this;
        }
        if (params == null || params.length == 0) {
            try {
                ISuspicious instance = clz.newInstance();
                add(instance);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Constructor<?>[] constructors = clz.getDeclaredConstructors();
                if (constructors.length > 1) {
                    throw new RuntimeException("Invalid ISuspicious implementation, should has only one constructor");
                }
                ISuspicious instance = (ISuspicious) constructors[0].newInstance(params);
                add(instance);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    private SuspiciousMock add(ISuspicious suspicious) {
        if (suspicious == null) {
            return this;
        }
        suspiciousList.add(suspicious);
        return this;
    }

    public SuspiciousMock catchAll(boolean catchAll) {
        this.catchAll = catchAll;
        return this;
    }

    public void execute() {
        for (ISuspicious su : suspiciousList) {
            if (su == null) {
                continue;
            }
            if (catchAll) {
                try {
                    Log.i("Detect", "execute " + su.getClass().getSimpleName());
                    su.onDoingSomething();
                } catch (Throwable throwable) {
                    Log.e("Detect", "StrictMode found suspicious", throwable);
                }
            } else {
                su.onDoingSomething();
            }
        }
    }
}
