package com.tangjd.common.abs;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tangjd.common.manager.VolleyManager;
import com.tangjd.common.utils.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by tangjd on 2016/8/8.
 */
public class ApiBase {
    public static final String TAG = "Api";

    public static Request doGetRequest(String url, OnResponseListener listener) {
        return doRequest(Request.Method.GET, url, null, listener);
    }

    private static Request doGetRequest(String url, Map<String, String> params, OnResponseListener listener) {
        if (params != null && params.size() != 0) {
            String paramsStr = "";
            for (Map.Entry<String, String> entry : params.entrySet()) {
                try {
                    paramsStr = paramsStr + entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF-8") + "&";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            paramsStr = paramsStr.substring(0, paramsStr.length() - 1);
            url = url + "?" + paramsStr;
        }
        return doGetRequest(url, listener);
    }

    public static Request doPostRequest(String url, Map<String, String> params, OnResponseListener listener) {
        return doRequest(Request.Method.POST, url, params, listener);
    }

    private static Request doRequest(int method, final String url, final Map<String, String> params, final OnResponseListener listener) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        StringRequest request = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.w(TAG, "----------------");
                Log.i(TAG, url + "\n params " + (params == null ? null : params.toString()) + "\n resp " + response);
                Log.w(TAG, "----------------");
                if (listener != null) {
                    listener.onFinish(true);
                    listener.onResponse(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.w(TAG, "----------------");
                Log.e(TAG, url + "\n params " + (params == null ? null : params.toString()) + "\n error " + error);
                Log.w(TAG, "----------------");
                if (listener != null) {
                    listener.onFinish(false);
                    listener.onError(parseVolleyError(error));
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (params != null) {
                    return params;
                }
                return super.getParams();
            }
        };
        VolleyManager.getInstance().getRequestQueue().add(request);
        return request;
    }

    public interface OnResponseListener {
        void onResponse(String response);

        void onError(String error);

        void onFinish(boolean withOutException);
    }

    private static String parseVolleyError(VolleyError error) {
        error.printStackTrace();
        String statusCodeStr = "";
        if (error.networkResponse != null) {
            statusCodeStr = " " + error.networkResponse.statusCode;
        }
        return "连接失败 " + statusCodeStr;
    }
}
