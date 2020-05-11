package droid.game.gradle.extension;


import java.util.HashMap;
import java.util.Map;

public class Aapt3Extension {
    public Map<String,String> rule = new HashMap<>();

    public Map<String,String> getRule(){
        return rule;
    }

    public void setRule(Map<String,String> rule){
        if (rule != null) {
            this.rule.putAll(rule);
        }
    }
}
