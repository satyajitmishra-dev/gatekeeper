package me.satyajit.gatekeeper.management.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException{

    private final HttpStatus httpStatus;

    public ApiException(HttpStatus status, String message){

        this.httpStatus = status;
        super(message);
    }

    public HttpStatus getHttpStatus(){

        return httpStatus;
    }

}
