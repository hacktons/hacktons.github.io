package cn.hacktons.memory;

import com.sun.istack.internal.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.zip.Deflater;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipOutputStream;

public class Main {

  public static void main(String[] args) {
    // write your code here

    String db = "sample.db";
    String pkg = "cn.hacktons.leakdemo";
    int sampleRate = 5;

    final Consumer consumer = new Consumer();
    final Executor executor = Executors.newSingleThreadExecutor();
    consumer.create(db);
    MemSampler sampler = new MemSampler(sampleRate, pkg);
    sampler.register(new OnSampledListener<MemBean>() {

      @Override public void onSampled(final MemBean data) {
        System.out.println("memory info: " + data.json());
        executor.execute(new Runnable() {
          @Override public void run() {
            consumer.insert(data);
          }
        });
      }
    });
    sampler.start();
    //nio("/Users/aven/Desktop/test/58client_v8.0.6_58585858_20180109_10.54_release.apk", "/Users/aven/Desktop/java-nio.apk");
    //normal("/Users/aven/Desktop/test/58client_v8.0.6_58585858_20180109_10.54_release.apk", "/Users/aven/Desktop/java-normal.apk");

  }

  public static void normal(String src, String dst) {
    File compressedFile = new File(dst);
    File srcFile = new File(src);
    try {
      GZIPOutputStream zos = new MaxGzipOutputStream(new FileOutputStream(compressedFile));
      FileInputStream inputStream = new FileInputStream(srcFile);
      byte[] buf = new byte[2048];
      int read = 0;
      while ((read = inputStream.read(buf)) > 0) {
        zos.write(buf, 0, read);
      }
      inputStream.close();
      zos.flush();
      zos.close();
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  public static void nio(String src, String dst) {
    getApkServedByPlay(new File(src), dst + ".gz");
  }

  /**
   * @return the input file compressed using "gzip -9" and saved in a temporary location
   */
  private static File getApkServedByPlay(@NotNull File apk, String dst) {
    File compressedFile;
    try {
      compressedFile = new File(dst);
    } catch (Throwable e) {
      //Logger.getInstance(ApkParser.class).warn(e);
      return apk;
    }

    // There is a difference between uncompressing the apk, and then compressing again using gzip -9, versus just compressing the apk
    // itself using gzip -9. But the difference seems to be negligible, and we are only aiming at an estimate of what Play provides, so
    // this should suffice. This also seems to be the same approach taken by https://github.com/googlesamples/apk-patch-size-estimator

    try (GZIPOutputStream zos = new MaxGzipOutputStream(new FileOutputStream(compressedFile))) {
      Files.copy(apk.toPath(), zos);
      zos.flush();
    } catch (IOException e) {
      //Logger.getInstance(ApkParser.class).warn(e);
      return apk;
    }

    return compressedFile;
  }

  private static final class MaxGzipOutputStream extends GZIPOutputStream {
    public MaxGzipOutputStream(OutputStream out) throws IOException {
      super(out);
      def.setLevel(Deflater.BEST_COMPRESSION); // Currently, Google Play serves an APK that is compressed using gzip -9
    }
  }

  private static final class MaxZipOutputStream extends ZipOutputStream {
    public MaxZipOutputStream(OutputStream out) throws IOException {
      super(out);
      def.setLevel(Deflater.BEST_COMPRESSION);
    }
  }
}