package droid.game.common.dialog;

import android.view.View;
import droid.game.annotation.NonNull;
import droid.game.common.widget.UIWrapContentScrollView;

public interface IContentScroll {
    @NonNull
    UIWrapContentScrollView wrapWithScroll(@NonNull View view);
}
