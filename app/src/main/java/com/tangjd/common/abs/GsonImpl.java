package com.tangjd.common.abs;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: tangjd
 * Date: 2017/5/8
 */

public class GsonImpl {
    private static GsonImpl sGsonImpl;
    private Gson mGson = new Gson();

    private GsonImpl() {
    }

    public static GsonImpl instance() {
        if (sGsonImpl == null) {
            sGsonImpl = new GsonImpl();
        }
        return sGsonImpl;
    }

    public String toJson(Object src) {
        return mGson.toJson(src);
    }

    public <T> T toObject(String json, Class<T> claxx) {
        return mGson.fromJson(json, claxx);
    }

    public <T> T toObject(byte[] bytes, Class<T> claxx) {
        return mGson.fromJson(new String(bytes), claxx);
    }

    public <T> List<T> toList(String json, Class<T> claxx) {
        Type type = new TypeToken<ArrayList<T>>() {
        }.getType();
        List<T> list = mGson.fromJson(json, type);
        return list;
    }
}
