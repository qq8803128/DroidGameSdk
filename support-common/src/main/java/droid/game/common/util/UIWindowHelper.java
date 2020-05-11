package droid.game.common.util;

import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewParent;
import droid.game.annotation.NonNull;
import droid.game.annotation.Nullable;

import java.lang.reflect.Field;

public final class UIWindowHelper {
    @Nullable
    @SuppressWarnings({"JavaReflectionMemberAccess"})
    public static Rect unSafeGetWindowVisibleInsets(@NonNull View view) {
        Object attachInfo = getAttachInfoFromView(view);
        if (attachInfo == null) {
            return null;
        }
        try {
            // fortunately now it is in light greylist, just be warned.
            Field visibleInsetsField = attachInfo.getClass().getDeclaredField("mVisibleInsets");
            visibleInsetsField.setAccessible(true);
            Object visibleInsets = visibleInsetsField.get(attachInfo);
            if (visibleInsets instanceof Rect) {
                return (Rect) visibleInsets;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    @SuppressWarnings({"JavaReflectionMemberAccess"})
    public static Rect unSafeGetContentInsets(@NonNull View view) {
        Object attachInfo = getAttachInfoFromView(view);
        if (attachInfo == null) {
            return null;
        }
        try {
            // fortunately now it is in light greylist, just be warned.
            Field visibleInsetsField = attachInfo.getClass().getDeclaredField("mContentInsets");
            visibleInsetsField.setAccessible(true);
            Object visibleInsets = visibleInsetsField.get(attachInfo);
            if (visibleInsets instanceof Rect) {
                return (Rect) visibleInsets;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getAttachInfoFromView(@NonNull View view) {
        Object attachInfo = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            // Android 10+ can not reflect the View.mAttachInfo
            // fortunately now it is in light greylist in ViewRootImpl
            View rootView = view.getRootView();
            if (rootView != null) {
                ViewParent vp = rootView.getParent();
                if (vp != null) {
                    try {
                        Field field = vp.getClass().getDeclaredField("mAttachInfo");
                        field.setAccessible(true);
                        attachInfo = field.get(vp);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            try {
                // Android P forbid the reflection for @hide filed,
                // fortunately now it is in light greylist, just be warned.
                Field field = View.class.getDeclaredField("mAttachInfo");
                field.setAccessible(true);
                attachInfo = field.get(view);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return attachInfo;
    }
}
