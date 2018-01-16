package monitorx.plugins.annotation;

import java.lang.annotation.*;

/**
 * @author qianlifeng
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SyncConfig {
    String code();

    String name();

    String description();

    String type() default "text";
}
