package droid.game.common.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

public final class UIResourcesHelper {
    public static void setContentView(Activity activity, String layoutId){
        activity.setContentView(getIdentifier(activity,layoutId));
    }

    public static int getIdentifier(Context context, String resId){
        String[] fields = resId.split("\\.");
        return context.getResources().getIdentifier(fields[2],fields[1],context.getPackageName());
    }

    public static <T extends View> T findViewById(Activity activity, String resId){
        return activity.findViewById(getIdentifier(activity,resId));
    }

    public static <T extends View> T findViewById(View view, String resId){
        return view.findViewById(getIdentifier(view.getContext(),resId));
    }

    public static <T extends Drawable> T getDrawable(Context context, String resId){
        return (T) context.getResources().getDrawable(getId(context,resId));
    }

    public static int getLayout(Context context,String resId){
        return getIdentifier(context,resId);
    }

    public static String getString(Context context,String resId){
        return context.getResources().getString(getIdentifier(context,resId));
    }

    public static String[] getStringArray(Context context,String resId){
        return context.getResources().getStringArray(getIdentifier(context,resId));
    }

    public static int[] getIntegerArray(Context context,String resId){
        return context.getResources().getIntArray(getIdentifier(context,resId));
    }

    public static CharSequence[] getTextArray(Context context,String resId){
        return context.getResources().getTextArray(getIdentifier(context,resId));
    }

    public static float getDimen(Context context,String resId){
        return context.getResources().getDimension(getIdentifier(context,resId));
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static boolean getBoolean(Context context, String resId){
        return context.getResources().getBoolean(getIdentifier(context,resId));
    }

    public static int getInteger(Context context,String resId){
        return context.getResources().getInteger(getIdentifier(context,resId));
    }

    public static int getColor(Context context,String resId){
        return context.getResources().getColor(getIdentifier(context,resId));
    }

    public static int getId(Context context,String resId){
        return getIdentifier(context,resId);
    }

}
