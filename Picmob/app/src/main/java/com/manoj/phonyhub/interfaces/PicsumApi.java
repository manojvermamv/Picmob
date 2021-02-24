package com.manoj.phonyhub.interfaces;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PicsumApi {

    public static Retrofit retrofit = null;

    public static PicsumApiInterface getClient() {

        // base url gose here
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://picsum.photos/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        // creating object for our interface
        PicsumApiInterface picsumApiInterface = retrofit.create(PicsumApiInterface.class);
        return picsumApiInterface;
    }

}
