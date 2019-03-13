package cn.hacktons.memory;

/**
 * Created by aven on 14/04/2018.
 */
public class MemBean {
  public int javaHeapSize;
  public int nativeHeapSize;
  public int totalSize;
  public long time;

  String json() {
    return "{\"javaHeap\":" + javaHeapSize + ",\"nativeHeap\":" + nativeHeapSize + ",\"totalSize\":" + totalSize + ", \"time\":" + time + "}";
  }
}
