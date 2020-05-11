package droid.game.common.suspen;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import droid.game.common.widget.LoadingView;
import droid.game.common.widget.RoundProgressBar;

public interface Theme {
    void setContainerStyle(View view);
    void setTitleViewStyle(TextView textView);
    void setContentViewStyle(TextView textView);
    void setButtonViewStyle(View topLine,View centerLine,TextView positive,TextView negative);
    void setLoadingStyle(LoadingView loadingView);
    void setProgressBarStyle(ProgressBar progressBar,TextView textView);
    void setRoundProgressBarStyle(RoundProgressBar progressBar);
}
