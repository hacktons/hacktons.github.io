package cn.hacktons.memory;

/**
 * Created by aven on 14/04/2018.
 */
public class ShellCommands {
  static final String DUMP_MEMORY = "adb shell dumpsys meminfo %s";

  public static String dumpPackage(String packageName) {
    return String.format(DUMP_MEMORY, packageName);
  }

}
