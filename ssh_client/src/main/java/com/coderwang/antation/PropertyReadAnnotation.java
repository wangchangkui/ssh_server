package com.coderwang.antation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(value = {ElementType.FIELD})
public @interface PropertyReadAnnotation {


    String value();
}
