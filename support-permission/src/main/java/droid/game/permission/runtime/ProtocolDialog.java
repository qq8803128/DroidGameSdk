package droid.game.permission.runtime;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.*;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import droid.game.common.dialog.UIDialog;
import droid.game.common.dialog.UIDialogBuilder;
import droid.game.common.dialog.widget.UIDialogView;
import droid.game.common.util.UIDisplayHelper;
import droid.game.common.util.UIDrawableHelper;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static droid.game.common.util.UIDisplayHelper.dpToPx;

public class ProtocolDialog extends UIDialogBuilder<ProtocolDialog> {
    private String  mUrl;

    public ProtocolDialog(Context context, String url) {
        super(context);
        mUrl = url;
        setLayout(calcWidth(0.85f), calcWidth(0.85f), 0, 0);
    }

    @Override
    public FrameLayout.LayoutParams onCreateDialogViewLayoutParam() {
        return new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, calcWidth(0.85f * 0.90f));
    }

    @Override
    public void configDialogView(UIDialogView dialogView) {
        super.configDialogView(dialogView);
        dialogView.setPadding(0, 0, 0, 0);
        dialogView.setBackgroundDrawable(
                UIDrawableHelper.createGradientDrawable()
                        .applyRadius(dpToPx(0))
                        .applyColor(Color.parseColor("#ffffff"))
        );
    }

    @Override
    public View onCreateContentView(UIDialog dialog, UIDialogView dialogView, Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout linearLayout1 = new LinearLayout(context);
        linearLayout.addView(linearLayout1,MATCH_PARENT,dpToPx(48));
        TextView textView = new TextView(context);
        textView.setTextSize(18);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);
        textView.setPadding(dpToPx(48),0,0,0);
        textView.setText("用户隐私保护协议");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,dpToPx(48));
        lp.weight = 1;
        linearLayout1.addView(textView, lp);
        linearLayout1.setBackgroundDrawable(
                UIDrawableHelper.createGradientDrawable().applyColor(Color.parseColor("#5198CE"))
        );

        linearLayout1.addView(new CloseView(context),dpToPx(48),dpToPx(48));

        linearLayout1.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        final WebView webView = new WebView(context);
        linearLayout.addView(webView, MATCH_PARENT, MATCH_PARENT);
        setupWebview(webView);

        dialog.addOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                try{
                    webView.destroy();
                }catch (Throwable e){
                    e.printStackTrace();
                }
            }
        });

        return linearLayout;
    }

    private void setupWebview(WebView mWebView){
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        mWebView.getSettings().setSupportMultipleWindows(false);
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setJavaScriptEnabled(true);// 设置使用够执行JS脚本
        mWebView.getSettings().setDomStorageEnabled(true);

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http://") || url.startsWith("https://")){
                    view.loadUrl(url);
                }else{
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        getBaseContext().startActivity(intent);
                    }catch (Throwable e){
                        e.printStackTrace();
                    }
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient(){

        });

        mWebView.loadUrl(mUrl);
    }

    private int calcWidth(float percentWidth) {
        int w = UIDisplayHelper.getScreenWidth(getBaseContext());
        int h = UIDisplayHelper.getScreenHeight(getBaseContext());
        int width = (int) (w * percentWidth);
        if (w > h) {
            width = (int) (h * percentWidth);
        }
        return width;
    }

    private static class CloseView extends View{
        private Paint mPaint;
        private int mColor = Color.parseColor("#ffffff");
        public CloseView(Context context) {
            super(context);
        }

        public CloseView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public CloseView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (mPaint == null) {
                mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(UIDisplayHelper.dpToPx(1.5f));
                mPaint.setColor(mColor);
            }

            int w = canvas.getWidth();
            int h = canvas.getHeight();
            Path path;
            int s;

            s = canvas.getWidth() / 4 - UIDisplayHelper.dpToPx(5);

            path = new Path();
            path.moveTo(w / 2 - s,h / 2 - s);
            path.lineTo(w / 2 + s, h / 2 + s);
            canvas.drawPath(path,mPaint);

            path = new Path();
            path.moveTo(w / 2 + s,h / 2 - s);
            path.lineTo(w / 2 - s, h / 2 + s);
            canvas.drawPath(path,mPaint);
        }
    }
}
