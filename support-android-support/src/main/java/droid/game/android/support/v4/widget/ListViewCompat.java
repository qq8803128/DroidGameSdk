package droid.game.android.support.v4.widget;

import android.os.Build;
import android.widget.ListView;
import droid.game.annotation.NonNull;

public class ListViewCompat {
    public static boolean canScrollList(@NonNull ListView listView, int direction) {
        if (Build.VERSION.SDK_INT >= 19) {
            // Call the framework version directly
            return listView.canScrollList(direction);
        } else {
            // provide backport on earlier versions
            final int childCount = listView.getChildCount();
            if (childCount == 0) {
                return false;
            }

            final int firstPosition = listView.getFirstVisiblePosition();
            if (direction > 0) {
                final int lastBottom = listView.getChildAt(childCount - 1).getBottom();
                final int lastPosition = firstPosition + childCount;
                return lastPosition < listView.getCount()
                        || (lastBottom > listView.getHeight() - listView.getListPaddingBottom());
            } else {
                final int firstTop = listView.getChildAt(0).getTop();
                return firstPosition > 0 || firstTop < listView.getListPaddingTop();
            }
        }
    }
}
