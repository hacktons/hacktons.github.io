package cn.hacktons.memory;

public interface Callback<T> {
  void onResult(T data);
}