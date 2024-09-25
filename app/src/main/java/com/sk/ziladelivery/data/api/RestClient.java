package com.sk.ziladelivery.data.api;

import android.util.Log;

import com.google.gson.Gson;
import com.sk.ziladelivery.BuildConfig;
import com.sk.ziladelivery.utilities.Aes256;
import com.sk.ziladelivery.utilities.MyApplication;
import com.sk.ziladelivery.utilities.SharePrefs;
import com.sk.ziladelivery.utilities.Utils;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {
    private static Retrofit retrofit;


    public static RestClient getInstance() {
        return new RestClient();
    }

    private RestClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    Response response = chain.proceed(request);

                    if (response.code() == 401) {
                        MyApplication.getInstance().token();
                    }

                    if (response.code() == 200) {
                        if (!request.url().toString().contains("/token") && !request.url().toString().contains("/appVersion") &&
                                !request.url().toString().contains("/UpdateAssignment") &&
                                !request.url().toString().contains("/MobileDelivery/CurrencyUploadChequeImageForMobile") &&
                                !request.url().toString().contains("/DBoyAssignmentDeposit/UploadDboySignature") &&
                                !request.url().toString().contains("/api/AssignmentCopyUpload") &&
                                !request.url().toString().contains("/UPI/GenerateOrderAmtQRCode") &&
                                !request.url().toString().contains("/UPI/TransactionDetail") &&
                                !request.url().toString().contains("/UPI/CheckTransactionStatus") &&
                                !request.url().toString().contains("/api/itemimageupload/UploadKKReturnReplaceImages") &&
                                !request.url().toString().contains("/api/DeliveryCancelledDraft/Image") && !request.url().toString().contains("/UPI/GenerateDeliveryQRCode") && !request.url().toString().contains("/UPI/CheckDeliveryResponse") && !request.url().toString().contains("UPI/DeliveryTransactionDetail")) {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("response", new JSONObject(response.body().string()));
                                String data = jsonObject.getJSONObject("response").getString("Data");
                                String destr = Aes256.decrypt(data, new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).format(new Date()) + "1201");
                                if (BuildConfig.DEBUG)
                                    printMsg(destr);
                                MediaType contentType = response.body().contentType();
                                ResponseBody responseBody = ResponseBody.create(contentType, destr);
                                return response.newBuilder().body(responseBody).build();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    return response;
                })
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .header("username", Utils.getCustMobile())
                            .addHeader("authorization", "Bearer " + Utils.getToken())
                            //.addHeader("NoEncryption", "1")
                            .build();
                    return chain.proceed(request);
                })
                .addInterceptor(interceptor)
                .build();
        retrofit = new Retrofit.Builder()//http://192.168.1.50:7011
                .baseUrl(SharePrefs.getInstance(MyApplication.getInstance()).getString(SharePrefs.BASEURL))
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

    private void printMsg(String msg) {
        int chunkCount = msg.length() / 4050;     // integer division
        for (int i = 0; i <= chunkCount; i++) {
            int max = 4050 * (i + 1);
            if (max >= msg.length()) {
                Log.d("MSG", msg.substring(4050 * i));
            } else {
                Log.d("MSG", msg.substring(4050 * i, max));
            }
        }
    }

    public APIServices getService() {
        return retrofit.create(APIServices.class);
    }
}