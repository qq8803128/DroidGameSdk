package droid.game.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import droid.game.common.dialog.widget.UIDialogRootLayout;
import droid.game.common.link.UILinkTouchMovementMethod;
import droid.game.common.util.UIDisplayHelper;
import droid.game.common.dialog.widget.UIDialogView;
import droid.game.common.util.UIDrawableHelper;
import droid.game.common.widget.UILoadView;
import droid.game.common.widget.UISpanTouchFixTextView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static droid.game.common.util.UIDisplayHelper.dpToPx;

public class IOSDialog extends UIDialog {
    private static final int DEF_IOS_DIALOG_BACKGROUND_COLOR = Color.WHITE;
    private static final int DEF_IOS_DIALOG_BACKGROUND_RADIUS = dpToPx(12);

    public IOSDialog(Context context) {
        super(context);
    }

    public IOSDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private static final class IOSDialogBuilderHelper {
        static void onConstructors(Context context, UIDialogBuilder builder) {
            onConstructors(context, builder, 0.72f);
        }

        static void onConstructors(Context context, UIDialogBuilder builder, float percent) {
            int w = UIDisplayHelper.getScreenWidth(context);
            int h = UIDisplayHelper.getScreenHeight(context);
            int width = (int) (percent * w);
            if (w > h) {
                width = (int) (h * percent);
            }
            builder.setLayout(width, width, dpToPx(20), dpToPx(20));
        }

        static void configDialogView(UIDialogView dialogView) {
            dialogView.setBackgroundDrawable(
                    UIDrawableHelper.createGradientDrawable()
                            .applyRadius(DEF_IOS_DIALOG_BACKGROUND_RADIUS)
                            .applyColor(DEF_IOS_DIALOG_BACKGROUND_COLOR)
            );
        }
    }

    public static final class Message extends UIDialogBuilder<Message> {
        private CharSequence mContent = "";

        public Message(Context context) {
            super(context);
            IOSDialogBuilderHelper.onConstructors(context, this);
        }

        public Message setContent(CharSequence content) {
            mContent = content;
            return this;
        }

        @Override
        public void configTitleView(View titleView) {
            super.configTitleView(titleView);
            if (hasContent()) {
                titleView.setPadding(
                        titleView.getPaddingLeft(),
                        titleView.getPaddingTop(),
                        titleView.getPaddingRight(),
                        dpToPx(2)
                );
            } else {
                titleView.setPadding(
                        titleView.getPaddingLeft(),
                        titleView.getPaddingTop(),
                        titleView.getPaddingRight(),
                        dpToPx(22)
                );
            }
        }

        @Override
        public View onCreateContentView(UIDialog dialog, UIDialogView dialogView, Context context) {
            UISpanTouchFixTextView textView = new UISpanTouchFixTextView(context);
            textView.setNeedForceEventToParent(true);
            textView.setMovementMethodDefault();
            int topPadding = hasTitle() ? dpToPx(6) : dpToPx(24);
            textView.setPadding(dpToPx(16), topPadding, dpToPx(16), dpToPx(16));
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(15);
            textView.setTextColor(Color.parseColor("#4f4f4f"));

            if (!hasContent()) {
                textView.setVisibility(View.GONE);
            } else {
                textView.setVisibility(View.VISIBLE);
                textView.setText(mContent);
            }

            return wrappContentViewScroll(textView);
        }

        protected View wrappContentViewScroll(View view) {
            return wrapWithScroll(view);
        }

        @Override
        public void configDialogView(UIDialogView dialogView) {
            super.configDialogView(dialogView);
            IOSDialogBuilderHelper.configDialogView(dialogView);
        }

        protected boolean hasContent() {
            return !textEmpty(mContent);
        }
    }

    public static final class Input extends UIDialogBuilder<Input> {
        private CharSequence mContent = "";
        private OnCreateEditTextListener mListener;
        private boolean mShowSoftInputDialog = false;
        private EditText mEditText;

        public Input(Context context) {
            super(context);

            IOSDialogBuilderHelper.onConstructors(context, this);

            setCanceledOnTouchOutside(false);
        }

        public Input setContent(CharSequence content) {
            mContent = content;
            return this;
        }

        public Input setOnCreateEditTextListener(OnCreateEditTextListener listener) {
            mListener = listener;
            return this;
        }

        public Input setShowSoftInputDialog(boolean showSoftInputDialog) {
            mShowSoftInputDialog = showSoftInputDialog;
            return this;
        }

        @Override
        public void configTitleView(View titleView) {
            super.configTitleView(titleView);
            if (hasContent()) {
                titleView.setPadding(
                        titleView.getPaddingLeft(),
                        titleView.getPaddingTop(),
                        titleView.getPaddingRight(),
                        dpToPx(6)
                );
            } else {
                titleView.setPadding(
                        titleView.getPaddingLeft(),
                        titleView.getPaddingTop(),
                        titleView.getPaddingRight(),
                        dpToPx(14)
                );
            }
        }

        @Override
        public View onCreateContentView(UIDialog dialog, UIDialogView dialogView, Context context) {
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setPadding(dpToPx(16), dpToPx(0), dpToPx(16), dpToPx(20));

            TextView textView = new TextView(context);
            int topPadding = hasTitle() ? dpToPx(6) : dpToPx(24);
            textView.setPadding(0, topPadding, 0, dpToPx(6));
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(15);
            textView.setTextColor(Color.parseColor("#4f4f4f"));

            if (!hasContent()) {
                textView.setVisibility(View.GONE);
            } else {
                textView.setVisibility(View.VISIBLE);
                textView.setText(mContent);
            }

            linearLayout.addView(textView);

            EditText editText = new EditText(context);
            editText.setBackgroundDrawable(
                    UIDrawableHelper.createGradientDrawable()
                            .applyColor(Color.TRANSPARENT)
                            .applyRadius(dpToPx(1))
                            .applyStoke(1, 0x95555555)
            );
            editText.setGravity(Gravity.CENTER_VERTICAL);
            editText.setTextSize(14);
            editText.setPadding(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4));
            editText.setSingleLine(true);

            mEditText = editText;

            linearLayout.addView(editText);

            if (mListener != null) {
                mListener.onEditCreate(editText);
            }

            return wrappContentViewScroll(linearLayout);
        }

        protected View wrappContentViewScroll(View view) {
            return wrapWithScroll(view);
        }

        @Override
        public void configDialogView(UIDialogView dialogView) {
            super.configDialogView(dialogView);
            IOSDialogBuilderHelper.configDialogView(dialogView);
        }

        protected boolean hasContent() {
            return !textEmpty(mContent);
        }

        @Override
        public void onAfterCreate(UIDialog dialog, UIDialogRootLayout rootLayout, Context context) {
            super.onAfterCreate(dialog, rootLayout, context);

            mEditText.post(new Runnable() {
                @Override
                public void run() {
                    if (mShowSoftInputDialog) {
                        UIBaseDialog.showSoftInputDialog(mEditText);
                    }
                }
            });
        }
    }

    public static class InputEdit{
        private EditText mEditText;

        public InputEdit setEditText(EditText editText){
            mEditText = editText;
            return this;
        }

        public EditText getEditText(){
            return mEditText;
        }

        public String getText(){
            try{
                return mEditText.getText().toString();
            }catch (Throwable e){
                e.printStackTrace();
            }
            return "";
        }
    }

    public static final class Tip extends UIDialogBuilder<Tip> {
        public static final int NONE = -1;
        public static final int LOADING = 0;
        public static final int SUCCESS = 1;
        public static final int ERROR = 2;
        public static final int INFO = 3;

        private String mContent = "";
        private long mDuration = 800;
        private int mTheme = LOADING;

        public Tip(Context context) {
            super(context);
            setDimAmount(0.0f);

            IOSDialogBuilderHelper.onConstructors(context, this, 0.3f);
        }

        @Deprecated
        @Override
        public Tip setTitle(String title) {
            return super.setTitle("");
        }

        public Tip setContent(String content) {
            mContent = content;
            return this;
        }

        public Tip setDuration(long duration) {
            mDuration = duration;
            return this;
        }

        public Tip setTheme(int theme) {
            mTheme = theme;
            return this;
        }

        @Override
        public void configDialogView(UIDialogView dialogView) {
            super.configDialogView(dialogView);
            //IOSDialogBuilderHelper.configDialogView(dialogView);
            dialogView.setBackgroundDrawable(
                    UIDrawableHelper.createGradientDrawable()
                            .applyRadius(DEF_IOS_DIALOG_BACKGROUND_RADIUS)
                            .applyColor(0xaf000000)
            );
        }

        @Override
        public View onCreateContentView(UIDialog dialog, UIDialogView dialogView, Context context) {
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setPadding(dpToPx(6), dpToPx(18), dpToPx(6), dpToPx(18));
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            UILoadView loadView = new UILoadView(context);
            loadView.setDuration(mDuration);
            loadView.setLoadingColor(Color.WHITE);
            loadView.setTheme(mTheme);
            linearLayout.addView(loadView, dpToPx(36), dpToPx(36));

            TextView textView = new TextView(context);
            textView.setPadding(0, dpToPx(4), 0, dpToPx(0));
            textView.setTextColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER);
            linearLayout.addView(textView, MATCH_PARENT, WRAP_CONTENT);
            textView.setSingleLine(true);
            textView.setTextSize(13.5f);

            if (mTheme == NONE) {
                textView.setPadding(0, dpToPx(0), 0, dpToPx(0));
                loadView.setVisibility(View.GONE);
            }
            if (!textEmpty(mContent)) {
                textView.setText(mContent);
                textView.setVisibility(View.VISIBLE);
            } else {
                textView.setVisibility(View.GONE);
            }

            return linearLayout;
        }

        @Override
        public View onCreateOperatorView(UIDialog dialog, UIDialogView dialogView, Context context) {
            return null;
        }
    }

    public interface OnCreateEditTextListener {
        void onEditCreate(EditText editText);
    }
}
