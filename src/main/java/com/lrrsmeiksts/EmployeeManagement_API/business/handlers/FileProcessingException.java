package com.lrrsmeiksts.EmployeeManagement_API.business.handlers;

import java.io.IOException;

public class FileProcessingException extends IOException {
    public FileProcessingException(String message, Throwable cause){
        super(message, cause);
    }
}
