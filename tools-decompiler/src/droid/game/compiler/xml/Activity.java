package droid.game.compiler.xml;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Activity extends Item {
    private boolean mIsLauncherActivity;

    @Override
    public Item createReadChildren(Node node) {
        Item item = null;
        switch (node.getNodeName()) {
            case "intent-filter":
                IntentFilter intentFilter = (IntentFilter) new IntentFilter().parse(node);
                item = intentFilter;
                mIsLauncherActivity = intentFilter.isLauncherActivity();
                if (mIsLauncherActivity){
                    changeActivityLaunchMode();
                }
                break;
            default:
                item = super.createReadChildren(node);
                break;

        }
        return item;
    }

    public boolean isLauncherActivity(){
        return mIsLauncherActivity;
    }

    @Override
    protected String getNameFilter() {
        return "remove-android-activity.json";
    }

    private void changeActivityLaunchMode(){
        attr.put("android:launchMode","standard");
    }
}
