package com.jaedeuk.notepad.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaedeuk.notepad.model.Category;
import com.jaedeuk.notepad.model.Memo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 앱이 꺼져도 캐시에 데이터 저장
 */
public class Preference {
    final static String TAG = "Preference";
    private final String mPreferenceId = "com.jaedeuk.notepad";

    static Context mContext;

    public Preference(Context c) {
        mContext = c;
    }

    public void putString(String key, String value) {
        SharedPreferences pref = mContext.getSharedPreferences(mPreferenceId, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 카테고리 데이터를 받아 Gson으로 변환 후 저장
     */
    public void putCategoryList(List<Category> value) {
        SharedPreferences pref = mContext.getSharedPreferences(mPreferenceId, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("CategoryList", toJson((value)));
        editor.commit();
    }

    public void putMemoList(List<Memo> value, String name) {
        SharedPreferences pref = mContext.getSharedPreferences(mPreferenceId, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(name, toJson((value)));
        editor.commit();
    }

    public void putCategoryHashMap(HashMap<String, String> value){
        SharedPreferences pref = mContext.getSharedPreferences(mPreferenceId, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("CategoryHashMap", toJson((value)));
        editor.commit();
    }

    public static String toJson(Object jsonObject) {
        return new Gson().toJson(jsonObject);
    }

    public List<Category> getCategoryList(){
        SharedPreferences pref = mContext.getSharedPreferences(mPreferenceId, Activity.MODE_PRIVATE);
        List<Category> categoryList = new ArrayList<>();
        try{
            String json = pref.getString("CategoryList","");
            categoryList = (ArrayList<Category>)fromJson(json, new TypeToken<ArrayList<Category>>() {}.getType());
            return categoryList;
        }catch (Exception e){
            Log.e(TAG,"gson 변환 오류"+e.getMessage());
        }
            return categoryList;
    }

    public List<Memo> getMemoList(String name){
        SharedPreferences pref = mContext.getSharedPreferences(mPreferenceId, Activity.MODE_PRIVATE);
        List<Memo> memoList = new ArrayList<>();
        try{
            String json = pref.getString(name,"");
            memoList = (ArrayList<Memo>)fromJson(json, new TypeToken<ArrayList<Memo>>() {}.getType());
            return memoList;
        }catch (Exception e){
            Log.e(TAG,"gson 변환 오류"+e.getMessage());
        }
        return memoList;
    }


    public HashMap<String, String> getCategoryHashMap(){
        SharedPreferences pref = mContext.getSharedPreferences(mPreferenceId, Activity.MODE_PRIVATE);
        HashMap<String, String> categoryHashMap = new HashMap<>();
        try{
            String json = pref.getString("CategoryHashMap","");
            categoryHashMap = (HashMap<String, String>)fromJson(json, new TypeToken<HashMap<String, String>>() {}.getType());
            return categoryHashMap;
        }catch (Exception e){
            Log.e(TAG,"gson 변환 오류"+e.getMessage());
        }
        return categoryHashMap;
    }

    public static Object fromJson(String jsonString, Type type) {
        return new Gson().fromJson(jsonString, type);
    }


    public String getString(String key) {
        try {
            return getString(key, "");
        } catch (Exception e) {}

        return "";
    }

    public String getString(String key, String val) {
        SharedPreferences pref = mContext.getSharedPreferences(mPreferenceId, Activity.MODE_PRIVATE);
        try {
            return pref.getString(key, val);
        } catch (Exception e) {
            return val;
        }
    }
}
