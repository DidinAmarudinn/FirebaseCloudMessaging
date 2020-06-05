package com.example.demo_fcm;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Endpoint {
    @FormUrlEncoded
    @POST(Api.SEND_ENDPOINT)
    Call<ResponseBody> sendNotification(@Field("token") String token,
                                        @Field("title") String title,
                                        @Field("body") String body);
}
