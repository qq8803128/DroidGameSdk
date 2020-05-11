package droid.game.compiler.xml;

import droid.game.compiler.util.ReplaceKit;

public class Provider extends Item{
    @Override
    public String[] createWriteAttributes(String key, String value) {
        if (key.equals("android:authorities")){
            try {
                if (value.contains(getPackageName())) {
                    String newValue = ReplaceKit.Companion.r(value, getPackageName(), "${applicationId}");
                    return new String[]{key, newValue};
                } else {
                    return new String[]{key, "${applicationId}." + value};
                }
            }catch (Throwable e){
                e.printStackTrace();
            }
        }

        return super.createWriteAttributes(key, value);
    }

    @Override
    protected String getNameFilter() {
        return "remove-android-provider.json";
    }
}
