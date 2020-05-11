package droid.game.common.log;

import android.text.TextUtils;

import droid.game.common.log.file.LogFileEngine;
import droid.game.common.log.file.LogFileFilter;
import droid.game.common.log.pattern.LogPattern;

import java.io.File;
import static droid.game.common.log.LogLevel.LogLevelType;

/**
 * Created by pengwei on 2017/3/30.
 */

class Log2FileConfigImpl implements Log2FileConfig {

    private static final String DEFAULT_LOG_NAME_FORMAT = "%d{yyyyMMdd}.txt";

    private LogFileEngine engine;
    private LogFileFilter fileFilter;
    private @LogLevelType int logLevel = LogLevel.TYPE_VERBOSE;
    private boolean enable = false;
    private String logFormatName = DEFAULT_LOG_NAME_FORMAT;
    private String logPath;
    private static Log2FileConfigImpl singleton;
    private String customFormatName;

    static Log2FileConfigImpl getInstance() {
        if (singleton == null) {
            synchronized (Log2FileConfigImpl.class) {
                if (singleton == null) {
                    singleton = new Log2FileConfigImpl();
                }
            }
        }
        return singleton;
    }

    @Override
    public Log2FileConfig configLog2FileEnable(boolean enable) {
        this.enable = enable;
        return this;
    }

    boolean isEnable() {
        return enable;
    }

    @Override
    public Log2FileConfig configLog2FilePath(String logPath) {
        this.logPath = logPath;
        return this;
    }

    /**
     * 获取日志路径
     * @return 日志路径
     */
    String getLogPath() {
        if (TextUtils.isEmpty(logPath)) {
            throw new RuntimeException("Log File Path must not be empty");
        }
        File file = new File(logPath);
        if (file.exists() || file.mkdirs()) {
            return logPath;
        }
        throw new RuntimeException("Log File Path is invalid or no sdcard permission");
    }

    @Override
    public Log2FileConfig configLog2FileNameFormat(String formatName) {
        if (!TextUtils.isEmpty(formatName)) {
            this.logFormatName = formatName;
        }
        return this;
    }

    String getLogFormatName() {
        if (customFormatName == null) {
            customFormatName = new LogPattern.Log2FileNamePattern(logFormatName).doApply();
        }
        return customFormatName;
    }

    @Override
    public Log2FileConfig configLog2FileLevel(@LogLevelType int level) {
        this.logLevel = level;
        return this;
    }

    int getLogLevel() {
        return logLevel;
    }

    @Override
    public Log2FileConfig configLogFileEngine(LogFileEngine engine) {
        this.engine = engine;
        return this;
    }

    @Override
    public Log2FileConfig configLogFileFilter(LogFileFilter fileFilter) {
        this.fileFilter = fileFilter;
        return this;
    }

    @Override
    public File getLogFile() {
        String path = getLogPath();
        if (!TextUtils.isEmpty(path)) {
            return new File(path, getLogFormatName());
        }
        return null;
    }

    @Override
    public void flushAsync() {
        if (engine != null) {
            engine.flushAsync();
        }
    }

    @Override
    public void release() {
        if (engine != null) {
            engine.release();
        }
    }

    LogFileFilter getFileFilter() {
        return fileFilter;
    }

    LogFileEngine getEngine() {
        return engine;
    }
}
