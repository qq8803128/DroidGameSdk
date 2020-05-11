package droid.game.common.http;

import android.text.TextUtils;
import droid.game.common.keep.Action;
import droid.game.common.keep.Consumer;
import droid.game.open.source.okhttp3.ResponseBody;
import droid.game.open.source.retrofit2.Call;
import droid.game.open.source.retrofit2.Callback;
import droid.game.open.source.retrofit2.Response;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Request<T extends Request> {
    protected String mUrl;
    protected Map<String, String> mHeader;
    protected Map<String, String> mBody;

    public Request() {
        super();
        mHeader = Collections.synchronizedMap(new HashMap<String, String>());
        mBody = Collections.synchronizedMap(new HashMap<String, String>());
    }

    public T url(String url) {
        mUrl = url;
        return (T) this;
    }

    public T header(String key, String value) {
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            mHeader.put(key, value);
        }
        return (T) this;
    }

    public T header(Map<String, String> map) {
        mHeader.putAll(new HttpMap().addAll(map));
        return (T) this;
    }

    public T body(String key, String value) {
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            mBody.put(key, value);
        }
        return (T) this;
    }

    public T body(Map<String, String> map) {
        mBody.putAll(new HttpMap().addAll(map));
        return (T) this;
    }

    public static class Get extends Request<Get> {
        public void enqueue(final RequestListener listener) {
            listener.onStart();
            RetrofitClient.client().api().get(mUrl, mHeader, mBody)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            try {
                                if (response != null && response.isSuccessful()) {
                                    listener.onResponse(response.body());
                                } else {
                                    listener.onError(new Throwable(response.errorBody().string()));
                                }
                            }catch (Throwable e){
                                e.printStackTrace();
                            }
                            listener.onComplete();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable throwable) {
                            try{
                                listener.onError(throwable);
                            }catch (Throwable e){
                                e.printStackTrace();
                            }
                            listener.onComplete();
                        }
                    });
        }
    }

    public static class Post extends Request<Post> {
        public void enqueue(final RequestListener listener) {
            listener.onStart();
            RetrofitClient.client().api().post(mUrl, mHeader, mBody)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            try {
                                if (response != null && response.isSuccessful()) {
                                    listener.onResponse(response.body());
                                } else {
                                    listener.onError(new Throwable(response.errorBody().string()));
                                }
                            }catch (Throwable e){
                                e.printStackTrace();
                            }
                            listener.onComplete();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable throwable) {
                            try{
                                listener.onError(throwable);
                            }catch (Throwable e){
                                e.printStackTrace();
                            }
                            listener.onComplete();
                        }
                    });
        }
    }


    public interface RequestListener {
        void onStart();
        void onResponse(String response);
        void onError(Throwable t);
        void onComplete();
    }

    public static class ConsumerListener implements RequestListener{
        public ConsumerListener doOnStart(Action action){
            return this;
        }

        public ConsumerListener doOnNext(Consumer<String> next){
            return this;
        }

        public ConsumerListener doOnError(Consumer<String> error){
            return this;
        }

        public ConsumerListener doOnComplete(Action action){
            return this;
        }

        @Override
        public void onStart() {

        }

        @Override
        public void onResponse(String response) {

        }

        @Override
        public void onError(Throwable t) {

        }

        @Override
        public void onComplete() {

        }
    }
}
