package com.manoj.phonyhub.interfaces;

import com.manoj.phonyhub.data.PicsumDataModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PicsumIDInteface {

    @GET("id/{id}/info")
    Call<PicsumDataModel> getImageInfo(@Path("id") int id);

}
