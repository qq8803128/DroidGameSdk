package droid.game.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.RetentionPolicy.CLASS;

@Retention(CLASS)
@Target({TYPE, METHOD, CONSTRUCTOR, FIELD, PACKAGE})
public @interface RequiresApi {
    /**
     * The API level to require. Alias for {@link #api} which allows you to leave out the {@code
     * api=} part.
     */
    @IntRange(from = 1)
    int value() default 1;

    /** The API level to require */
    @IntRange(from = 1)
    int api() default 1;
}
