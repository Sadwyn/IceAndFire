package com.sadwyn.iceandfire;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Sadwyn on 07.04.2017.
 */

public interface ContextAnnotation {
    @Qualifier
    @Retention(RUNTIME)
    public @interface ForApplication {
    }
}
