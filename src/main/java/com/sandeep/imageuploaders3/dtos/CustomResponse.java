package com.sandeep.imageuploaders3.dtos;

import lombok.Getter;

@Getter
public class CustomResponse {
    private String message;
    private String detail;

    public CustomResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public CustomResponse setDetail(String detail) {
        this.detail = detail;
        return this;
    }

    public static CustomResponse build() {
        return new CustomResponse();
    }

}
