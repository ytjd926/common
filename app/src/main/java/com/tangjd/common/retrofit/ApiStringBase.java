package com.tangjd.common.retrofit;

import com.tangjd.common.utils.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by tangjd on 2017/11/17.
 */

public class ApiStringBase {
    public static final String TAG = "Api";

    private static Retrofit sStringRetrofit;

    public static Retrofit getStringRetrofit(String domainName) {
        if (sStringRetrofit == null) {
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
            sStringRetrofit = new Retrofit.Builder()
                    .baseUrl(domainName)
                    .addConverterFactory(StringConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return sStringRetrofit;
    }
}
