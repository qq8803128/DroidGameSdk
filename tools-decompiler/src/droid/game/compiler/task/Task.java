package droid.game.compiler.task;

import droid.game.compiler.config.Project;
import io.reactivex.functions.Action;

public abstract class Task {
    Project temp;
    Project dest;
    Logger logger;

    public abstract void doFirst(Action action);
    public abstract void doLast(Action action);

    public void input(Object... args){
        temp = (Project) args[0];
        dest = (Project) args[1];
    }

    public void callFirst(){
        doFirst(new Action() {
            @Override
            public void run() throws Exception {

            }
        });
    }

    public void callLast() {
        doLast(new Action(){
            @Override
            public void run() throws Exception {

            }
        });
    }

    protected void log(String message){
        if (logger != null){
            logger.log(message);
        }
    }

    public void setLogger(Logger logger){
        this.logger = logger;
    }

    protected Project getTemp(){
        return temp;
    }

    protected Project getDest(){
        return dest;
    }

    public static interface Logger{
        void log(String message);
    }
}
