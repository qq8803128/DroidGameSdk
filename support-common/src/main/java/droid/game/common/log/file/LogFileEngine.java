package droid.game.common.log.file;

import java.io.File;

/**
 * Created by pengwei on 2017/3/30.
 */

public interface LogFileEngine {
    void writeToFile(File logFile, String logContent, LogFileParam params);
    void flushAsync();
    void release();
}
