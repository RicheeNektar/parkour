package org.Richee.Commands;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface SubCommandExecutor {
    String name();
    String[] alias() default {};
}
