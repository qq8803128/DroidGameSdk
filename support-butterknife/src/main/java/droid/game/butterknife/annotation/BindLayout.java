package droid.game.butterknife.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Created by admin on 2018/6/4.
 */
@Retention(CLASS) @Target(FIELD)
public @interface BindLayout {
    String value() default "";
}