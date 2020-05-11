package droid.game.core.keep;

import android.app.Activity;
import droid.game.annotation.NonNull;
import droid.game.annotation.Nullable;
import droid.game.core.parameter.Parameter;
import droid.game.core.result.Result;

public interface IGame {
    /**
     * 注册sdk事件回调监听器
     * @param eventReceiver
     */
    void registerSDKEventReceiver(@NonNull IEventReceiver eventReceiver);

    /**
     * 解除sdk事件回调监听器
     * @param eventReceiver
     */
    void unregisterSDKEventReceiver(@NonNull IEventReceiver eventReceiver);

    /**
     * 执行sdk初始化
     * @param activity 当前activity对象
     * @param parameter sdk初始化参数
     */
    void init(@NonNull Activity activity, @NonNull Parameter parameter);

    /**
     * 执行sdk登录
     * @param activity
     * @param parameter
     */
    void login(@NonNull Activity activity, @Nullable Parameter parameter);

    /**
     * 执行sdk登出
     * @param activity
     * @param parameter
     */
    void logout(@NonNull Activity activity, @Nullable Parameter parameter);

    /**
     * 提交角色信息
     * @param activity
     * @param parameter
     */
    void role(@NonNull Activity activity, @NonNull Parameter parameter);

    /**
     * 执行sdk支付
     * @param activity
     * @param parameter
     */
    void pay(@NonNull Activity activity, @NonNull Parameter parameter);

    /**
     * 执行sdk退出
     * @param activity
     * @param parameter
     */
    void exit(@NonNull Activity activity, @Nullable Parameter parameter);

    /**
     * 执行sdk扩展事件
     * @param activity
     * @param parameter
     */
    void exec(@NonNull Activity activity, @NonNull Parameter parameter);

    /**
     * 执行sdk扩展事件
     * @param activity
     * @param parameter
     * @param resultCreator
     */
    void exec(@NonNull Activity activity,@NonNull Parameter parameter,@Nullable IResult resultCreator);

    interface IResult{
        @NonNull Result onResult(@NonNull Result result);
    }
}
