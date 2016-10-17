package com.example.beck.coolweather.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beck.coolweather.R;
import com.example.beck.coolweather.db.CoolWeatherDB;
import com.example.beck.coolweather.model.City;
import com.example.beck.coolweather.model.Country;
import com.example.beck.coolweather.model.Province;
import com.example.beck.coolweather.util.HttpCallbackLister;
import com.example.beck.coolweather.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beck on 2016/10/14.
 */
public class ChooseAreaActivity extends Activity {
    private static String PROVINCE_LEVEL = "province";
    private static String CITY_LEVEL = "city";
    private static String COUNTRY_LEVEL = "country";
    private TextView title;
    private ListView listView;
    List<Province> provinceList = new ArrayList<>();
    List<City> cityList = new ArrayList<>();
    List<Country> countryList = new ArrayList<>();
    private Province selectedProvince;
    private City selectedCity;
    private Country selectedCountry;
    private List dataList = new ArrayList();
    CoolWeatherDB coolWeatherDB = CoolWeatherDB.getInstance(this);
    ArrayAdapter adapter;
    private String currentLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_area);
        title = (TextView) findViewById(R.id.title_text);
        listView = (ListView) findViewById(R.id.listView);
        title.setText("中国");
        queryProvince();
        adapter = new ArrayAdapter<String>(ChooseAreaActivity.this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel.equals(PROVINCE_LEVEL)) {
                    selectedProvince = provinceList.get(position);
                    queryCity(selectedProvince.getProvinceCode());
                } else if (currentLevel.equals(CITY_LEVEL)) {
                    selectedCity = cityList.get(position);
                    queryCountry(selectedCity.getCityCode());
                }
            }
        });
    }

    private void queryProvince() {
        provinceList = coolWeatherDB.loadProvinces();
        if (provinceList.size() > 0) {
            provinceList.clear();
            for (Province p : provinceList) {
                dataList.add(p.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
        } else {
            getAreaFromServer(null, "Province");
        }
        currentLevel = PROVINCE_LEVEL;
    }

    private void queryCity(String provinceId) {
        cityList = coolWeatherDB.loadCities(provinceId);
        if (cityList.size() > 0) {
            cityList.clear();
            for (City c : cityList) {
                dataList.add(c.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
        } else {
            getAreaFromServer(provinceId, "City");
        }
        currentLevel = CITY_LEVEL;
    }

    private void queryCountry(String cityId) {
        countryList = coolWeatherDB.loadCountry(cityId);
        if (countryList.size() > 0) {
            countryList.clear();
            for (Country c : countryList) {
                dataList.add(c.getCountryName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
        } else {
            getAreaFromServer(cityId, "Country");
        }
        currentLevel = COUNTRY_LEVEL;
    }

    public void getAreaFromServer(String code, final String type) {
        String url = "";
        if (code != null) {
            url = "http://www.weather.com.cn/data/list3/city+" + code + ".xml";
        } else {
            url = "http://www.weather.com.cn/data/list3/city.xml";
        }

        HttpUtil.sendHttpRequest(url, new HttpCallbackLister() {
            @Override
            public void onFinish(String response) {
                HttpUtil.handleResponse(coolWeatherDB, response, type, null);
                //通过runonuithread() 回到主线程处理逻辑
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (type.equals("Province")) {
                            queryProvince();
                        } else if (type.equals("City")) {
                            queryCity(selectedProvince.getProvinceCode());
                        } else if (type.equals("Country")) {
                            queryCountry(selectedCity.getCityCode());
                        }

                    }
                });

            }

            @Override
            public void onError(Exception e) {
                //通过runonuithread() 回到主线程处理逻辑
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChooseAreaActivity.this, "请求失败", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

}
