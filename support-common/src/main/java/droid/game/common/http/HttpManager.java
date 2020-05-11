package droid.game.common.http;


public class HttpManager {
    public static Request.Get get(){
        return new Request.Get();
    }

    public static Request.Post post(){
        return new Request.Post();
    }

    public static Request.ConsumerListener newListener(){
        return new Request.ConsumerListener();
    }
}
