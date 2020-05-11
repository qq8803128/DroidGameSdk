package droid.game.common.log;



/**
 * Created by pengwei08 on 2015/7/16.
 * 日志管理器
 */
public final class LogRecord {

    private static Printer printer = new Logger();
    private static LogConfigImpl logConfig = LogConfigImpl.getInstance();
    private static Log2FileConfigImpl log2FileConfig = Log2FileConfigImpl.getInstance();

    static {

    }

    /**
     * 选项配置
     *
     * @return LogConfig
     */
    public static LogConfig getLogConfig() {
        return logConfig;
    }

    /**
     * 日志写入文件相关配置
     *
     * @return LogConfig
     */
    public static Log2FileConfig getLog2FileConfig() {
        return log2FileConfig;
    }

    public static Printer tag(String tag) {
        return printer.setTag(tag);
    }

    /**
     * verbose输出
     *
     * @param msg
     * @param args
     */
    public static void v(String msg, Object... args) {
        try {
            printer.v(msg, args);
        } catch (Throwable e) {

        }
    }

    public static void v(Object object) {
        try {
            printer.v(object);
        } catch (Throwable e) {

        }
    }


    /**
     * debug输出
     *
     * @param msg
     * @param args
     */
    public static void d(String msg, Object... args) {
        try {
            printer.d(msg, args);
        } catch (Throwable e) {

        }
    }

    public static void d(Object object) {
        try {
            printer.d(object);
        } catch (Throwable e) {

        }
    }

    /**
     * info输出
     *
     * @param msg
     * @param args
     */
    public static void i(String msg, Object... args) {

        try {
            printer.i(msg, args);
        } catch (Throwable e) {

        }
    }

    public static void i(Object object) {
        try {
            printer.i(object);
        } catch (Throwable e) {

        }
    }

    /**
     * warn输出
     *
     * @param msg
     * @param args
     */
    public static void w(String msg, Object... args) {
        try {
            printer.w(msg, args);
        } catch (Throwable e) {

        }
    }

    public static void w(Object object) {
        try {
            printer.w(object);
        } catch (Throwable e) {

        }
    }

    /**
     * error输出
     *
     * @param msg
     * @param args
     */
    public static void e(String msg, Object... args) {
        try {
            printer.e(msg, args);
        } catch (Throwable e) {

        }
    }

    public static void currentThread(){
        try{
            printer.d(Thread.currentThread().toString());
        }catch (Throwable e){

        }
    }

    public static void e(Object object) {
        try {
            printer.e(object);
        } catch (Throwable e) {

        }
    }

    /**
     * assert输出
     *
     * @param msg
     * @param args
     */
    public static void wtf(String msg, Object... args) {
        try {
            printer.wtf(msg, args);
        } catch (Throwable e) {

        }
    }

    public static void wtf(Object object) {
        try {
            printer.wtf(object);
        } catch (Throwable e) {

        }
    }

    /**
     * 打印json
     *
     * @param json
     */
    public static void json(String json) {
        try {
            printer.json(json);
        } catch (Throwable e) {

        }
    }

    /**
     * 输出xml
     *
     * @param xml
     */
    public static void xml(String xml) {
        try {
            printer.xml(xml);
        } catch (Throwable e) {

        }
    }
}
