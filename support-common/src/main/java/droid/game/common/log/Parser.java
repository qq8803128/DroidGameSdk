package droid.game.common.log;

/**
 * Created by pengwei on 16/3/8.
 * 类解析接口
 */
public interface Parser<T> {

    // 换行符
    String LINE_SEPARATOR = System.getProperty("line.separator");

    Class<T> parseClassType();

    String parseString( T t);
}
