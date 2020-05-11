package droid.game.annotation;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.CLASS;

@Retention(CLASS)
public @interface VisibleForTesting {
    /**
     * The visibility the annotated element would have if it did not need to be made visible for
     * testing.
     */
    //@ProductionVisibility
    int otherwise() default PRIVATE;

    /**
     * The annotated element would have "private" visibility
     */
    int PRIVATE = 2; // Happens to be the same as Modifier.PRIVATE

    /**
     * The annotated element would have "package private" visibility
     */
    int PACKAGE_PRIVATE = 3;

    /**
     * The annotated element would have "protected" visibility
     */
    int PROTECTED = 4; // Happens to be the same as Modifier.PROTECTED

    /**
     * The annotated element should never be called from production code, only from tests.
     * <p>
     * This is equivalent to {@code @RestrictTo.Scope.TESTS}.
     */
    int NONE = 5;
}
