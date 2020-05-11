package droid.game.permission.runtime.easy;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import droid.game.permission.runtime.easy.helper.PermissionHelper;

import java.util.Arrays;

class RationaleDialogClickListener implements Dialog.OnClickListener {

    private Object mHost;
    private RationaleDialogConfig mConfig;
    private EasyPermissions.PermissionCallbacks mCallbacks;
    private EasyPermissions.RationaleCallbacks mRationaleCallbacks;

    RationaleDialogClickListener(RationaleDialogFragment dialogFragment,
                                 RationaleDialogConfig config,
                                 EasyPermissions.PermissionCallbacks callbacks,
                                 EasyPermissions.RationaleCallbacks dialogCallback) {

        mHost = dialogFragment.getActivity();

        mConfig = config;
        mCallbacks = callbacks;
        mRationaleCallbacks = dialogCallback;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        int requestCode = mConfig.requestCode;
        if (which == Dialog.BUTTON_POSITIVE) {
            String[] permissions = mConfig.permissions;
            if (mRationaleCallbacks != null) {
                mRationaleCallbacks.onRationaleAccepted(requestCode);
            }
            if (mHost instanceof Activity) {
                PermissionHelper.newInstance((Activity) mHost).directRequestPermissions(requestCode, permissions);
            } else {
                throw new RuntimeException("Host must be an Activity or Fragment!");
            }
        } else {
            if (mRationaleCallbacks != null) {
                mRationaleCallbacks.onRationaleDenied(requestCode);
            }
            notifyPermissionDenied();
        }
    }

    private void notifyPermissionDenied() {
        if (mCallbacks != null) {
            mCallbacks.onPermissionsDenied(mConfig.requestCode, Arrays.asList(mConfig.permissions));
        }
    }
}
