package droid.game.common.util;

import java.io.Closeable;
import java.io.IOException;

public class UILangHelper {
    public static void close(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
