package droid.game.core.bridge.splash;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import droid.game.common.dialog.UIDialog;
import droid.game.common.dialog.UIDialogBuilder;
import droid.game.common.dialog.widget.UIDialogView;
import droid.game.common.span.Span;
import droid.game.common.toast.ToastEx;
import droid.game.common.util.UIDisplayHelper;
import droid.game.common.widget.ReboundScrollView;
import droid.game.common.widget.UISpanTouchFixTextView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static droid.game.common.util.UIDisplayHelper.dpToPx;

public class UpdateDialog extends UIDialogBuilder<UpdateDialog> {
    private int mHeight = 0;
    private Info mInfo;
    public UpdateDialog(Context context) {
        super(context);
        onCreate(this);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    public UpdateDialog setUpdate(Info info){
        mInfo = info;
        return this;
    }

    @Override
    public void configDialogView(UIDialogView dialogView) {
        super.configDialogView(dialogView);
        dialogView.setBackgroundColor(Color.parseColor("#262c3b"));
        dialogView.setPadding(0, 0, 0, 0);
    }

    @Override
    public View onCreateContentView(UIDialog dialog, UIDialogView dialogView, Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        createTitleLayout(context,linearLayout);
        createContentLayout(context,linearLayout);
        createProgressLayout(context,linearLayout);
        createOperatorLayout(context,linearLayout);
        return linearLayout;
    }

    @Override
    public LinearLayout.LayoutParams onCreateContentViewParams(Context context) {
        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mHeight);
    }

    private void onCreate(UIDialogBuilder builder) {
        builder.setCancelable(true);
        builder.setCanceledOnTouchOutside(false);

        float percent = 0.89f;
        int w = UIDisplayHelper.getScreenWidth(builder.getBaseContext());
        int h = UIDisplayHelper.getScreenHeight(builder.getBaseContext());
        int width = (int) (percent * w);
        if (w > h) {
            width = (int) (h * percent);
        }
        mHeight = (int) (width * 0.85f);
        builder.setLayout(width, width, dpToPx(20), 0);

    }

    private void createTitleLayout(Context context, LinearLayout linearLayout) {
        LinearLayout top = new LinearLayout(context);
        linearLayout.addView(top, ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(40));

        LinearLayout.LayoutParams lp = null;

        ImageView imageView1 = new UpdateImageView(context);
        lp = new LinearLayout.LayoutParams(dpToPx(16), dpToPx(16));
        lp.leftMargin = dpToPx(12);
        lp.rightMargin = dpToPx(4);
        lp.topMargin = dpToPx(14f);
        lp.bottomMargin = 0;
        top.addView(imageView1, lp);
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setTextSize(16f);
        textView.setTextColor(Color.WHITE);
        //textView.getPaint().setFakeBoldText(true);
        textView.setPadding(dpToPx(0), dpToPx(2), 0, 0);
        textView.setText("在线更新");
        top.addView(textView, WRAP_CONTENT, MATCH_PARENT);

        lp = new LinearLayout.LayoutParams(0, 0);
        lp.weight = 1;
        top.addView(new View(context), lp);

        LinearLayout close = new LinearLayout(context);
        ImageView imageView = new CloseImageView(context);
        top.addView(close, dpToPx(40), dpToPx(40));
        close.addView(imageView, MATCH_PARENT, MATCH_PARENT);
        imageView.setClickable(false);
        close.setClickable(true);

        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(Color.parseColor("#E31B5D")));
        close.setBackgroundDrawable(drawable);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getDialog().dismiss();
            }
        });

        if (mInfo.isForce){
            close.setVisibility(View.INVISIBLE);
        }

    }

    private void createContentLayout(Context context, LinearLayout linearLayout) {
        LinearLayout.LayoutParams lp = null;
        ReboundScrollView scrollView = new ReboundScrollView(context);
        scrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        lp = new LinearLayout.LayoutParams(MATCH_PARENT, 0);
        lp.weight = 1;
        lp.leftMargin = dpToPx(16);
        lp.rightMargin = dpToPx(16);
        lp.topMargin = dpToPx(4);
        linearLayout.addView(scrollView, lp);
        scrollView.setBackgroundColor(Color.parseColor("#0C101B"));
        scrollView.setFillViewport(true);

        LinearLayout scrollLayout = new LinearLayout(context);
        scrollLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(scrollLayout, MATCH_PARENT, MATCH_PARENT);

        TextView textView3 = new TextView(context);
        textView3.setTextSize(16);
        textView3.setTextColor(Color.WHITE);
        textView3.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));
        textView3.setText(mInfo.versionName + "版本更新说明");
        scrollLayout.addView(textView3, MATCH_PARENT, WRAP_CONTENT);

        UISpanTouchFixTextView textView4 = new UISpanTouchFixTextView(context);
        textView4.setTextSize(14);
        textView4.setTextColor(Color.parseColor("#a5a5a5"));
        textView4.setPadding(dpToPx(16), dpToPx(0), dpToPx(16), dpToPx(16));
        scrollLayout.addView(textView4, MATCH_PARENT, WRAP_CONTENT);

        Span span = new Span(
                /*"1.极大的优化系统内存占用，机器性能越好，优化越明显；\n" +
                        "2.键位操控方案支持快捷键切换，使用更方便快捷；\n" +
                        "3.手柄支持旋转视角功能，更加游戏手柄体验，例如Brawl Starts；\n" +*/
                mInfo.updateContent +
                        "#去浏览器下载最新版#"
        ).add()
                .lightText("#去浏览器下载最新版#")
                .textColor(0xff1D87FF, 0xff1D87FF)
                .backgroundColor(Color.TRANSPARENT, 0x35ffffff)
                .listener(new Span.OnClickSpanListener() {
                    @Override
                    public void onClick(View v, String lightText) {
                        ToastEx.longShow("去浏览器下载最新版");
                    }
                });

        textView4.setMovementMethodDefault();
        textView4.setText(span.build());

    }

    private void createProgressLayout(Context context, LinearLayout linearLayout) {
        LinearLayout.LayoutParams lp = null;
        TextView textView1 = new TextView(context);
        textView1.setTextColor(0xffa1a1a1);
        textView1.setPadding(dpToPx(16), dpToPx(8), 0, 0);
        textView1.setText("等待更新");
        textView1.setTextSize(14);
        linearLayout.addView(textView1);

        ProgressBar progressBar = new ProgressBar(context,null,android.R.style.Widget_DeviceDefault_ProgressBar_Horizontal);
        lp = new LinearLayout.LayoutParams(MATCH_PARENT, dpToPx(3));
        lp.leftMargin = dpToPx(16);
        lp.rightMargin = dpToPx(16);
        lp.topMargin = dpToPx(6);
        lp.bottomMargin = dpToPx(6);
        linearLayout.addView(progressBar, lp);
        progressBar.setBackgroundColor(Color.parseColor("#000000"));
    }

    private void createOperatorLayout(Context context, LinearLayout linearLayout) {
        LinearLayout.LayoutParams lp = null;
        LinearLayout bottom = new LinearLayout(context);
        lp = new LinearLayout.LayoutParams(MATCH_PARENT, dpToPx(40));
        lp.leftMargin = dpToPx(16);
        lp.rightMargin = dpToPx(16);
        lp.topMargin = dpToPx(6);
        lp.bottomMargin = dpToPx(16);
        linearLayout.addView(bottom, MATCH_PARENT, lp);

        bottom.setGravity(Gravity.CENTER_VERTICAL);

        TextView textView2 = new TextView(context);
        textView2.setTextColor(Color.parseColor("#1D87FF"));
        textView2.setText("忽略此版本");
        textView2.setTextSize(16);
        textView2.setGravity(Gravity.CENTER);
        configTextColor(textView2,0xff195AA7,0xff1D87FF);
        if (mInfo.isForce || !mInfo.isIgnorable){
            textView2.setVisibility(View.GONE);
        }

        bottom.addView(textView2);

        lp = new LinearLayout.LayoutParams(0, MATCH_PARENT);
        lp.weight = 1;
        bottom.addView(new View(context), lp);

        TextView textView = new TextView(context);
        textView.setTextColor(0xffa1a1a1);
        textView.setPadding(dpToPx(12),0,dpToPx(12),0);
        textView.setTextSize(16);
        textView.setText("稍后提醒");
        configBackgroundColor(textView,Color.TRANSPARENT,0x35ffffff,1,0xffa1a1a1,dpToPx(1));

        bottom.addView(textView,WRAP_CONTENT,MATCH_PARENT);

        if (mInfo.isForce){
            bottom.setVisibility(View.GONE);
        }

        bottom.addView(new View(context),dpToPx(8),0);

        TextView textView1 = new TextView(context);
        textView1.setTextColor(0xffffffff);
        textView1.setPadding(dpToPx(12),0,dpToPx(12),0);
        textView1.setTextSize(16);
        textView1.setText("立即更新");
        configBackgroundColor(textView1,0xff195AA7,0xff1D87FF,0,0xffa1a1a1,dpToPx(1));

        bottom.addView(textView1,WRAP_CONTENT,MATCH_PARENT);

    }

    private void configTextColor(TextView textView,int normalColor,int pressColor){
        textView.setClickable(true);
        int[][] states = new int[2][];
        states[0] = new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled };
        states[1] = new int[] {};

        ColorStateList colorStateList = new ColorStateList(states,new int[]{normalColor,pressColor});
        textView.setTextColor(colorStateList);
    }

    private void configBackgroundColor(TextView textView,int normalColor,int pressColor,int stroke,int strokeColor,int radius){
        StateListDrawable drawable = new StateListDrawable();
        GradientDrawable gradientDrawable1 = new GradientDrawable();
        gradientDrawable1.setCornerRadius(radius);
        gradientDrawable1.setColor(normalColor);
        gradientDrawable1.setStroke(stroke,strokeColor);

        GradientDrawable gradientDrawable2 = new GradientDrawable();
        gradientDrawable2.setCornerRadius(radius);
        gradientDrawable2.setColor(pressColor);
        gradientDrawable2.setStroke(stroke,strokeColor);

        drawable.addState(new int[]{android.R.attr.state_pressed},gradientDrawable2);
        drawable.addState(new int[]{},gradientDrawable1);
        textView.setBackgroundDrawable(drawable);
        textView.setClickable(true);
        textView.setGravity(Gravity.CENTER);

    }

    @SuppressLint("AppCompatCustomView")
    private static class CloseImageView extends ImageView {
        private Paint mPaint;
        private int mColor = Color.parseColor("#ffffff");

        public CloseImageView(Context context) {
            this(context, null);
        }

        public CloseImageView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public CloseImageView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (mPaint == null) {
                mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(UIDisplayHelper.dpToPx(1f));
                mPaint.setColor(mColor);
            }

            drawSymble(canvas);
        }

        private void drawSymble(Canvas canvas) {
            int w = canvas.getWidth();
            int h = canvas.getHeight();
            Path path;
            int s;

            s = canvas.getWidth() / 4 - UIDisplayHelper.dpToPx(4);

            path = new Path();
            path.moveTo(w / 2 - s, h / 2 - s);
            path.lineTo(w / 2 + s, h / 2 + s);
            canvas.drawPath(path, mPaint);

            path = new Path();
            path.moveTo(w / 2 + s, h / 2 - s);
            path.lineTo(w / 2 - s, h / 2 + s);
            canvas.drawPath(path, mPaint);
        }
    }

    @SuppressLint("AppCompatCustomView")
    private static class UpdateImageView extends ImageView {
        private Paint mPaint;
        private int mColor = Color.parseColor("#ffffff");

        public UpdateImageView(Context context) {
            super(context);
        }

        public UpdateImageView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public UpdateImageView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (mPaint == null) {
                mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(UIDisplayHelper.dpToPx(1f));
                mPaint.setColor(mColor);
            }

            drawSymble(canvas);
        }

        private void drawSymble(Canvas canvas) {
            int w = canvas.getWidth();
            int h = canvas.getHeight();
            Path path;
            int s;
            int r = w / 2;

            path = new Path();
            path.moveTo(0, r);
            path.lineTo(0, h);
            path.lineTo(w, h);
            path.lineTo(w, r);
            canvas.drawPath(path, mPaint);

            path = new Path();
            path.moveTo(w / 2, h - dpToPx(4f));
            path.lineTo(w / 2, 0);
            canvas.drawPath(path, mPaint);

            path = new Path();
            path.moveTo(w / 4, h / 4);
            path.lineTo(w / 2, 0);
            path.lineTo(w / 4 * 3, h / 4);
            canvas.drawPath(path, mPaint);
        }
    }

    public static class Info{
        // 是否有新版本
        public boolean hasUpdate = false;
        // 是否静默下载：有新版本时不提示直接下载
        public boolean isSilent = false;
        // 是否强制安装：不安装无法使用app
        public boolean isForce = false;
        // 是否下载完成后自动安装
        public boolean isAutoInstall = true;
        // 是否可忽略该版本
        public boolean isIgnorable = true;

        public int versionCode;
        public String versionName;
        public String updateContent;

        public String url;
        public String md5;
        public long size;
    }
}
