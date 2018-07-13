package com.zerone_catering.retrofitIp;

import com.zerone_catering.Contants.Content;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by on 2018-07-11 10 21.
 * Author  LiuXingWen
 */

public class ResponseUtils {
    public static CateringIp getCateringIp() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Content.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(CateringIp.class);
    }
}
