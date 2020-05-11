package droid.game.common.suspen;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import droid.game.common.util.Utils;
import droid.game.common.widget.MarqueeTextView;

import static droid.game.common.util.Utils.*;

public class Suspen extends LinearLayout {
    public static final Theme Light = SuspenLight.theme();
    public static final Theme Night = SuspenNight.theme();

    private static Theme sTheme = Light;
    private Theme mTheme = sTheme;
    private ImageView mImageView;
    private TextView mTitleView,mContentView,mPositiveButton;
    private Drawable mIcon;
    private String mTitle = "溫馨提示";
    private String mContent = "您收到了一条新的通知,点击查看";
    private OnSuspenClickListener mOnClickListener;
    private String mPositive = "确认";

    public static Suspen create(Activity activity) {
        return new Suspen(activity);
    }

    public static void initTheme(Theme theme){
        if (theme != null) {
            sTheme = theme;
        }
    }

    private WindowManager wm;
    private WindowManager.LayoutParams lp;
    private IViewCreator mViewCreator;

    Suspen(Context context) {
        this(context, null);
    }

    Suspen(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    Suspen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        lp = new WindowManager.LayoutParams();
        setFormat(PixelFormat.RGBA_8888);
        setPosition(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        setType(WindowManager.LayoutParams.TYPE_APPLICATION);
        setFlag(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        | WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        int w = (int) (windowWidth());
        if (!portrait()) {
            w = (int) (windowHeight());
        }
        w -= dp(32);

        int h = dp(76);
        setLayout(w, h);
        setPoint(0, dp(16));
    }

    public Suspen setTheme(Theme theme) {
        mTheme = theme;
        return this;
    }

    public Theme getTheme() {
        return mTheme;
    }

    public final View createDefaultView(Context ctx) {
        int w = (int) (windowWidth());
        if (!portrait()) {
            w = (int) (windowHeight());
        }
        w -= dp(32);
        int h = dp(76);

        setGravity(Gravity.CENTER);
        LinearLayout linearLayout = new LinearLayout(ctx);
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);
        linearLayout.setOrientation(HORIZONTAL);
        linearLayout.setPadding(dp(12), dp(12), dp(12), dp(12));
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(w - dp(4), h - dp(4)));

        ImageView imageView = new ImageView(ctx);
        linearLayout.addView(imageView,dp(48),dp(48));
        imageView.setImageDrawable(getAppIcon(ctx));

        LinearLayout linearLayout1 = new LinearLayout(ctx);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(0,dp(48));
        llp.weight = 1;
        llp.leftMargin = dp(6);
        linearLayout.addView(linearLayout1,llp);

        TextView textView = new TextView(ctx);
        textView.setPadding(dp(8),0,dp(8),0);
        textView.setText("确认");
        llp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp(28));
        llp.leftMargin = dp(6);
        linearLayout.addView(textView,llp);


        getTheme().setContainerStyle(linearLayout);
        getTheme().setButtonViewStyle(null,null,textView,null);

        TextView textView1 = new TextView(ctx);
        linearLayout1.setOrientation(VERTICAL);

        linearLayout1.addView(textView1);
        getTheme().setTitleViewStyle(textView1);

        TextView textView2 = new MarqueeTextView(ctx);
        linearLayout1.addView(textView2);

        getTheme().setContentViewStyle(textView2);

        //textView2.setMovementMethod(LinkMovementMethod.getInstance());
        textView2.setText(Utils.fromHtml("欢迎新玩家加入，入群建议先看群公告。所有版本下载登录器就可以玩，不需要客户端。主页地址<a href=\"https://www.ajjie.cn\">https://www.ajjie.cn</a>"));

        textView1.setText(getAppName(ctx));

        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setClickable(false);
                hide();
            }
        });

        mImageView = imageView;
        mTitleView = textView1;
        mContentView = textView2;
        mPositiveButton = textView;

        return linearLayout;
    }

    private Drawable getAppIcon(Context context){
        try{
            PackageManager packageManager = context.getApplicationContext().getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            return packageManager.getApplicationIcon(applicationInfo);
        }catch (Throwable e){

        }
        return null;
    }

    private String getAppName(Context context){
        try{
            PackageManager packageManager = context.getApplicationContext().getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            return context.getResources().getString(applicationInfo.labelRes);
        }catch (Throwable e){

        }
        return "";
    }

    public Suspen setFormat(int format) {
        lp.format = format;
        return this;
    }

    public Suspen setPosition(int gravity) {
        lp.gravity = gravity;
        return this;
    }

    public Suspen setType(int type) {
        lp.type = type;
        return this;
    }

    public Suspen setFlag(int flag) {
        lp.flags = flag;
        return this;
    }

    public Suspen setLayout(int w, int h) {
        lp.width = w;
        lp.height = h;
        return this;
    }

    public Suspen setPoint(int x, int y) {
        lp.x = x;
        lp.y = y;
        return this;
    }

    public Suspen setView(IViewCreator viewCreator) {
        mViewCreator = viewCreator;
        return this;
    }

    public Suspen setIcon(Drawable icon){
        mIcon = icon;
        return this;
    }

    public Suspen setTitle(String title){
        mTitle = title;
        return this;
    }

    public Suspen setContent(String content){
        mContent = content;
        return this;
    }

    public Suspen setPositiveButton(String text,OnSuspenClickListener onClickListener){
        mPositive = text;
        mOnClickListener = onClickListener;
        return this;
    }

    public WindowManager.LayoutParams getWindowLayoutParam() {
        return lp;
    }

    public Suspen show() {
        try {
            if (mViewCreator == null) {
                mViewCreator = new IViewCreator() {
                    @Override
                    public View createView(Suspen toast) {
                        View rootView = (ViewGroup) createDefaultView(toast.getContext());
                        rootView.setTranslationY(-dp(76));
                        setClickable(false);
                        if (mImageView != null){
                            try {
                                if (mIcon != null) {
                                    mImageView.setImageDrawable(mIcon);
                                }
                            }catch (Throwable e){

                            }
                            try{
                                mTitleView.setText(mTitle);
                                mContentView.setText(mContent);
                                mPositiveButton.setText(mPositive);
                                if (mOnClickListener != null){
                                    mPositiveButton.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mOnClickListener.onClick(Suspen.this);
                                        }
                                    });
                                }
                            }catch (Throwable e){

                            }
                        }
                        rootView.post(new Runnable() {
                            @Override
                            public void run() {
                                ValueAnimator valueAnimator = ValueAnimator.ofFloat(-dp(76), 0);
                                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                        getChildAt(0).setTranslationY((float) valueAnimator.getAnimatedValue());
                                    }
                                });
                                valueAnimator.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animator) {
                                        setClickable(true);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animator) {

                                    }
                                });
                                valueAnimator.start();
                            }
                        });
                        return rootView;
                    }
                };
            }
            if (getChildCount() == 0) {
                int w = (int) (windowWidth());
                if (!portrait()) {
                    w = (int) (windowHeight());
                }
                w -= dp(32);

                int h = dp(76);
                addView(mViewCreator.createView(this), new LinearLayout.LayoutParams(w, h));
            }
            wm.addView(this, lp);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return this;
    }

    public Suspen hide() {
        try {
            setClickable(false);
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, -dp(76));
            valueAnimator.setDuration(300);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    getChildAt(0).setTranslationY((float) valueAnimator.getAnimatedValue());
                }
            });
            valueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    try {
                        wm.removeViewImmediate(Suspen.this);
                    } catch (Throwable e1) {
                        e1.printStackTrace();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            valueAnimator.start();
        } catch (Throwable e) {
            e.printStackTrace();
            try {
                wm.removeViewImmediate(this);
            } catch (Throwable e1) {
                e1.printStackTrace();
            }
        }
        return this;
    }

    public interface OnSuspenClickListener{
        void onClick(Suspen suspen);
    }
    public interface IViewCreator {
        View createView(Suspen toast);
    }

}
