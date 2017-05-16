package com.tangjd.common.abs;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tangjd.common.manager.VolleyManager;
import com.tangjd.common.utils.Log;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by tangjd on 2016/8/8.
 */
public class JsonApiBase {
    public static final String TAG = "Api";

    public static Request doGetRequest(String url, OnJsonResponseListener listener) {
        return doRequest(Request.Method.GET, url, null, listener);
    }

    public static Request doGetRequest(String url, Map<String, String> params, OnJsonResponseListener listener) {
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

    public static Request doPostRequest(String url, JSONObject params, OnJsonResponseListener listener) {
        return doRequest(Request.Method.POST, url, params, listener);
    }

    public static Request doPostRequest(String url, JSONObject params, Map<String, String> headers, OnJsonResponseListener listener) {
        return doRequest(Request.Method.POST, url, params, headers, listener);
    }

    private static Request doRequest(int method, final String url, final JSONObject params, final OnJsonResponseListener listener) {
        return doRequest(method, url, params, null, listener);
    }

    public static Request doRequest(int method, final String url, final JSONObject params, final Map<String, String> headers, final OnJsonResponseListener listener) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        JsonObjectRequest request = new JsonObjectRequest(method, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.w(TAG, "----------------");
                Log.i(TAG, url
                        + "\n headers " + (headers == null ? null : headers.toString())
                        + "\n params " + (params == null ? null : params.toString())
                        + "\n resp " + response);
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
                Log.e(TAG, url
                        + "\n headers " + (headers == null ? null : headers.toString())
                        + "\n params " + (params == null ? null : params.toString())
                        + "\n error " + error);
                Log.w(TAG, "----------------");
                if (listener != null) {
                    listener.onFinish(false);
                    listener.onError(parseVolleyError(error));
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (headers == null) {
                    return super.getHeaders();
                }
                return headers;
            }
        };
        VolleyManager.getInstance().getRequestQueue().add(request);
        return request;
    }

    public interface OnJsonResponseListener {
        void onResponse(JSONObject response);

        void onError(String error);

        void onFinish(boolean withoutException);
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
