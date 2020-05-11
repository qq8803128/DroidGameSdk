package droid.game.core.parameter;

import droid.game.annotation.Explain;

public class UserParam extends Parameter<UserParam>{
    @Explain(description = "用户id")
    public static final String LOGIN_USER_ID                        = "userId";
    @Explain(description = "用户token")
    public static final String LOGIN_USER_TOKEN                     = "userToken";

    public String getUserId() {
        return get(LOGIN_USER_ID,"");
    }

    public String getUserToken() {
        return get(LOGIN_USER_TOKEN,"");
    }
}
