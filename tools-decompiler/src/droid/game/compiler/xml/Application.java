package droid.game.compiler.xml;

import org.w3c.dom.Node;

public class Application extends Item{
    public Activity launcherActivity;
    @Override
    public Item createReadChildren(Node node) {
        Item item = null;
        switch (node.getNodeName()) {
            case "activity":
                Activity activity = (Activity) new Activity().parse(node);
                if(activity.isLauncherActivity() && launcherActivity == null){
                    launcherActivity = activity;
                }
                item = activity;
                break;
            case "activity-alias":
                item = new ActivityAlias().parse(node);
                break;
            case "service":
                item = new Service().parse(node);
                break;
            case "receiver":
                item = new Receiver().parse(node);
                break;
            case "provider":
                item = new Provider().parse(node);
                break;
            case "meta-data":
                item = new MetaData().parse(node);
                break;
            default:
                item = super.createReadChildren(node);
                break;
        }
        return item;
    }

    @Override
    protected String getAttrFilter() {
        return "remove-android-application-attr.json";
    }
}
