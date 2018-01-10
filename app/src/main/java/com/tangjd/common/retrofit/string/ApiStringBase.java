package com.tangjd.common.retrofit.string;

import com.tangjd.common.utils.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by tangjd on 2017/11/17.
 */

public class ApiStringBase {
    public static final String TAG = "ApiTT";

    public static Retrofit getStringRetrofit(String domainName) {
        //日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.i(TAG, message);
            }
        });
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.retryOnConnectionFailure(false);
        // Default timeout time
        builder.connectTimeout(10_000, TimeUnit.MILLISECONDS);
        builder.readTimeout(30_000, TimeUnit.MILLISECONDS);
        builder.writeTimeout(10_000, TimeUnit.MILLISECONDS);
        builder.addInterceptor(loggingInterceptor);
        OkHttpClient okHttpClient = builder.build();
        loggingInterceptor.setLevel(level);
        Retrofit sStringRetrofit = new Retrofit.Builder()
                .baseUrl(domainName)
                .addConverterFactory(StringConverterFactory.create())
                .client(okHttpClient)
                .build();
        return sStringRetrofit;
    }

    public static void enqueue(Call<String> call, final OnStringRespListener listener) {
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (listener != null) {
                    listener.onFinish(true);
                    try {
                        listener.onResponse(response.body());
                    } catch (Exception e) {
                        e.printStackTrace();
                        listener.onError("数据格式出错");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (listener != null) {
                    listener.onFinish(false);
                    try {
                        listener.onError("连接失败\n" + t.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
