package com.social.media.rest.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FieldError {

    private String field;
    private String message;

    public FieldError(String field, String message) {
        this.field = field;
        this.message = message;
    }

}
