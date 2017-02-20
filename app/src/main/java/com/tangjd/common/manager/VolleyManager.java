package com.tangjd.common.manager;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by tangjd on 2016/1/8.
 */
public class VolleyManager {
    private static VolleyManager sVolleyManager;
    private Context mContext;

    public static VolleyManager getInstance() {
        if (sVolleyManager == null) {
            sVolleyManager = new VolleyManager();
        }
        return sVolleyManager;
    }

    public void init(Context context) {
        mContext = context;
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
    }

    private RequestQueue mRequestQueue;

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }
}
