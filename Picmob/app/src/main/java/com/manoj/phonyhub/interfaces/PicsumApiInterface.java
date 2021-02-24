package com.manoj.phonyhub.interfaces;

import com.manoj.phonyhub.data.PicsumDataModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PicsumApiInterface {

    //@GET("/v2/list?page=2&limit=36")
    //Call<List<PicsumDataModel>> getPicsumList();

    @GET("v2/list")
    Call<List<PicsumDataModel>> getPicsumList(
            @Query("page") int pageNo,
            @Query("limit") int itemLimit
    );

//    @GET("v2/list")
//    Call<List<PicsumDataModel>> getPicsumlist(@QueryMap Map<String, Integer> pageLimitMap);

}
