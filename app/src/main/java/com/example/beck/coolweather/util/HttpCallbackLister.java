package com.example.beck.coolweather.util;

/**
 * Created by beck on 2016/10/13.
 */
public interface HttpCallbackLister {
    void onFinish(String response);

    void onError(Exception e);

}
