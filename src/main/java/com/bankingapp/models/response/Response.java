package com.bankingapp.models.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Response {
    private Status status;
    private Object output;
    private String errorMessage;

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", output=" + output +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
