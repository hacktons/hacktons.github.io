package cn.hacktons.strictmodeexample.mock;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import cn.hacktons.strictmodeexample.SimpleUtils;

/**
 * Created by aven on 25/03/2018.
 */

public class NetworkSuspicious implements ISuspicious {
    @Override
    public void onDoingSomething() {
        try {
            URL url = new URL("https://www.baidu.com");
            URLConnection connection = url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            SimpleUtils.read2Log(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}