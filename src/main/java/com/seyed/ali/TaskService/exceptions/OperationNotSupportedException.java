package com.seyed.ali.TaskService.exceptions;

import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public class OperationNotSupportedException extends RuntimeException {

    private String message;

    public OperationNotSupportedException() {
    }

    public OperationNotSupportedException(String message) {
        super(message);
        this.message = message;
    }

}
