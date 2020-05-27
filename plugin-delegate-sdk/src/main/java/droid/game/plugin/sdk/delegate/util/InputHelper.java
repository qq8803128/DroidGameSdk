package droid.game.plugin.sdk.delegate.util;

import android.graphics.drawable.GradientDrawable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.widget.EditText;

import java.lang.ref.WeakReference;

import static droid.game.common.util.Utils.dp;

public class InputHelper {

    public static InputHelper with(EditText editText){
        return new InputHelper(editText);
    }

    private WeakReference<EditText> mEditText;
    private InputHelper(EditText editText) {
        mEditText = new WeakReference(editText);
    }

    public InputHelper setText(CharSequence charSequence){
        editText().setText(charSequence);
        return this;
    }

    public InputHelper setSingleLine(){
        editText().setMaxLines(1);
        return this;
    }

    public InputHelper setInputPassword(){
        editText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        return this;
    }

    public InputHelper setInputNumber(){
        editText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        return this;
    }

    public InputHelper setInputText(){
        editText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        return this;
    }

    public InputHelper setInputDefault(){
        editText().setInputType(InputType.TYPE_CLASS_TEXT);
        return this;
    }

    public InputHelper addFilter(InputFilter filter){
        InputFilter[] filters = editText().getFilters();
        int length = filters == null ? 0 : filters.length;
        InputFilter[] newFilters = new InputFilter[length + 1];
        if (length == 0){
            newFilters[0] = filter;
        }else{
            System.arraycopy(filters,0,newFilters,0,length);
            newFilters[length] = filter;
        }
        editText().setFilters(newFilters);
        return this;
    }

    private EditText editText(){
        return mEditText.get();
    }

    public static class DigitsFilter implements InputFilter {
        private final String mDigits;

        public DigitsFilter(String digits){
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

    public static class LengthFilter extends InputFilter.LengthFilter{
        public LengthFilter(int max) {
            super(max);
        }
    }

}
