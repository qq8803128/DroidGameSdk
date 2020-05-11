package droid.game.plugin.sdk.delegate.util;

import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.widget.EditText;

public class InputHelper {

    public InputHelper with(EditText editText){
        return new InputHelper(editText);
    }

    private EditText mEditText;
    private InputHelper(EditText editText) {
        mEditText = editText;
    }

    public InputHelper setText(CharSequence charSequence){
        editText().setText(charSequence);
        return this;
    }

    public InputHelper setMaxLength(int maxLength){
        return this;
    }

    public InputHelper setDigits(String digits){
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

    public int getLength(){
        return editText().getText().toString().length();
    }

    public String getText(){
        return editText().getText().toString();
    }

    public EditText editText(){
        return mEditText;
    }

    public void unbinder() {
        mEditText = null;
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
