package droid.game.compiler.ui;

import javafx.scene.control.RadioButton;

public class ToggleButton extends RadioButton {
    private Object tag;
    public void setTag(Object tag){
        this.tag = tag;
    }

    public Object getTag(){
        return tag;
    }
}
