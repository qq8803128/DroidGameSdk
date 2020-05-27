package droid.game.android.floater.window;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.*;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import droid.game.annotation.FloatRange;
import droid.game.common.global.Global;
import droid.game.common.preferences.Prefs;

import java.util.ArrayList;
import java.util.List;

import static droid.game.common.util.Utils.*;

public class FtWindowManager {
    final int TOP = 0;
    final int LEFT = 1;
    final int RIGHT = 2;
    final int BOTTOM = 3;

    float xPercent = 0.0f;
    float yPercent = 0.5f;

    int size = 40;
    List<MenuItem> menus = new ArrayList<>();
    boolean storagePosition = false;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private FtContainer mContainer;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            ValueAnimator animator = ValueAnimator.ofFloat(1.0f, 0.5f);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    float x = (mLayoutParams.x == 0 ? -dp(size) / 2 : dp(size) / 2);
                    mContainer.getChildAt(0).setTranslationX(x * Math.abs(value - 1f) * 2);
                }
            });
            animator.addListener(new AnimationEndListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mContainer.getChildAt(0).invalidate();
                }
            });
            animator.setDuration(200);
            animator.start();
        }
    };

    FtWindowManager(Activity activity) {
        mWindowManager = activity.getWindowManager();
        mLayoutParams = new WindowManager.LayoutParams();
        mContainer = new FtContainer(activity);

        mContainer.setOnTouchListener(new MoveListener() {
            @Override
            protected void onActionDown(float x, float y, View v, MotionEvent event) {
                startAnimation();
            }

            @Override
            protected void onActionMove(float x, float y, View v, MotionEvent event) {
                move(x, y);
            }

            @Override
            protected void onActionUp(float x, float y, View v, MotionEvent event) {
                reset();
            }

            @Override
            protected void onClick(View v, MotionEvent event) {
                //reset();
                showMenu();
            }
        });
    }

    public void addView(View view) {
        try {
            if (mContainer.getChildAt(0) != view) {
                mContainer.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mWindowManager.addView(mContainer, mLayoutParams);
                endAnimation();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void removeView(View view) {
        try {
            mContainer.removeAllViews();
            mWindowManager.removeViewImmediate(mContainer);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public WindowManager.LayoutParams getLayoutParams() {
        return mLayoutParams;
    }

    FtWindowManager setXPosition(@FloatRange(from = 0.0f, to = 1.0f) float percent) {
        xPercent = percent;
        return this;
    }

    FtWindowManager setYPosition(@FloatRange(from = 0.0f, to = 1.0f) float percent) {
        yPercent = percent;
        return this;
    }

    FtWindowManager setSize(int size) {
        this.size = size;
        return this;
    }

    FtWindowManager storagePosition(boolean storagePosition) {
        this.storagePosition = storagePosition;
        return this;
    }

    FtWindowManager menu(List<MenuItem> menus) {
        this.menus.addAll(menus);
        return this;
    }

    FtWindowManager build() {
        if (storagePosition) {
            readPosition();
        }
        mLayoutParams.x = (int) (xPercent * windowWidth() - dp(size) / 2);
        if (mLayoutParams.x < 0) {
            mLayoutParams.x = 0;
        }
        if (mLayoutParams.x > windowWidth() - dp(size)) {
            mLayoutParams.x = windowWidth() - dp(size);
        }
        mLayoutParams.y = (int) (yPercent * windowHeight() - dp(size) / 2);
        if (mLayoutParams.y < 0) {
            mLayoutParams.y = 0;
        }
        if (mLayoutParams.y > windowHeight() - dp(size)) {
            mLayoutParams.y = windowHeight() - dp(size);
        }
        mLayoutParams.width = dp(size);
        mLayoutParams.height = dp(size);

        mLayoutParams.format = PixelFormat.RGBA_8888;
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_FULLSCREEN;
        return this;
    }

    private void move(float x, float y) {
        mLayoutParams.x += (int) x;
        mLayoutParams.y += (int) y;
        mWindowManager.updateViewLayout(mContainer, mLayoutParams);
    }

    private void reset() {
        int sizeTop = mLayoutParams.y + dp(size) / 2;
        int sizeLeft = mLayoutParams.x + dp(size) / 2;
        int sizeRight = windowWidth() - sizeLeft;
        int sizeBottom = windowHeight() - sizeTop;

        final int type = min(sizeTop, sizeLeft, sizeRight, sizeBottom);
        float start = (type == TOP || type == BOTTOM) ? mLayoutParams.y : mLayoutParams.x;
        float end = type == LEFT ? 0 : windowWidth() - dp(size);
        ValueAnimator animator = ValueAnimator.ofFloat(start, end);
        animator.setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float pos = (float) animation.getAnimatedValue();
                float x = (type == TOP || type == BOTTOM) ? 0 : (pos - mLayoutParams.x);
                float y = (type == TOP || type == BOTTOM) ? (pos - mLayoutParams.y) : 0;
                move(x, y);
            }
        });
        animator.addListener(new AnimationEndListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                endAnimation();
                savePosition(type);
            }

        });
        animator.start();
    }

    private int min(int top, int left, int right, int bottom) {
        int min = Math.min(right, left);

        if (min == right) {
            return RIGHT;
        } else {
            return LEFT;
        }
    }

    private void readPosition() {
        xPercent = Prefs.with(Global.getApplication()).readFloat("floater_x", xPercent);
        yPercent = Prefs.with(Global.getApplication()).readFloat("floater_y", yPercent);
    }

    private void savePosition(int type) {
        if (type == RIGHT) {
            xPercent = 1.0f;
        } else {
            xPercent = 0.0f;
        }
        yPercent = (mLayoutParams.y + dp(size) / 2f) / (float) windowHeight();
        Prefs.with(Global.getApplication())
                .writeFloat("floater_x", xPercent)
                .writeFloat("floater_y", yPercent);

    }

    private void startAnimation() {
        try {
            mHandler.removeCallbacks(mRunnable);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        mContainer.setAlpha(1.0f);
        mContainer.getChildAt(0).setTranslationX(0);
        mContainer.getChildAt(0).invalidate();
    }

    private void endAnimation() {
        mHandler.postDelayed(mRunnable, 3000);
    }

    private void showMenu() {
        mContainer.setVisibility(View.INVISIBLE);
        boolean isRight = false;
        if (mLayoutParams.x > windowWidth() / 2) {
            isRight = true;
        }
        final boolean isRightLayout = isRight;
        Context context = mContainer.getContext();

        float length = menus.size() + 1 + 0.5f;
        int w = (int) (length * dp(size));
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(0x95000000);
        drawable.setCornerRadius(dp(size) / 2);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_FULLSCREEN;
        layoutParams.x = 0;
        layoutParams.y = 0;
        layoutParams.width = windowWidth();
        layoutParams.height = windowHeight();

        final LinearLayout linearLayout = new LinearLayout(context);
        mWindowManager.addView(linearLayout, layoutParams);

        final LinearLayout container = new LinearLayout(context);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(w, dp(size));
        llp.topMargin = mLayoutParams.y;

        if (isRight) {
            View view = new View(context);
            container.addView(view, dp(size) / 2, dp(size));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            llp.leftMargin = windowWidth() - w;
        } else {
            container.addView(new FtCloseView(context), dp(size), dp(size));
        }

        for (int i = 0; i < ((int) length - 1); i++) {
            FtMenuItem menuItem = new FtMenuItem(context);
            container.addView(menuItem, dp(size), dp(size));
            applyMenuClickDrawable(menuItem);
            menuItem.setMenuItem(
                    menus.get(i).mText,
                    menus.get(i).mDrawable,
                    menus.get(i).mTextSize,
                    menus.get(i).mIconSize
            );
            final int index = i;
            menuItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideMenu(linearLayout, isRightLayout);
                    if (menus.get(index).mListener != null){
                        menus.get(index).mListener.onClick(v);
                    }
                }
            });
        }

        if (isRight) {
            container.addView(new FtCloseView(context), dp(size), dp(size));
        } else {
            View view = new View(context);
            container.addView(view, dp(size) / 2, dp(size));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        linearLayout.addView(container, llp);
        container.setBackgroundDrawable(drawable);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideMenu(linearLayout, isRightLayout);
            }
        });

        ScaleAnimation animation = new ScaleAnimation((float) dp(size) / (float) w, 1, 1, 1, Animation.RELATIVE_TO_SELF, isRight ? 1f : 0f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(300);
        container.startAnimation(animation);
    }

    private void applyMenuClickDrawable(View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            StateListDrawable drawable = new StateListDrawable();
            drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(0x35ffffff));
            drawable.addState(new int[]{}, new ColorDrawable(Color.TRANSPARENT));
            view.setBackgroundDrawable(drawable);
        } else {
            int[][] stateList = new int[][]{
                    new int[]{android.R.attr.state_pressed},
                    new int[]{android.R.attr.state_focused},
                    new int[]{android.R.attr.state_activated},
                    new int[]{}
            };

            int normalColor = Color.parseColor("#00000000");
            int pressedColor = Color.parseColor("#35ffffff");
            int[] stateColorList = new int[]{
                    pressedColor,
                    pressedColor,
                    pressedColor,
                    normalColor
            };
            ColorStateList colorStateList = new ColorStateList(stateList, stateColorList);

            final RippleDrawable rippleDrawable = new RippleDrawable(colorStateList, null, null);
            view.setBackgroundDrawable(rippleDrawable);
        }
    }

    private void hideMenu(final LinearLayout linearLayout, boolean isRight) {
        ScaleAnimation animation = new ScaleAnimation(1, 0, 1, 1, Animation.RELATIVE_TO_SELF, isRight ? 1f : 0f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(300);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                try {
                    mWindowManager.removeViewImmediate(linearLayout);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
                try {
                    mContainer.setVisibility(View.VISIBLE);
                    endAnimation();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        linearLayout.getChildAt(0).startAnimation(animation);
    }

    public static class Builder {
        float xPercent = 0.0f;
        float yPercent = 0.5f;
        int size = 40;
        boolean storagePosition = true;
        List<MenuItem> menus = new ArrayList<>();

        public Builder() {

        }

        public Builder setXPosition(@FloatRange(from = 0.0f, to = 1.0f) float percent) {
            xPercent = percent;
            return this;
        }

        public Builder setYPosition(@FloatRange(from = 0.0f, to = 1.0f) float percent) {
            yPercent = percent;
            return this;
        }

        public Builder setSize(int size) {
            this.size = size;
            return this;
        }

        public Builder storagePosition(boolean storagePosition) {
            this.storagePosition = storagePosition;
            return this;
        }

        public Builder addMenu(MenuItem menuItem) {
            menus.add(menuItem);
            return this;
        }

        public FtWindowManager create(Activity activity) {
            return new FtWindowManager(activity)
                    .setXPosition(xPercent)
                    .setYPosition(yPercent)
                    .setSize(size)
                    .storagePosition(storagePosition)
                    .menu(menus)
                    .build();
        }

    }

    public static class MenuItem {
        Drawable mDrawable;
        String mText;
        View.OnClickListener mListener;
        private int mIconSize = 18;
        private int mTextSize = 12;

        public MenuItem() {

        }


        public MenuItem setDrawable(Drawable drawable) {
            mDrawable = drawable;
            return this;
        }

        public MenuItem setText(String text) {
            mText = text;
            return this;
        }

        public MenuItem setIconSize(int iconSize){
            mIconSize = iconSize;
            return this;
        }

        public MenuItem setTextSize(int textSize){
            mTextSize = textSize;
            return this;
        }

        public MenuItem setOnClickListener(View.OnClickListener listener) {
            mListener = listener;
            return this;
        }
    }

    public abstract class MoveListener implements View.OnTouchListener {
        int mSlop;
        boolean mOnTouch;
        float downX;
        float downY;
        float lastX;
        float lastY;

        float changeX;
        float changeY;

        float upX;
        float upY;
        boolean mPtInRect = false;
        boolean mPtActionDown = false;

        protected abstract void onActionDown(float x, float y, View v, MotionEvent event);

        protected abstract void onActionMove(float x, float y, View v, MotionEvent event);

        protected abstract void onActionUp(float x, float y, View v, MotionEvent event);

        protected abstract void onClick(View v, MotionEvent event);

        public MoveListener() {
            super();
            mSlop = ViewConfiguration.get(Global.getApplication()).getScaledTouchSlop();
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mPtInRect = ptInRect(v, event);
            if (!mPtInRect) {
                return true;
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mOnTouch = false;

                    downX = event.getRawX();
                    downY = event.getRawY();
                    lastX = event.getRawX();
                    lastY = event.getRawY();

                    onActionDown(event.getRawX(), event.getRawY(), v, event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    changeX = event.getRawX() - lastX;
                    changeY = event.getRawY() - lastY;
                    lastX = event.getRawX();
                    lastY = event.getRawY();

                    onActionMove(changeX, changeY, v, event);
                    break;
                case MotionEvent.ACTION_UP:
                    upX = event.getRawX();
                    upY = event.getRawY();
                    mOnTouch = (Math.abs(upX - downX) > mSlop) || (Math.abs(upY - downY) > mSlop);

                    if (mOnTouch) {
                        onActionUp(event.getRawX(), event.getRawY(), v, event);
                    } else {
                        onClick(v, event);
                    }
                    break;
            }
            return mOnTouch;
        }

        private boolean ptInRect(View v, MotionEvent event) {
            View child = ((ViewGroup) v).getChildAt(0);

            if (mLayoutParams.x < 0) {
                mLayoutParams.x = 0;
            }
            if (mLayoutParams.x > windowWidth() - dp(size)) {
                mLayoutParams.x = windowWidth() - dp(size);
            }
            if (mLayoutParams.y < 0) {
                mLayoutParams.y = 0;
            }
            if (mLayoutParams.y > windowHeight() - dp(size)) {
                mLayoutParams.y = windowHeight() - dp(size);
            }

            RectF rect = new RectF(
                    (mLayoutParams.x + child.getTranslationX()),
                    mLayoutParams.y,
                    (mLayoutParams.x + child.getTranslationX()) + dp(size) / 2,
                    mLayoutParams.y + dp(size) / 2
            );


            int x = (int) (mLayoutParams.x + child.getTranslationX()) + dp(size) / 2;
            int y = mLayoutParams.y + dp(size) / 2;

            float x1 = event.getRawX();
            float y1 = event.getRawY();

            float x2 = Math.abs(x - x1);
            float y2 = Math.abs(y - y1);

            float r = (float) Math.sqrt(x2 * x2 + y2 * y2);
            int s = dp(size) / 2;

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (r < s) {
                        mPtActionDown = true;
                        mPtInRect = true;
                    } else {
                        mPtActionDown = false;
                        mPtInRect = false;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return mPtInRect;
        }
    }

    public abstract class AnimationEndListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }
}
