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
package com.github.ketal.cornerstone.validation.constraintvalidators;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.github.ketal.cornerstone.validation.constraints.Password;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    private static final Pattern DIGIT_REGEX = Pattern.compile("[0-9]");
    private static final Pattern LOWER_CASE_REGEX = Pattern.compile("[a-z]");
    private static final Pattern UPPER_CASE_REGEX = Pattern.compile("[A-Z]");
    private static final Pattern WHITE_SPACE_REGEX = Pattern.compile("\\s");

    private int minLength;
    private int maxLength;
    private boolean requireDigit;
    private boolean requireLowerCase;
    private boolean requireUpperCase;
    private boolean allowWhiteSpace;
    private String specialCharacters;

    @Override
    public void initialize(Password password) {
        this.minLength = password.minLength();
        this.maxLength = password.maxLength();
        this.requireDigit = password.requireDigit();
        this.requireLowerCase = password.requireLowerCase();
        this.requireUpperCase = password.requireUpperCase();
        this.allowWhiteSpace = password.allowWhiteSpace();
        this.specialCharacters = password.specialCharacters();
        
        validateParameters();
    }

    @Override
    public boolean isValid(String charSequence, ConstraintValidatorContext constraintValidatorContext) {
        if (charSequence == null) {
            return true;
        }
        
        boolean isValid = true;
        StringBuilder error = new StringBuilder();

        if (charSequence.length() < this.minLength) {
            isValid = false;
            error.append("Min length must be " + this.minLength + ". ");
        }

        if (charSequence.length() > this.maxLength) {
            isValid = false;
            error.append("Max length allowed is " + this.maxLength + ". ");
        }

        if (this.requireDigit && !DIGIT_REGEX.matcher(charSequence).find()) {
            isValid = false;
            error.append("At least one digit is required. ");
        }

        if (this.requireLowerCase && !LOWER_CASE_REGEX.matcher(charSequence).find()) {
            isValid = false;
            error.append("At least one lower case character is required. ");
        }

        if (this.requireUpperCase && !UPPER_CASE_REGEX.matcher(charSequence).find()) {
            isValid = false;
            error.append("At least one upper case character is required. ");
        }

        if (!this.allowWhiteSpace && WHITE_SPACE_REGEX.matcher(charSequence).find()) {
            isValid = false;
            error.append("White space is not allowed. ");
        }

        if (this.specialCharacters != null && !this.specialCharacters.trim().isEmpty()) {
            Pattern specialCharacterRegex = Pattern.compile("[" + this.specialCharacters.trim() + "]");
            if (!specialCharacterRegex.matcher(charSequence).find()) {
                isValid = false;
                error.append("At least one special character is required. Allowed special characters: " + this.specialCharacters);
            }
        }
        
        if ( !isValid ) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    "{com.github.ketal.validation.constraints.Password.message} " +
                    error.toString()
            )
            .addConstraintViolation();
        }
        
        return isValid;
    }

    private void validateParameters() {
        if ( minLength < 0 ) {
            throw new IllegalArgumentException("The minLength cannot be negative.");
        }
        if ( maxLength < 0 ) {
            throw new IllegalArgumentException("The maxLength cannot be negative.");
        }
        if ( maxLength < minLength ) {
            throw new IllegalArgumentException("The maxLength cannot be less than minLength.");
        }
    }
}
