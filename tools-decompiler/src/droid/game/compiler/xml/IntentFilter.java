package droid.game.compiler.xml;

public class IntentFilter extends Item{
    boolean isLauncherActivity(){
        boolean isMain = false;
        boolean isLauncher = false;

        for (Item item : child){
            if (item.name.equals("action") && item.androidName.equals("android.intent.action.MAIN")){
                isMain = true;
            }

            if (item.name.equals("category") && item.androidName.equals("android.intent.category.LAUNCHER")){
                isLauncher = true;
            }
        }
        return isMain && isLauncher;
    }
}
