package com.tangjd.common.retrofit.string;

/**
 * Created by tangjd on 2017/11/24.
 */

public interface OnStringRespListener {

    void onResponse(String response);

    void onError(String error);

    void onFinish(boolean withoutException);
}
