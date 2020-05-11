package droid.game.common.util;

import android.graphics.drawable.GradientDrawable;

public class UIDrawableHelper {
    public static GradientDrawableWrapper createGradientDrawable(){
        return new GradientDrawableWrapper();
    }

    public static class GradientDrawableWrapper extends GradientDrawable{

        public GradientDrawableWrapper applyColor(int color) {
            setColor(color);
            return this;
        }

        public GradientDrawableWrapper applyRadius(float radius){
            setCornerRadius(radius);
            return this;
        }

        public GradientDrawableWrapper applyStoke(int width,int color){
            setStroke(width,color);
            return this;
        }

        public GradientDrawableWrapper applyRadius(float[] radii){
            setCornerRadii(radii);
            return this;
        }
    }
}
