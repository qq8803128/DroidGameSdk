package droid.game.permission.runtime;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.*;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import droid.game.common.dialog.UIDialog;
import droid.game.common.dialog.UIDialogBuilder;
import droid.game.common.dialog.widget.UIDialogView;
import droid.game.common.util.UIDisplayHelper;
import droid.game.common.util.UIDrawableHelper;
import droid.game.common.widget.UICheckBox;
import droid.game.open.source.picasso.Picasso;
import droid.game.open.source.picasso.Transformation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static droid.game.common.util.UIDisplayHelper.dpToPx;

public class PermissionDialog extends UIDialogBuilder<PermissionDialog> {
    private List<String> mPermissions;
    private String mUserProtocolUrl;
    private DialogInterface.OnClickListener mListener;

    public PermissionDialog(Context context) {
        super(context);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        setLayout(calcWidth(0.85f),calcWidth(0.85f),0,0);
    }

    @Override
    public UIDialog onCreateDialog(Context context) {
        return new UIDialog(context,true);
    }

    @Override
    public void configDialogView(UIDialogView dialogView) {
        super.configDialogView(dialogView);
        dialogView.setPadding(0,0,0,0);
        dialogView.setBackgroundDrawable(
                UIDrawableHelper.createGradientDrawable()
                .applyRadius(dpToPx(1))
                .applyColor(Color.parseColor("#ffffff"))
        );
    }

    @Override
    public View onCreateContentView(UIDialog dialog, UIDialogView dialogView, final Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView textView = new TextView(context);
        textView.setTextSize(18);
        linearLayout.setPadding(0,0,0,dpToPx(24));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);
        textView.setText("权限申请说明");
        linearLayout.addView(textView,MATCH_PARENT,dpToPx(48));
        textView.setBackgroundDrawable(
                UIDrawableHelper.createGradientDrawable()
                .applyColor(Color.parseColor("#5198CE"))
                .applyRadius(new float[]{dpToPx(1),dpToPx(1),dpToPx(1),dpToPx(1),0,0,0,0})
        );

        textView = new TextView(context);
        textView.setTextSize(14);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.parseColor("#5198CE"));
        textView.setPadding(dpToPx(16),dpToPx(12),dpToPx(16),dpToPx(12));
        textView.setText("为了良好的游戏体验,我们需要向您申请以下权限:");
        linearLayout.addView(textView,MATCH_PARENT,WRAP_CONTENT);

        final HorizontalScrollView scrollView = new HorizontalScrollView(context);
        linearLayout.addView(scrollView,MATCH_PARENT,dpToPx(120));

        Button button = new Button(context);
        button.setBackgroundDrawable(
                UIDrawableHelper.createGradientDrawable()
                        .applyColor(Color.parseColor("#5198CE"))
                        .applyRadius(dpToPx(1))
        );
        button.setTextColor(Color.WHITE);
        button.setTextSize(16);
        button.setText("确定");
        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(MATCH_PARENT,dpToPx(40));
        marginLayoutParams.leftMargin = dpToPx(16);
        marginLayoutParams.rightMargin = dpToPx(16);
        linearLayout.addView(button,marginLayoutParams);

        LinearLayout linearLayout1 = new LinearLayout(context);
        linearLayout1.setGravity(Gravity.CENTER);
        linearLayout1.setPadding(dpToPx(16),dpToPx(12),dpToPx(16),0);
        linearLayout.addView(linearLayout1,MATCH_PARENT,WRAP_CONTENT);
        UICheckBox checkBox = new UICheckBox(context);
        checkBox.setColor(Color.parseColor("#5198CE"));
        checkBox.setChecked(true);
        linearLayout1.addView(checkBox,dpToPx(16),dpToPx(16));

        textView = new TextView(context);
        textView.setTextSize(15);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.parseColor("#5198CE"));
        textView.setText("我已阅读并同意");
        textView.setPadding(dpToPx(2),0,0,0);
        linearLayout1.addView(textView,WRAP_CONTENT,WRAP_CONTENT);

        if (TextUtils.isEmpty(mUserProtocolUrl)){
            linearLayout1.setVisibility(View.GONE);
        }

        textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.parseColor("#5198CE"));
        textView.setText("《用户隐私保护协议》");
        textView.getPaint().setFakeBoldText(true);
        linearLayout1.addView(textView,WRAP_CONTENT,WRAP_CONTENT);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserProtocolDialog();
            }
        });

        final LinearLayout linearLayout2 = new LinearLayout(context);
        linearLayout2.setGravity(Gravity.CENTER);
        linearLayout2.setOrientation(LinearLayout.HORIZONTAL);
        scrollView.addView(linearLayout2,MATCH_PARENT,MATCH_PARENT);
        scrollView.setFillViewport(true);

        scrollView.setPadding(dpToPx(8),0,dpToPx(8),0);

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                setupPermissions(context,linearLayout2);
            }
        });
        scrollView.setHorizontalFadingEdgeEnabled(false);
        scrollView.setHorizontalScrollBarEnabled(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onClick(getDialog(),DialogInterface.BUTTON_POSITIVE);
                }
            }
        });

        return linearLayout;
    }

    private void openUserProtocolDialog() {
        new ProtocolDialog(getBaseContext(),mUserProtocolUrl)
                .setCancelable(true)
                .setCanceledOnTouchOutside(false)
                .show();
    }

    private void setupPermissions(Context context,LinearLayout linearLayout2){
        int width = calcWidth(0.85f) - dpToPx(16);

        /*
        List<String> GROUP = Arrays.asList(
                "android.permission-group.CALENDAR",
                "android.permission-group.CAMERA",
                "android.permission-group.CONTACTS",
                "android.permission-group.LOCATION",
                "android.permission-group.MICROPHONE",
                "android.permission-group.PHONE",
                "android.permission-group.SENSORS",
                "android.permission-group.SMS",
                "android.permission-group.STORAGE"
        ) ;
        */

        List<String> GROUP = PermissionScaner.getDangerousPermissionsGroup(context,mPermissions);

        Map<String,String> transform = new HashMap<>();
        transform.put("CALENDAR".toLowerCase(),"日历");
        transform.put("CAMERA".toLowerCase(),"相机");
        transform.put("CONTACTS".toLowerCase(),"通讯录");
        transform.put("LOCATION".toLowerCase(),"位置信息");
        transform.put("MICROPHONE".toLowerCase(),"麦克风");
        transform.put("PHONE".toLowerCase(),"电话");
        transform.put("SENSORS".toLowerCase(),"传感器");
        transform.put("SMS".toLowerCase(),"短信");
        transform.put("STORAGE".toLowerCase(),"存储空间");

        for (String group : GROUP){
            String[] split = group.split("\\.");
            String name = split[split.length - 1].toLowerCase();

            if (!transform.containsKey(name)){
                continue;
            }

            int size = width / 4 - dpToPx(16);
            if (size > dpToPx(48)){
                size = dpToPx(48);
            }
            ImageView imageView = new ImageView(context);
            LinearLayout linearLayout3 = new LinearLayout(context);
            linearLayout3.setOrientation(LinearLayout.VERTICAL);
            linearLayout3.setGravity(Gravity.CENTER);
            linearLayout3.addView(imageView,size,size);


            Picasso.with(context)
                    .load("file:///android_asset/permission/group-icon/permission_ic_" + name + ".png")
                    .resize(size,size)
                    .centerCrop()
                    .transform(permissionTransformation)
                    .into(imageView);
            TextView textView1 = new TextView(context);
            textView1.setTextSize(13);
            textView1.setTextColor(Color.parseColor("#5198CE"));
            textView1.setPadding(dpToPx(6),dpToPx(12),dpToPx(6),dpToPx(0));
            textView1.setSingleLine(true);
            textView1.setText(transform.get(name));
            textView1.setGravity(Gravity.CENTER);
            linearLayout3.addView(textView1,MATCH_PARENT,WRAP_CONTENT);

            linearLayout2.addView(linearLayout3,width / 4, MATCH_PARENT);

            linearLayout2.setGravity(Gravity.CENTER);
        }
    }

    private int calcWidth(float percentWidth){
        int w = UIDisplayHelper.getScreenWidth(getBaseContext());
        int h = UIDisplayHelper.getScreenHeight(getBaseContext());
        int width = (int) (w * percentWidth);
        if (w > h){
            width = (int) (h * percentWidth);
        }
        return width;
    }

    private Transformation permissionTransformation = new Transformation() {
        @Override
        public Bitmap transform(Bitmap source) {
            int width, height;
            height = source.getHeight();
            width = source.getWidth();

            Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bmpGrayscale);
            Paint paint = new Paint();
            ColorMatrix cm = new ColorMatrix();
            cm.set(
                    new float[]{
                            1, 0, 0, 0, 0x51,
                            1, 0, 0, 0, 0x98,
                            1, 0, 0, 0, 0xce,
                            0, 0, 0, 1, 0
                    }
            );
            ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
            paint.setColorFilter(f);
            c.drawBitmap(source, 0, 0, paint);

            if(source!=null && source!=bmpGrayscale){
                source.recycle();
            }
            return bmpGrayscale;
        }

        @Override
        public String key() {
            return "permission-transform";
        }
    };

    public PermissionDialog setPermissions(List<String> permissions) {
        mPermissions = permissions;
        return this;
    }

    public PermissionDialog setUserProtocolUrl(String userProtocolUrl) {
        mUserProtocolUrl = userProtocolUrl;
        return this;
    }

    public PermissionDialog setOnClickListener(DialogInterface.OnClickListener listener) {
        mListener = listener;
        return this;
    }
}
