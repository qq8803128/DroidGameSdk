package droid.game.butterknife;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.util.Log;
import android.util.Property;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ButterKnife {
    private ButterKnife() {
        throw new AssertionError("No instances.");
    }

    /** An action that can be applied to a list of views. */
    public interface Action<T extends View> {
        /** Apply the action on the {@code view} which is at {@code index} in the list. */

        void apply( T view, int index);
    }

    /** A setter that can apply a value to a list of views. */
    public interface Setter<T extends View, V> {
        /** Set the {@code value} on the {@code view} which is at {@code index} in the list. */

        void set( T view, V value, int index);
    }

    private static final String TAG = "ButterKnife";
    private static boolean debug = false;


    static final Map<Class<?>, Constructor<? extends Unbinder>> BINDINGS = new LinkedHashMap<>();

    /** Control whether debug logging is enabled. */
    public static void setDebug(boolean debug) {
        ButterKnife.debug = debug;
    }

    /**
     * BindView annotated fields and methods in the specified {@link Activity}. The current content
     * view is used as the view root.
     *
     * @param target Target activity for view binding.
     */

    public static Unbinder bind( Activity target) {
        View sourceView = target.getWindow().getDecorView();
        return createBinding(target, sourceView);
    }

    /**
     * BindView annotated fields and methods in the specified {@link View}. The view and its children
     * are used as the view root.
     *
     * @param target Target view for view binding.
     */

    public static Unbinder bind( View target) {
        return createBinding(target, target);
    }

    /**
     * BindView annotated fields and methods in the specified {@link Dialog}. The current content
     * view is used as the view root.
     *
     * @param target Target dialog for view binding.
     */

    public static Unbinder bind( Dialog target) {
        View sourceView = target.getWindow().getDecorView();
        return createBinding(target, sourceView);
    }

    /**
     * BindView annotated fields and methods in the specified {@code target} using the {@code source}
     * {@link Activity} as the view root.
     *
     * @param target Target class for view binding.
     * @param source Activity on which IDs will be looked up.
     */
    public static Unbinder bind( Object target,  Activity source) {
        View sourceView = source.getWindow().getDecorView();
        return createBinding(target, sourceView);
    }

    /**
     * BindView annotated fields and methods in the specified {@code target} using the {@code source}
     * {@link View} as the view root.
     *
     * @param target Target class for view binding.
     * @param source View root on which IDs will be looked up.
     */

    public static Unbinder bind( Object target,  View source) {
        return createBinding(target, source);
    }

    /**
     * BindView annotated fields and methods in the specified {@code target} using the {@code source}
     * {@link Dialog} as the view root.
     *
     * @param target Target class for view binding.
     * @param source Dialog on which IDs will be looked up.
     */

    public static Unbinder bind( Object target,  Dialog source) {
        View sourceView = source.getWindow().getDecorView();
        return createBinding(target, sourceView);
    }

    private static Unbinder createBinding( Object target,  View source) {
        Class<?> targetClass = target.getClass();
        if (debug) {
            Log.d(TAG, "Looking up binding for " + targetClass.getName());
        }
        Constructor<? extends Unbinder> constructor = findBindingConstructorForClass(targetClass);

        if (constructor == null) {
            return Unbinder.EMPTY;
        }

        //noinspection TryWithIdenticalCatches Resolves to API 19+ only type.
        try {
            return constructor.newInstance(target, source);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to invoke " + constructor, e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Unable to invoke " + constructor, e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw new RuntimeException("Unable to create binding instance.", cause);
        }
    }


    private static Constructor<? extends Unbinder> findBindingConstructorForClass(Class<?> cls) {
        Constructor<? extends Unbinder> bindingCtor = BINDINGS.get(cls);
        if (bindingCtor != null) {
            if (debug) {
                Log.d(TAG, "HIT: Cached in binding map.");
            }
            return bindingCtor;
        }
        String clsName = cls.getName();
        if (clsName.startsWith("android.") || clsName.startsWith("java.")) {
            if (debug) {
                Log.d(TAG, "MISS: Reached framework class. Abandoning search.");
            }
            return null;
        }
        try {
            Class<?> bindingClass = cls.getClassLoader().loadClass(clsName + "_ViewBinding");
            //noinspection unchecked
            bindingCtor = (Constructor<? extends Unbinder>) bindingClass.getConstructor(cls, View.class);
            if (debug) Log.d(TAG, "HIT: Loaded binding class and constructor.");
        } catch (ClassNotFoundException e) {
            if (debug) Log.d(TAG, "Not found. Trying superclass " + cls.getSuperclass().getName());
            bindingCtor = findBindingConstructorForClass(cls.getSuperclass());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to find binding constructor for " + clsName, e);
        }
        BINDINGS.put(cls, bindingCtor);
        return bindingCtor;
    }

    /** Apply the specified {@code actions} across the {@code list} of views. */

    @SafeVarargs public static <T extends View> void apply( List<T> list,
                                                            Action<? super T>... actions) {
        for (int i = 0, count = list.size(); i < count; i++) {
            for (Action<? super T> action : actions) {
                action.apply(list.get(i), i);
            }
        }
    }

    /** Apply the specified {@code actions} across the {@code array} of views. */

    @SafeVarargs public static <T extends View> void apply( T[] array,
                                                            Action<? super T>... actions) {
        for (int i = 0, count = array.length; i < count; i++) {
            for (Action<? super T> action : actions) {
                action.apply(array[i], i);
            }
        }
    }

    /** Apply the specified {@code action} across the {@code list} of views. */

    public static <T extends View> void apply( List<T> list,
                                               Action<? super T> action) {
        for (int i = 0, count = list.size(); i < count; i++) {
            action.apply(list.get(i), i);
        }
    }

    /** Apply the specified {@code action} across the {@code array} of views. */
    public static <T extends View> void apply( T[] array,  Action<? super T> action) {
        for (int i = 0, count = array.length; i < count; i++) {
            action.apply(array[i], i);
        }
    }

    /** Apply {@code actions} to {@code view}. */
    @SafeVarargs public static <T extends View> void apply( T view,
                                                            Action<? super T>... actions) {
        for (Action<? super T> action : actions) {
            action.apply(view, 0);
        }
    }

    /** Apply {@code action} to {@code view}. */

    public static <T extends View> void apply( T view,  Action<? super T> action) {
        action.apply(view, 0);
    }

    /** Set the {@code value} using the specified {@code setter} across the {@code list} of views. */

    public static <T extends View, V> void apply( List<T> list,
                                                  Setter<? super T, V> setter, V value) {
        for (int i = 0, count = list.size(); i < count; i++) {
            setter.set(list.get(i), value, i);
        }
    }

    /** Set the {@code value} using the specified {@code setter} across the {@code array} of views. */

    public static <T extends View, V> void apply( T[] array,
                                                  Setter<? super T, V> setter, V value) {
        for (int i = 0, count = array.length; i < count; i++) {
            setter.set(array[i], value, i);
        }
    }

    /** Set {@code value} on {@code view} using {@code setter}. */

    public static <T extends View, V> void apply( T view,
                                                  Setter<? super T, V> setter, V value) {
        setter.set(view, value, 0);
    }

    /**
     * Apply the specified {@code value} across the {@code list} of views using the {@code property}.
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) // http://b.android.com/213630
    public static <T extends View, V> void apply(List<T> list,
                                                 Property<? super T, V> setter, V value) {
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0, count = list.size(); i < count; i++) {
            setter.set(list.get(i), value);
        }
    }

    /**
     * Apply the specified {@code value} across the {@code array} of views using the {@code property}.
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) // http://b.android.com/213630
    public static <T extends View, V> void apply( T[] array,
                                                  Property<? super T, V> setter, V value) {
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0, count = array.length; i < count; i++) {
            setter.set(array[i], value);
        }
    }

    /** Apply {@code value} to {@code view} using {@code property}. */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) // http://b.android.com/213630
    public static <T extends View, V> void apply( T view,
                                                  Property<? super T, V> setter, V value) {
        setter.set(view, value);
    }
}
