package droid.game.core.parameter;

import droid.game.annotation.Explain;

public class GameParam extends Parameter<GameParam>{
    @Explain(description = "游戏是否支持退出账号功能")
    public static final String GAME_CAN_LOGOUT                      = "canLogout";
    @Explain(description = "游戏是否支持SDK直接切换用户功能")
    public static final String GAME_SWITCH_ACCOUNT                  = "switchAccount";

    public GameParam setCanLogout(boolean canLogout){
        put(GAME_CAN_LOGOUT,canLogout);
        return this;
    }

    public GameParam setCanSwitchAccount(boolean canSwitchAccount){
        put(GAME_SWITCH_ACCOUNT,canSwitchAccount);
        return this;
    }

    public boolean canLogout(){
        return get(GAME_CAN_LOGOUT,false);
    }

    public boolean canSwitchAccount(){
        return get(GAME_SWITCH_ACCOUNT,false);
    }
}
