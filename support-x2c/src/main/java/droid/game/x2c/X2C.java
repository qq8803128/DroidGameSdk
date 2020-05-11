package droid.game.x2c;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public final class X2C {
    private static final SparseArray<IViewCreator> sSparseArray = new SparseArray<>();

    /**
     * 设置contentview，检测如果有对应的java文件，使用java文件，否则使用xml
     *
     * @param activity 上下文
     * @param layoutId layout的资源id
     */
    public static void setContentView(Activity activity, Context cloneInContext, int layoutId) {
        if (activity == null) {
            throw new IllegalArgumentException("Activity must not be null");
        }
        View view = getView(activity, cloneInContext, layoutId);
        if (view != null) {
            activity.setContentView(view);
        } else {
            activity.setContentView(layoutId);
        }
    }

    /**
     * 加载xml文件，检测如果有对应的java文件，使用java文件，否则使用xml
     *
     * @param context  上下文
     * @param layoutId layout的资源id
     */
    public static View inflate(Context context, Context cloneInContext, int layoutId, ViewGroup parent) {
        return inflate(context, cloneInContext, layoutId, parent, parent != null);
    }

    public static View inflate(Context context, Context cloneInContext, int layoutId, ViewGroup parent, boolean attach) {
        return inflate(LayoutInflater.from(context), cloneInContext, layoutId, parent, attach);
    }

    public static View inflate(LayoutInflater inflater, Context cloneInContext, int layoutId, ViewGroup parent) {
        return inflate(inflater, cloneInContext, layoutId, parent, parent != null);
    }

    public static View inflate(LayoutInflater inflater, Context cloneInContext, int layoutId, ViewGroup parent, boolean attach) {
        View view = getView(inflater.getContext(), cloneInContext, layoutId);
        if (view != null) {
            if (parent != null && attach) {
                parent.addView(view);
            }
            return view;
        } else {
            return inflater.inflate(layoutId, parent, attach);
        }
    }

    public static View getView(Context context, Context cloneInContext, int layoutId) {
        IViewCreator creator = sSparseArray.get(layoutId);
        Context ctx = cloneInContext == null ? context : cloneInContext;
        if (creator == null) {
            try {
                int group = generateGroupId(layoutId);
                String layoutName = ctx.getResources().getResourceName(layoutId);
                layoutName = layoutName.substring(layoutName.lastIndexOf("/") + 1);
                String clzName = "droid.game.x2c.X2C" + group + "_" + layoutName;
                Log.e("Class",clzName);
                creator = (IViewCreator) context.getClassLoader().loadClass(clzName).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //如果creator为空，放一个默认进去，防止每次都调用反射方法耗时
            if (creator == null) {
                creator = new DefaultCreator();
            }
            sSparseArray.put(layoutId, creator);
        }
        return creator.createView(context, cloneInContext);
    }

    private static class DefaultCreator implements IViewCreator {

        @Override
        public View createView(Context context, Context cloneInContext) {
            return null;
        }
    }

    private static int generateGroupId(int layoutId) {
        return layoutId >> 24;
    }
}