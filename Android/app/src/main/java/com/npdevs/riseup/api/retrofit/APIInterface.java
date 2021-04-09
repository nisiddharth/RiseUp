package com.npdevs.riseup.api.retrofit;

import com.npdevs.riseup.api.responseModels.auth.SignInResponse;
import com.npdevs.riseup.api.responseModels.auth.SignUpResponse;
import com.npdevs.riseup.api.responseModels.user.GetEmotionResponse;
import com.npdevs.riseup.api.responseModels.user.SaveEmotionResponse;
import com.npdevs.riseup.api.responseModels.user.SaveTokenResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface APIInterface {

    //Auth
    @POST("auth/signIn")
    Call<SignInResponse> signIn(@Body Map<String, String> body);
    @POST("auth/signUp")
    Call<SignUpResponse> signUp(@Body Map<String, String> body);

    //User
    @POST("user/save/token")
    Call<SaveTokenResponse> saveDeviceToken(@Body Map<String, String> body);
    @POST("user/save/emotion")
    Call<SaveEmotionResponse> saveEmotion(@Body Map<String, List<List<String>>> body);
    @GET("user/get/emotion")
    Call<GetEmotionResponse> getEmotion();

}
