package com.example.beck.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.beck.coolweather.model.City;
import com.example.beck.coolweather.model.Country;
import com.example.beck.coolweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beck on 2016/10/13.
 */
public class CoolWeatherDB {
    private static CoolWeatherDB coolWeatherDB;
    private SQLiteDatabase db;
    private int version = 1;

    private CoolWeatherDB(Context context) {
        CoolWeatherOpenHelper coolWeatherOpenHelper = new CoolWeatherOpenHelper(context, "cool_weather", null, version);
        db = coolWeatherOpenHelper.getWritableDatabase();
    }

    /**
     * 单例获取CoolWeatherDB实例
     *
     * @param context
     * @return
     */
    public static synchronized CoolWeatherDB getInstance(Context context) {
        if (coolWeatherDB == null) {
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    /**
     * 将Province实例存储到数据库
     *
     * @param province
     */
    public void saveProvince(Province province) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("province_name", province.getProvinceName());
        contentValues.put("provicne_code", province.getProvinceCode());
        db.insert("Province", null, contentValues);
    }

    /**
     * 获取全部省份信息
     *
     * @return
     */
    public List<Province> loadProvinces() {
        List<Province> provinceList = new ArrayList<>();
        Cursor cursor = db.query("Province", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                provinceList.add(province);
            } while (cursor.moveToNext());
        }
        return provinceList;
    }

    public void saveCity(City city) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("city_code", city.getCityCode());
        contentValues.put("city_name", city.getCityName());
        contentValues.put("province_id", city.getProvinceId());
        db.insert("City", null, contentValues);
    }

    public List<City> loadCities(String provincId) {
        List<City> cityList = new ArrayList<>();
        Cursor cursor = db.query("City", null, "province_id=?", new String[]{provincId}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setProvinceId(cursor.getString(cursor.getColumnIndex("province_id")));
                cityList.add(city);
            } while (cursor.moveToNext());
        }
        return cityList;
    }

    public void saveCountry(Country country) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("country_code", country.getCountryCode());
        contentValues.put("country_name", country.getCountryName());
        contentValues.put("city_id", country.getCityId());
        db.insert("country", null, contentValues);
    }

    public List<Country> loadCountry(String cityId) {
        List<Country> countryList = new ArrayList<>();
        Cursor cursor = db.query("country", null, "city_id=?", new String[]{cityId}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Country country = new Country();
                country.setId(cursor.getInt(cursor.getColumnIndex("id")));
                country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
                country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
                country.setCityId(cursor.getString(cursor.getColumnIndex("city_id")));
                countryList.add(country);
            } while (cursor.moveToNext());
        }
        return countryList;
    }
}
