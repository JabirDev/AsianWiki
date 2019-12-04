package com.jemberdeveloper.asianwikitutorial.network;

import com.jemberdeveloper.asianwikitutorial.app.Config;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

public class Koneksi {
    public interface AsianWiki {
        @GET
        Call<ResponseBody> getData(@Url String url);
    }

    private static AsianWiki asianWiki = null;
    public static AsianWiki getAsianWiki(){
        if (asianWiki == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Config.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            asianWiki = retrofit.create(AsianWiki.class);
        }
        return asianWiki;
    }

}
