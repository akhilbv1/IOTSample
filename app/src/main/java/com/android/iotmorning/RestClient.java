package com.android.iotmorning;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RestClient {

    private static String BaseUrl_Login = "https://www.googleapis.com/identitytoolkit/v3/relyingparty/";

    //replace with new db url
    private static String BaseUrl_Post = "https://iotmorning-d5b3d.firebaseio.com/";
    private final RestApis loginService;
    private Retrofit retrofit = null;

    private final RestApis postService;


    public RestClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("Content-Type", "application/json")
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        httpClient.addInterceptor(logging);

        Retrofit retrofitLogin = new Retrofit.Builder()
                .baseUrl(BaseUrl_Login)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .build();

        Retrofit retrofitPostData = new Retrofit.Builder()
                 .baseUrl(BaseUrl_Post)
                 .addConverterFactory(GsonConverterFactory.create())
                 .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                 .client(httpClient.build())
                 .build();


        loginService = retrofitLogin.create(RestApis.class);

        postService = retrofitPostData.create(RestApis.class);

    }

    public RestApis getLoginService() {
        return loginService;
    }
    public RestApis getPostService(){
        return  postService;
    }
    /*

    private Converter.Factory createGsonConverter() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(EventKeyModel.class, new EventDetailsDeserializer());
        Gson gson = gsonBuilder.create();
        return GsonConverterFactory.create(gson);
    }*/
}
