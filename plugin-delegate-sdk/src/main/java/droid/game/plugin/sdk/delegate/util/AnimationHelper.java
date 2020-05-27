package droid.game.plugin.sdk.delegate.util;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class AnimationHelper {
    public static void animationToPage(final ViewGroup page, int newPage){
        int oldPageFlag = -1;
        for (int i = 0; i < page.getChildCount(); i++){
            if (page.getChildAt(i).getVisibility() == View.VISIBLE){
                oldPageFlag = i;
                break;
            }
        }

        if (oldPageFlag == -1){
            page.getChildAt(newPage).setVisibility(View.VISIBLE);
            return;
        }

        if (oldPageFlag != newPage){
            final int oldPage = oldPageFlag;
            page.getChildAt(newPage).setVisibility(View.VISIBLE);

            float f1 = oldPageFlag < newPage ? 1f : -1f;
            float f2 = oldPageFlag < newPage ? -1f : 1f;
            TranslateAnimation animation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF,f1,Animation.RELATIVE_TO_SELF,0f,
                    Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f
            );
            animation.setDuration(300);
            page.getChildAt(newPage).startAnimation(animation);

            animation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,f2,
                    Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f
            );
            animation.setDuration(300);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    page.getChildAt(oldPage).setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            page.getChildAt(oldPage).startAnimation(animation);

        }

    }
}
