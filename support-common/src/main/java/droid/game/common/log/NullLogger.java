package droid.game.common.log;

public class NullLogger implements Printer{
    @Override
    public void d(String message, Object... args) {

    }

    @Override
    public void d(Object object) {

    }

    @Override
    public void e(String message, Object... args) {

    }

    @Override
    public void e(Object object) {

    }

    @Override
    public void w(String message, Object... args) {

    }

    @Override
    public void w(Object object) {

    }

    @Override
    public void i(String message, Object... args) {

    }

    @Override
    public void i(Object object) {

    }

    @Override
    public void v(String message, Object... args) {

    }

    @Override
    public void v(Object object) {

    }

    @Override
    public void wtf(String message, Object... args) {

    }

    @Override
    public void wtf(Object object) {

    }

    @Override
    public void json(String json) {

    }

    @Override
    public void xml(String xml) {

    }

    @Override
    public Printer setTag(String tag) {
        return this;
    }
}
