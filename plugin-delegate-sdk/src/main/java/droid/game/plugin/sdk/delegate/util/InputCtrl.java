package droid.game.plugin.sdk.delegate.util;


import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;


public class InputCtrl {
    private LinearLayout mLinearLayout;

    public InputCtrl(LinearLayout linearLayout){
        mLinearLayout = linearLayout;
        if (cleanView() != null) {
            cleanView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText().setText("");
                }
            });
            if (getLength() == 0) {
                cleanView().setVisibility(View.GONE);
            }
        }
    }

    public LinearLayout getLayout(){
        return mLinearLayout;
    }

    public EditText editText(){
        return (EditText) mLinearLayout.getChildAt(1);
    }

    public ImageView cleanView(){
        try {
            return (ImageView) mLinearLayout.getChildAt(2);
        }catch (Throwable e){
            e.printStackTrace();
        }
        return null;
    }

    public void setText(String text){
        editText().setText(text);
    }

    public void setMaxLength(int maxLength){
        InputFilter[] oldFilters = editText().getFilters();
        if (oldFilters != null && oldFilters.length > 0) {
            InputFilter[] newFilters = new InputFilter[oldFilters.length + 1];
            System.arraycopy(oldFilters,0,newFilters,0,oldFilters.length);
            newFilters[oldFilters.length] = new InputFilter.LengthFilter(maxLength);
            editText().setFilters(newFilters);
        }else {
            editText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        }
    }

    public void setSingleLine(){
        editText().setMaxLines(1);
    }

    public void setInputPassword(){
        editText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    public void setInputNumber(){
        editText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
    }

    public void setInputText(){
        editText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
    }

    public void setDigits(String digits){
        //editText().setKeyListener(DigitsKeyListener.getInstance(digits));
        InputFilter[] oldFilters = editText().getFilters();
        if (oldFilters != null && oldFilters.length > 0) {
            InputFilter[] newFilters = new InputFilter[oldFilters.length + 1];
            System.arraycopy(oldFilters,0,newFilters,0,oldFilters.length);
            newFilters[oldFilters.length] = new DigitsFilter(digits);
            editText().setFilters(newFilters);
        }else {
            editText().setFilters(new InputFilter[]{new DigitsFilter(digits)});
        }
    }

    public int getLength(){
        return editText().getText().toString().length();
    }

    public void unbinder() {
        cleanView().setOnClickListener(null);
        mLinearLayout = null;
    }


    private class DigitsFilter implements InputFilter {
        private final String mDigits;

        DigitsFilter(String digits){
            mDigits = digits;
        }
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (mDigits.contains(source)){
                return source;
            }
            return "";
        }
    }
}
