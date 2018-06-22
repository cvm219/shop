package com.shop.aggregator.exceptions;

public class RestException extends Exception {
    
    private int statusCode;
    private String description;

    public RestException(int statusCode, String description) {
        this.statusCode = statusCode;
        this.description = description;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getDescription() {
        return description;
    }

}
