package com.example.beck.coolweather.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.beck.coolweather.R;
import com.example.beck.coolweather.util.HttpCallbackLister;
import com.example.beck.coolweather.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beck on 2016/10/14.
 */
public class ChooseAreaActivity extends Activity {
    private TextView title;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_area);
        title = (TextView) findViewById(R.id.title_text);
        listView = (ListView) findViewById(R.id.listView);
    }

    public void showAllProvince() {
        title.setText("中国");
        List provinceList = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter<String>(ChooseAreaActivity.this, android.R.layout.simple_list_item_1, provinceList);
        listView.setAdapter(adapter);
    }
    public void getAreaFromServer(){
        String url="http://www.weather.com.cn/data/list3/city.xml";
        HttpUtil.sendHttpRequest(url, new HttpCallbackLister() {
            @Override
            public void onFinish(String response) {

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

}
