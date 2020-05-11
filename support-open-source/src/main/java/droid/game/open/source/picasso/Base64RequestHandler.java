package droid.game.open.source.picasso;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by admin on 2018/5/29.
 */

public class Base64RequestHandler extends RequestHandler{
    public static final String[] SCHEME_BASE64 = new String[]{
            "data:image/png;base64,",
            "data:image/jepg;base64,"
    };
    @Override public boolean canHandleRequest(Request data) {
        for (String scheme : SCHEME_BASE64){
            boolean result = data.uri.toString().startsWith(scheme);
            if (result)return result;
        }
        return false;
    }

    @Override public Result load(Request request, int networkPolicy) throws IOException {
        return new Result(getInputStream(request), Picasso.LoadedFrom.DISK);
    }

    private InputStream getInputStream(Request request) {
        String imageString = null;
        for (String scheme : SCHEME_BASE64){
            boolean result = request.uri.toString().startsWith(scheme);
            if (result)imageString = request.uri.toString().replaceFirst(scheme,"");
        }
        return new ByteArrayInputStream(Base64.decode(imageString, Base64.DEFAULT));
    }
}
