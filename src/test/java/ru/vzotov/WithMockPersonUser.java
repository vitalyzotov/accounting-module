package ru.vzotov;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockPersonUserSecurityContextFactory.class)
public @interface WithMockPersonUser {
    String value() default "user";
    String[] roles() default { "USER" };
    String password() default "password";
    String person();
}
