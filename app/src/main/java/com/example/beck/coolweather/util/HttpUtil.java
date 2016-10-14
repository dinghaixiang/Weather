package com.example.beck.coolweather.util;

import com.example.beck.coolweather.db.CoolWeatherDB;
import com.example.beck.coolweather.model.City;
import com.example.beck.coolweather.model.Country;
import com.example.beck.coolweather.model.Province;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by beck on 2016/10/13.
 */
public class HttpUtil {
    HttpURLConnection httpURLConnection = null;

    public void sendHttpRequest(final String address, final HttpCallbackLister lister) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(address);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(8000);
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }
                    if (lister != null) {
                        lister.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if (lister != null) {
                        lister.onError(e);
                    }
                    e.printStackTrace();
                } finally {
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                }

            }
        }).start();
    }

    public synchronized boolean  handleResponse(CoolWeatherDB db, String response,String flag,String id) {
        if (response != null) {
            String[] all = response.split(",");
            for (int i = 0; i < all.length; i++) {
                String[] array = all[i].split("\\|");
                if(flag.equals("Province")){
                    Province province1 = new Province();
                    province1.setProvinceCode(array[0]);
                    province1.setProvinceName(array[1]);
                    db.saveProvince(province1);
                }else if(flag.equals("City")){
                    City city=new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(id);
                }else if(flag.equals("Country")){
                    Country country=new Country();
                    country.setCountryCode(array[0]);
                    country.setCountryName(array[1]);
                    country.setCityId(id);
                }

            }
            return true;
        }
        return false;
    }

}
