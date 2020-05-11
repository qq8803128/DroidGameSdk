package droid.game.common.util;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import org.xml.sax.XMLReader;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static final int dp(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, Resources.getSystem().getDisplayMetrics());
    }

    public static final boolean portrait() {
        return windowWidth() < windowHeight();
    }

    public static final int windowWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static final int windowHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static Spanned fromHtml(String source) {
        source = source.replaceAll("<span", "<custom");
        source = source.replaceAll("</span", "</custom");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(
                    source,
                    Html.FROM_HTML_MODE_COMPACT,
                    null,
                    new CssStyleTagHandler()
            );
        } else {
            return Html.fromHtml(source, null, new CssStyleTagHandler());
        }

    }

    static class CssStyleTagHandler implements Html.TagHandler {
        private final String TAG = "CustomTagHandler";

        private int startIndex = 0;
        private int stopIndex = 0;


        public CssStyleTagHandler() {
        }

        @Override
        public void handleTag(boolean opening, String tag, Editable output,
                              XMLReader xmlReader) {

            processAttributes(xmlReader);

            if (tag.equalsIgnoreCase("custom")) {
                if (opening) {
                    startSpan(tag, output, xmlReader);
                } else {
                    endSpan(tag, output, xmlReader);
                    attributes.clear();
                }
            }

        }

        public void startSpan(String tag, Editable output, XMLReader xmlReader) {
            startIndex = output.length();
        }

        public void endSpan(String tag, Editable output, XMLReader xmlReader) {
            stopIndex = output.length();

            String color = attributes.get("color");
            String size = attributes.get("size");
            String style = attributes.get("style");
            if (!TextUtils.isEmpty(style)) {
                analysisStyle(startIndex, stopIndex, output, style);
            }
            if (!TextUtils.isEmpty(size)) {
                size = size.split("px")[0];
            }
            if (!TextUtils.isEmpty(color) && !TextUtils.isEmpty(size)) {
                output.setSpan(new ForegroundColorSpan(Color.parseColor(color)), startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                output.setSpan(new AbsoluteSizeSpan(dp(Float.parseFloat(size))), startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (!TextUtils.isEmpty(color)) {
                try {
                    output.setSpan(new ForegroundColorSpan(Color.parseColor(color)), startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        final HashMap<String, String> attributes = new HashMap<String, String>();

        private void processAttributes(final XMLReader xmlReader) {
            try {
                Field elementField = xmlReader.getClass().getDeclaredField("theNewElement");
                elementField.setAccessible(true);
                Object element = elementField.get(xmlReader);
                Field attsField = element.getClass().getDeclaredField("theAtts");
                attsField.setAccessible(true);
                Object atts = attsField.get(element);
                Field dataField = atts.getClass().getDeclaredField("data");
                dataField.setAccessible(true);
                String[] data = (String[]) dataField.get(atts);
                Field lengthField = atts.getClass().getDeclaredField("length");
                lengthField.setAccessible(true);
                int len = (Integer) lengthField.get(atts);

                /**
                 * MSH: Look for supported attributes and add to hash map.
                 * This is as tight as things can get :)
                 * The data index is "just" where the keys and values are stored.
                 */
                for (int i = 0; i < len; i++)
                    attributes.put(data[i * 5 + 1], data[i * 5 + 4]);
            } catch (Exception e) {
            }
        }

        private void analysisStyle(int startIndex, int stopIndex, Editable editable, String style) {
            String[] attrArray = style.split(";");
            Map<String, String> attrMap = new HashMap<>();
            if (null != attrArray) {
                for (String attr : attrArray) {
                    String[] keyValueArray = attr.split(":");
                    if (null != keyValueArray && keyValueArray.length == 2) {
                        // 记住要去除前后空格
                        attrMap.put(keyValueArray[0].trim(), keyValueArray[1].trim());
                    }
                }
            }

            String color = attrMap.get("color");
            String size = attrMap.get("font-size");
            if (!TextUtils.isEmpty(size)) {
                size = size.split("px")[0];
            }
            if (!TextUtils.isEmpty(color) && !TextUtils.isEmpty(size)) {
                editable.setSpan(new ForegroundColorSpan(Color.parseColor(color)), startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                editable.setSpan(new AbsoluteSizeSpan(dp(Float.parseFloat(size))), startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (!TextUtils.isEmpty(color)) {

                try {
                    editable.setSpan(new ForegroundColorSpan(Color.parseColor(color)), startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
