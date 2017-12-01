/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.ketal.validation.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.github.ketal.validation.constraints.Password.List;
import com.github.ketal.validation.constraintvalidators.PasswordValidator;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(List.class)
@Documented
@Constraint(validatedBy = { PasswordValidator.class })
public @interface Password {

    String message() default "{com.github.ketal.validation.constraints.Password.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return length the password must be higher or equal to
     */
    int minLength() default 0;

    /**
     * @return length the password must be lower or equal to
     */
    int maxLength() default Integer.MAX_VALUE;

    /**
     * @return The password must contain a digit
     */
    boolean requireDigit() default false;

    /**
     * @return The password must contain a lower case character
     */
    boolean requireLowerCase() default false;

    /**
     * @return The password must contain a upper case character
     */
    boolean requireUpperCase() default false;

    /**
     * @return The password may contain white space. Default is false
     */
    boolean allowWhiteSpace() default false;

    /**
     * @return The password must contain one of the specified special character
     */
    String specialCharacters() default "";

    /**
     * Defines several {@link Password} annotations on the same element.
     *
     * @see Password
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    @interface List {

        Password[] value();
    }

}
