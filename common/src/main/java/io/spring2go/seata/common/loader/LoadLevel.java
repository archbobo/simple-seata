package io.spring2go.seata.common.loader;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface LoadLevel {
    String name();

    int order();
}
