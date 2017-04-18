package com.sadwyn.iceandfire;

import com.sadwyn.iceandfire.network.Api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sadwyn on 10.04.2017.
 */

public final class InstanceApi {
    private static boolean isLoggerEnabled = false;
    public InstanceApi() {}

    public static Api getApi() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient logger;
        if (isLoggerEnabled) {
            logger = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();
        } else {
            logger = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.MINUTES)
                    .build();
        }

        Retrofit.Builder builder = new Retrofit.Builder()
                .client(logger)
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());


        return builder.build().create(Api.class);
    }
}
