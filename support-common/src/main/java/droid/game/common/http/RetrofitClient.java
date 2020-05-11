package droid.game.common.http;

import droid.game.open.source.okhttp3.OkHttpClient;
import droid.game.open.source.okhttp3.ResponseBody;
import droid.game.open.source.okhttp3.logging.HttpLoggingInterceptor;
import droid.game.open.source.retrofit2.Call;
import droid.game.open.source.retrofit2.Retrofit;
import droid.game.open.source.retrofit2.converter.scalars.ScalarsConverterFactory;
import droid.game.open.source.retrofit2.http.*;

import java.util.Map;

public final class RetrofitClient {
    public static RetrofitClient client(){
        return Holder.holder;
    }

    private Api mApi;
    private OkHttpClient mOkHttpClient;
    RetrofitClient() {
        super();
        mApi = new Retrofit.Builder()
                .baseUrl("https://www.baidu.com")
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(createOkHttpClient())
                .build()
                .create(Api.class);
    }

    public Api api(){
        return mApi;
    }

    private OkHttpClient createOkHttpClient(){
        mOkHttpClient =  new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        return mOkHttpClient;
    }

    public interface Api{
        @GET
        Call<String> get(@Url String url, @HeaderMap Map<String,String> header, @QueryMap Map<String,String> query);

        @FormUrlEncoded
        @POST
        Call<String> post(@Url String url, @HeaderMap Map<String,String> header, @FieldMap Map<String,String> field);
    }
    private static class Holder{
        private static final RetrofitClient holder = new RetrofitClient();
    }
}
