package droid.game.common.span;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Span {
    private List<Builder> mBuilders = new ArrayList<>();
    private String mText;
    public Span(String text) {
        super();
        mText = text;
    }

    public Span lightText(String lightText){
        getBuilder().mHightLightText = lightText;
        return this;
    }

    public Span textColor(int textColor,int pressTextColor){
        getBuilder().mNormalTextColor = textColor;
        getBuilder().mPressedTextColor = pressTextColor;
        return this;
    }

    public Span backgroundColor(int backgroundColor,int pressBackgroundColor){
        getBuilder().mNormalBackgroundColor = backgroundColor;
        getBuilder().mPressedBackgroundColor = pressBackgroundColor;
        return this;
    }

    public Span listener(OnClickSpanListener listener){
        getBuilder().mListener = listener;
        return this;
    }

    public Span add(){
        mBuilders.add(new Builder());
        return this;
    }

    private Builder getBuilder(){
        return mBuilders.size() > 0 ? mBuilders.get(mBuilders.size() - 1) : new Builder();
    }

    public CharSequence build(){
        if (!TextUtils.isEmpty(mText)){
            int start = 0, end;
            int index;

            SpannableString sp = new SpannableString(mText);
            if (mBuilders != null && mBuilders.size() > 0){
                for (Builder builder : mBuilders) {
                    final Builder builder1 = builder;
                    String hightLightText = builder.mHightLightText;
                    while ((index = mText.indexOf(hightLightText, start)) > -1) {
                        end = index + hightLightText.length();
                        sp.setSpan(new UITouchableSpan(
                                builder.mNormalTextColor,
                                builder.mPressedTextColor,
                                builder.mNormalBackgroundColor,
                                builder.mPressedBackgroundColor
                        ) {
                            @Override
                            public void onSpanClick(View widget) {
                                if (builder1.mListener != null) {
                                    builder1.mListener.onClick(widget,builder1.mHightLightText);
                                }
                            }
                        }, index, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        start = end;
                    }
                }
            }
            return sp;
        }
        return "";
    }

    public static class Builder {
        String mHightLightText;
        int mNormalTextColor = 0xff2AAFDD;
        int mPressedTextColor = 0xff2AAFDD;
        int mNormalBackgroundColor = 0x00000000;
        int mPressedBackgroundColor = 0x35000000;
        OnClickSpanListener mListener;
    }
        public interface OnClickSpanListener{
        void onClick(View v,String lightText);
    }
}
