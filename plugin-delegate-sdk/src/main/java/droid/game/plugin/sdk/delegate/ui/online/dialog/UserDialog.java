package droid.game.plugin.sdk.delegate.ui.online.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import droid.game.butterknife.annotation.BindView;
import droid.game.common.dialog.UIDialog;
import droid.game.common.dialog.widget.UIDialogRootLayout;
import droid.game.common.dialog.widget.UIDialogView;
import droid.game.common.util.Utils;
import droid.game.plugin.sdk.delegate.R3;
import droid.game.x2c.annotation.Xml;

import static droid.game.common.util.Utils.windowHeight;
import static droid.game.common.util.Utils.windowWidth;


@Xml(layouts = {"droid_game_sdk_self_dialog_user_center"})
public class UserDialog extends Dialog<UserDialog> {

    @BindView(R3.id.droid_game_sdk_self_user_center_container)
    View mContainer;

    @BindView(R3.id.droid_game_sdk_self_user_center_vertical_shadow)
    View mVerticalShadowView;

    @BindView(R3.id.droid_game_sdk_self_user_center_horizontal_shadow)
    View mHorizontalShadowView;

    @BindView(R3.id.droid_game_sdk_selft_h_user_controller)
    View mHorizontalController;

    @BindView(R3.id.droid_game_sdk_selft_v_user_controller)
    View mVerticalController;

    public UserDialog(Context context) {
        super(context, 1.0f);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        if (Utils.portrait()) {
            setGravity(Gravity.BOTTOM);
        } else {
            setGravity(Gravity.LEFT);
        }
        setDimAmount(0.0f);
    }

    @Override
    protected String getLayout() {
        return R3.layout.droid_game_sdk_self_dialog_user_center;
    }

    @Override
    public UIDialog onCreateDialog(Context context) {
        UIDialog uiDialog = super.onCreateDialog(context);
        uiDialog.setFullScreen();
        return uiDialog;
    }

    @Override
    public void show() {
        super.show();
        getDialog().setFullScreen();
    }

    @Override
    public void configDialogRootLayout(UIDialogRootLayout dialogRootLayout) {
        super.configDialogRootLayout(dialogRootLayout);
        dialogRootLayout.setPadding(0, 0, 0, 0);
    }

    @Override
    public void configDialogView(UIDialogView dialogView) {
        super.configDialogView(dialogView);
        if (Utils.portrait()) {
            dialogView.setPadding(0, (int) (windowHeight() * 0.4f), 0, 0);
        } else {
            dialogView.setPadding(0, 0, (int) (windowWidth() * 0.5f), 0);
        }
        dialogView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public FrameLayout.LayoutParams onCreateDialogViewLayoutParam() {
        return new FrameLayout.LayoutParams(windowWidth(), ViewGroup.LayoutParams.MATCH_PARENT);
    }


    @Override
    public void onAfterCreate(UIDialog dialog, UIDialogRootLayout rootLayout, Context context) {
        super.onAfterCreate(dialog, rootLayout, context);
        dialog.setFullScreen();
        setup();
    }

    private void setup() {
        float startX = !Utils.portrait() ? -1 : 0;
        float startY = !Utils.portrait() ? 0 : 1;
        float endX = 0;
        float endY = 0;
        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, startX, Animation.RELATIVE_TO_SELF, endX,
                Animation.RELATIVE_TO_SELF, startY, Animation.RELATIVE_TO_SELF, endY
        );
        animation.setDuration(250);
        mContainer.startAnimation(animation);
        mContainer.setVisibility(View.VISIBLE);
        mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mHorizontalShadowView.setVisibility(!Utils.portrait() ? View.GONE : View.VISIBLE);
        mVerticalShadowView.setVisibility(!Utils.portrait() ? View.VISIBLE : View.GONE);

        mVerticalController.setVisibility(!Utils.portrait() ? View.VISIBLE : View.GONE);
        mHorizontalController.setVisibility(!Utils.portrait() ? View.GONE : View.VISIBLE);
    }

    private void dismiss() {
        float startX = 0;
        float startY = 0;
        float endX = !Utils.portrait() ? -1 : 0;
        float endY = !Utils.portrait() ? 0 : 1;
        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, startX, Animation.RELATIVE_TO_SELF, endX,
                Animation.RELATIVE_TO_SELF, startY, Animation.RELATIVE_TO_SELF, endY
        );

        animation.setDuration(250);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mContainer.setVisibility(View.GONE);
                getDialog().dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mContainer.startAnimation(animation);
    }
}
