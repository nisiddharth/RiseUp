package com.npdevs.riseup.api.retrofit;

import com.npdevs.riseup.api.responseModels.Response;
import com.npdevs.riseup.api.responseModels.auth.SignInResponse;
import com.npdevs.riseup.api.responseModels.auth.SignUpResponse;
import com.npdevs.riseup.api.responseModels.user.GetEmotionResponse;
import com.npdevs.riseup.api.responseModels.user.GetFriendsResponse;
import com.npdevs.riseup.api.responseModels.user.SaveEmotionResponse;
import com.npdevs.riseup.api.responseModels.user.SaveTokenResponse;
import com.npdevs.riseup.api.responseModels.videos.GetVideoResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {

    //Auth
    @POST("auth/signIn")
    Call<SignInResponse> signIn(@Body Map<String, String> body);

    @POST("auth/signUp")
    Call<SignUpResponse> signUp(@Body Map<String, String> body);

    //User
    @POST("user/save/token")
    Call<SaveTokenResponse> saveDeviceToken(@Header("Authorization") String token, @Body Map<String, String> body);

    @DELETE("user/remove/token")
    Call<Response> removeDeviceToken(@Header("Authorization") String token);

    @POST("user/save/emotion")
    Call<SaveEmotionResponse> saveEmotion(@Header("Authorization") String token, @Body Map<String, List<List<String>>> body);

    @GET("user/get/emotion")
    Call<GetEmotionResponse> getEmotion(@Header("Authorization") String token);

    @GET("friend/get/emotions")
    Call<GetEmotionResponse> getFriendEmotion(@Query("friend_id") String friendId);

    @POST("user/add/friend")
    Call<Response> addFriend(@Header("Authorization") String token, @Body Map<String, String> body);

    @GET("user/requests")
    Call<GetFriendsResponse> getAllRequest(@Header("Authorization") String token);

    @POST("user/accept/invite")
    Call<Response> acceptRequest(@Header("Authorization") String token, @Body Map<String, String> body);

    @GET("user/get/friends")
    Call<GetFriendsResponse> getFriends(@Header("Authorization") String token);

    @GET("video/get")
    Call<GetVideoResponse> getVideos(@Header("Authorization") String token, @Query("emotion") String emotion);

}
