package droid.game.plugin.sdk.delegate.http;

import droid.game.open.source.reactivex.Observable;
import droid.game.open.source.retrofit2.http.*;
import droid.game.plugin.sdk.delegate.json.JsonCode;

import java.util.Map;

public interface Api {
    @FormUrlEncoded
    @POST("v2/{field}/active")
    Observable<JsonCode> init(
            @Path("field") String field,
            @HeaderMap Map<String, String> headers,
            @FieldMap Map<String, String> bodys
    );


}
