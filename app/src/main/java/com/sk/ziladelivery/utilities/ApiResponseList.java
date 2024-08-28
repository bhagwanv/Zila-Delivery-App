package com.sk.ziladelivery.utilities;

import com.google.gson.JsonArray;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

import static com.sk.ziladelivery.utilities.Status.ERROR;
import static com.sk.ziladelivery.utilities.Status.LOADING;
import static com.sk.ziladelivery.utilities.Status.SUCCESS;


/**
 * Created by ${Saquib} on 03-05-2018.
 */

public class ApiResponseList {

    public final Status status;

    /*@Nullable
    public final JsonElement data;*/
    @Nullable
    public final JsonArray data;
    @Nullable
    public final Throwable error;

    private ApiResponseList(Status status, @Nullable JsonArray data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static ApiResponseList loading() {
        return new ApiResponseList(LOADING, null, null);
    }

    public static ApiResponseList success(@NonNull JsonArray data) {
        return new ApiResponseList(SUCCESS, data, null);
    }

    public static ApiResponseList error(@NonNull Throwable error) {
        return new ApiResponseList(ERROR, null, error);
    }

}
