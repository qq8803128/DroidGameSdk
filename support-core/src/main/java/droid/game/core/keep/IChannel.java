package droid.game.core.keep;

import android.app.Activity;
import droid.game.annotation.NonNull;
import droid.game.annotation.Nullable;
import droid.game.common.keep.Consumer;
import droid.game.core.parameter.Parameter;
import droid.game.core.result.Result;

public interface IChannel extends IActivity{

    /**
     * 执行sdk初始化
     * @param activity
     * @param parameter
     * @param success
     * @param failed
     */
    void init(@NonNull Activity activity, @NonNull Parameter parameter, @NonNull Consumer<Result> success, @NonNull Consumer<Result> failed);

    /**
     * 执行sdk登录
     * @param activity
     * @param parameter
     */
    void login(@NonNull Activity activity, @Nullable Parameter parameter, @NonNull Consumer<Result> success, @NonNull Consumer<Result> failed);

    /**
     * 执行sdk登出
     * @param activity
     * @param parameter
     */
    void logout(@NonNull Activity activity, @Nullable Parameter parameter, @NonNull Consumer<Result> success, @NonNull Consumer<Result> failed);

    /**
     * 提交角色信息
     * @param activity
     * @param parameter
     */
    void role(@NonNull Activity activity, @NonNull Parameter parameter, @NonNull Consumer<Result> success, @NonNull Consumer<Result> failed);

    /**
     * 执行sdk支付
     * @param activity
     * @param parameter
     */
    void pay(@NonNull Activity activity, @NonNull Parameter parameter, @NonNull Consumer<Result> success, @NonNull Consumer<Result> failed);

    /**
     * 执行sdk退出
     * @param activity
     * @param parameter
     */
    void exit(@NonNull Activity activity, @Nullable Parameter parameter, @NonNull Consumer<Result> success, @NonNull Consumer<Result> failed);

    /**
     * 执行sdk扩展事件
     * @param activity
     * @param parameter
     */
    void exec(@NonNull Activity activity, @NonNull Parameter parameter, @NonNull Consumer<Result> success, @NonNull Consumer<Result> failed);
}
