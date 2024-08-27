package com.lrrsmeiksts.EmployeeManagement_API.business.handlers;

public class GeneralDatabaseException extends RuntimeException{
    public GeneralDatabaseException(String message, Throwable cause){
        super(message, cause);
    }
}
