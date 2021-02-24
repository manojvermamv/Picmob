package com.manoj.phonyhub.interfaces;

import com.manoj.phonyhub.data.PicsumDataModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TestApiInterface {
    @GET("v2/list")
    Call<List<PicsumDataModel>> getPicsumList(
            @Query("page") int page,
            @Query("limit") int limit
    );
}
