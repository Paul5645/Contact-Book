package com.example.contactbook.exceptions;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AppError {
    private int status;
    private String message;
    private Date timestamp;

    public AppError(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }

}

