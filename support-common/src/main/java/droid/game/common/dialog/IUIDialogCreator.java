package droid.game.common.dialog;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import droid.game.annotation.NonNull;
import droid.game.annotation.Nullable;
import droid.game.common.dialog.widget.UIDialogRootLayout;
import droid.game.common.dialog.widget.UIDialogView;

public interface IUIDialogCreator {
    @NonNull
    UIDialog create();

    @NonNull
    UIDialog onCreateDialog(@NonNull Context context);
    void configDialog(UIDialog dialog);

    @NonNull
    UIDialogView onCreateDialogView(@NonNull Context context);

    @NonNull
    FrameLayout.LayoutParams onCreateDialogViewLayoutParam();

    void configDialogView(@NonNull UIDialogView dialogView);

    @NonNull
    UIDialogRootLayout onCreateDialogRootLayout(@NonNull Context context);
    void configDialogRootLayout(@NonNull UIDialogRootLayout dialogRootLayout);

    @Nullable View onCreateTitleView(@NonNull UIDialog dialog, @NonNull UIDialogView dialogView, @NonNull Context context);
    @Nullable LinearLayout.LayoutParams onCreateTitleViewParams(@NonNull Context context);
    void configTitleView(View titleView);

    @Nullable View onCreateContentView(@NonNull UIDialog dialog,@NonNull UIDialogView dialogView,@NonNull Context context);
    @Nullable LinearLayout.LayoutParams onCreateContentViewParams(@NonNull Context context);

    @Nullable View onCreateOperatorView(@NonNull UIDialog dialog,@NonNull UIDialogView dialogView,@NonNull Context context);
    @Nullable LinearLayout.LayoutParams onCreateOperatorViewParams(@NonNull Context context);
    void configOperatorView(View operatorView);

    void onAfterCreate(@NonNull UIDialog dialog, @NonNull UIDialogRootLayout rootLayout, @NonNull Context context);
}
