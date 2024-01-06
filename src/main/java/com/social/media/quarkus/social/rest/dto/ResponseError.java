package com.social.media.quarkus.social.rest.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.ConstraintViolation;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
public class ResponseError {
    private String message;
    private Collection<FieldError> errors;

    public ResponseError(String message, Collection<FieldError> fieldErrorList) {
        this.message = message;
        this.errors = fieldErrorList;
    }

    public static <T> ResponseError createFromValidation(Set<ConstraintViolation<T>> violations) {
        List<FieldError> fieldErrorList = violations.stream().map(cv -> new FieldError(cv.getPropertyPath().toString(), cv.getMessage()))
                .collect(Collectors.toList());

        String message = "Validation Error";

        return new ResponseError(message, fieldErrorList);
    }

}
