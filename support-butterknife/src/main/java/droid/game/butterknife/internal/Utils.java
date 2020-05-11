package droid.game.butterknife.internal;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;

import java.lang.reflect.Array;
import java.util.List;

public final class Utils {
    public static int id(Context context, String idString){
        String packageName = context.getPackageName();
        String[] fields = idString.split("\\.");
        return context.getResources().getIdentifier(fields[2],fields[1],context.getPackageName());
    }

    private static final TypedValue VALUE = new TypedValue();

    // Implicit synchronization for use of shared resource VALUE.
    public static float getFloat(Context context,  int id) {
        TypedValue value = VALUE;
        context.getResources().getValue(id, value, true);
        if (value.type == TypedValue.TYPE_FLOAT) {
            return value.getFloat();
        }
        throw new Resources.NotFoundException("Resource ID #0x" + Integer.toHexString(id)
                + " type #0x" + Integer.toHexString(value.type) + " is not valid");
    }

    @SafeVarargs
    public static <T> T[] arrayOf(T... views) {
        return filterNull(views);
    }

    @SafeVarargs
    public static <T> List<T> listOf(T... views) {
        return new ImmutableList<>(filterNull(views));
    }

    private static <T> T[] filterNull(T[] views) {
        int end = 0;
        int length = views.length;
        for (int i = 0; i < length; i++) {
            T view = views[i];
            if (view != null) {
                views[end++] = view;
            }
        }
        if (end == length) {
            return views;
        }
        //noinspection unchecked
        T[] newViews = (T[]) Array.newInstance(views.getClass().getComponentType(), end);
        System.arraycopy(views, 0, newViews, 0, end);
        return newViews;
    }

    public static <T> T findOptionalViewAsType(View source, int id, String who,
                                               Class<T> cls) {
        View view = source.findViewById(id);
        return castView(view, id, who, cls);
    }

    public static View findRequiredView(View source,  int id, String who) {
        View view = source.findViewById(id);
        if (view != null) {
            return view;
        }
        String name = getResourceEntryName(source, id);
        throw new IllegalStateException("Required view '"
                + name
                + "' with ID "
                + id
                + " for "
                + who
                + " was not found. If this view is optional add '@Nullable' (fields) or '@Optional'"
                + " (methods) annotation.");
    }

    public static <T> T findRequiredViewAsType(View source,  int id, String who,
                                               Class<T> cls) {
        View view = findRequiredView(source, id, who);
        return castView(view, id, who, cls);
    }

    public static <T> T castView(View view,  int id, String who, Class<T> cls) {
        try {
            return cls.cast(view);
        } catch (ClassCastException e) {
            String name = getResourceEntryName(view, id);
            throw new IllegalStateException("View '"
                    + name
                    + "' with ID "
                    + id
                    + " for "
                    + who
                    + " was of the wrong type. See cause for more info.", e);
        }
    }

    public static <T> T castParam(Object value, String from, int fromPos, String to, int toPos,
                                  Class<T> cls) {
        try {
            return cls.cast(value);
        } catch (ClassCastException e) {
            throw new IllegalStateException("Parameter #"
                    + (fromPos + 1)
                    + " of method '"
                    + from
                    + "' was of the wrong type for parameter #"
                    + (toPos + 1)
                    + " of method '"
                    + to
                    + "'. See cause for more info.", e);
        }
    }

    private static String getResourceEntryName(View view,  int id) {
        if (view.isInEditMode()) {
            return "<unavailable while editing>";
        }
        return view.getContext().getResources().getResourceEntryName(id);
    }

    private Utils() {
        throw new AssertionError("No instances.");
    }
}
