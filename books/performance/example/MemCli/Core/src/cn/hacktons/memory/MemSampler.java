package cn.hacktons.memory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * Created by aven on 14/04/2018.
 */
public class MemSampler implements Callback<MemBean> {
  private LinkedBlockingQueue<MemBean> mSampleData = new LinkedBlockingQueue<MemBean>(4);
  private ScheduledExecutorService mExecutor = Executors.newScheduledThreadPool(1);
  private ExecutorService mConsumerExecutor = Executors.newSingleThreadExecutor();
  private ArrayList<OnSampledListener<MemBean>> mCallbacks = new ArrayList<>();
  private ScheduledFuture<?> mScheduledFuture;
  private MemCollectTask mTask;
  private int mSampleRate;
  private String mPackage;

  public MemSampler(int seconds, String pkg) {
    mSampleRate = seconds;
    mPackage = pkg;
  }

  public void start() {
    if (mTask == null) {
      mTask = new MemCollectTask(this, mPackage);
      mScheduledFuture = mExecutor.scheduleAtFixedRate(mTask, 0, mSampleRate, TimeUnit.SECONDS);
    }
  }

  public void stop() {
    if (mTask != null) {
      try {
        mScheduledFuture.cancel(true);
        mTask.cancel();
      } catch (Throwable e) {
        e.printStackTrace();
      }
    }
  }

  public void register(OnSampledListener<MemBean> callback) {
    mCallbacks.add(callback);
  }

  @Override public void onResult(MemBean data) {
    try {
      mSampleData.put(data);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    notifyDataChanged();
  }

  private void notifyDataChanged() {
    mConsumerExecutor.execute(mConsumeTask);
  }

  Runnable mConsumeTask = new Runnable() {
    @Override public void run() {
      MemBean memBean = mSampleData.poll();
      for (OnSampledListener<MemBean> callback : mCallbacks) {
        if (callback != null) {
          callback.onSampled(memBean);
        }
      }
    }
  };

  /*
    aven-mac-pro-2:Desktop aven$ adb shell dumpsys meminfo cn.hacktons.leakdemo
  Applications Memory Usage (in Kilobytes):
  Uptime: 88948299 Realtime: 88948299

  ** MEMINFO in pid 12049 [cn.hacktons.leakdemo] **
                     Pss  Private  Private  SwapPss     Heap     Heap     Heap
                   Total    Dirty    Clean    Dirty     Size    Alloc     Free
                  ------   ------   ------   ------   ------   ------   ------
    Native Heap     4102     3840        0        0    13824    12187     1636
    Dalvik Heap      550      516        0        0     9355     4678     4677
   Dalvik Other      392      392        0        0
          Stack      228      228        0        0
         Ashmem        5        0        0        0
      Other dev       10        0        8        0
       .so mmap     1423      196        0        0
      .apk mmap      396        0        0        0
      .ttf mmap       45        0        0        0
      .dex mmap     3709        4     2316        0
      .oat mmap      285        0        0        0
      .art mmap     3914     3668        0        0
     Other mmap       13        4        0        0
        Unknown      355      328        0        0
          TOTAL    15427     9176     2324        0    23179    16865     6313

   App Summary
                         Pss(KB)
                          ------
             Java Heap:     4184
           Native Heap:     3840
                  Code:     2516
                 Stack:      228
              Graphics:        0
         Private Other:      732
                System:     3927

                 TOTAL:    15427       TOTAL SWAP PSS:        0

   Objects
                 Views:       17         ViewRootImpl:        1
           AppContexts:        3           Activities:        1
                Assets:        2        AssetManagers:        3
         Local Binders:        9        Proxy Binders:       16
         Parcel memory:        3         Parcel count:       12
      Death Recipients:        0      OpenSSL Sockets:        0
              WebViews:        0

   SQL
           MEMORY_USED:        0
    PAGECACHE_OVERFLOW:        0          MALLOC_SIZE:        0
     */
  static class MemCollectTask implements Runnable {
    private boolean cancelled;
    private String pkg;
    private Callback<MemBean> callback;

    public MemCollectTask(Callback<MemBean> callback, String pkg) {
      this.callback = callback;
      this.pkg = pkg;
    }

    @Override public void run() {
      if (cancelled) {
        return;
      }
      try {
        Process process = Runtime.getRuntime().exec(ShellCommands.dumpPackage(pkg));
        InputStream inputStream = process.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line;
        boolean findStartLine = false;
        boolean findEndLine = false;
        MemBean bean = new MemBean();
        bean.time = System.currentTimeMillis();
        while ((line = bufferedReader.readLine()) != null && !findEndLine) {
          //Native Heap
          if (line.contains("App Summary")) {
            findStartLine = true;
          }
          if (findStartLine) {
            if (line.contains("Java Heap")) {
              bean.javaHeapSize = splitSizeColumn(line, ":");
            } else if (line.contains("Native Heap")) {
              bean.nativeHeapSize = splitSizeColumn(line, ":");
            } else if (line.contains("TOTAL")) {
              findEndLine = true;
              String[] splits = line.split(":")[1].trim().split(" ");
              int size = 0;
              for (String item : splits) {
                if (item != null && item.length() > 0) {
                  try {
                    size = Integer.parseInt(item);
                    break;
                  } catch (NumberFormatException e) {
                    e.printStackTrace();
                  }
                }
              }
              bean.totalSize = size;
              callback.onResult(bean);
            }
          }
        }
        bufferedReader.close();
        inputStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    int splitSizeColumn(String line, String split) {
      try {
        String[] splits = line.split(split);
        String size = splits[1].trim();
        return Integer.parseInt(size);
      } catch (Throwable e) {
        e.printStackTrace();
      }
      return 0;
    }

    void cancel() {
      cancelled = true;
    }
  }
}
