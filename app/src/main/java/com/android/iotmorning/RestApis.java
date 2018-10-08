package com.android.iotmorning;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestApis {

    //give api key
    @POST("verifyPassword?key=AIzaSyDMf5WWdOuljBJguzrubD1b0bsTbWfmq0Q")
    Single<Response<LoginResponse>> loginUser(@Body LoginBody loginBody);

    //give api key
    @POST("signupNewUser?key=AIzaSyDMf5WWdOuljBJguzrubD1b0bsTbWfmq0Q")
    Single<Response<LoginResponse>> registerUser(@Body LoginBody loginBody);

    @POST("users/{uid}")
    Single<Response<Void>> registerUserDb(@Path("uid") String uid, @Body UserPojo userPojo);
}
