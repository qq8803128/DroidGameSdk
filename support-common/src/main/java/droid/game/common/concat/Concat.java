package droid.game.common.concat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static droid.game.common.util.UIThreadHelper.runNow;

public final class Concat {

    private List<Function> serials = new ArrayList<>();

    public static Concat create() {
        return new Concat();
    }

    private Concat() {
        super();
    }

    public final Concat concat(Function... serials) {
        if (serials != null) {
            this.serials.addAll(Arrays.asList(serials));
        }
        return this;
    }

    public final Concat next() {
        runNow(new Runnable() {
            @Override
            public void run() {
                if (serials.size() > 0) {
                    Function serial = serials.get(0);
                    serials.remove(0);
                    try {
                        serial.run(Concat.this);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return this;
    }

    public interface Function {
        void run(Concat concat);
    }
}
