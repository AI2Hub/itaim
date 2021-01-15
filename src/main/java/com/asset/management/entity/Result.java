package com.asset.management.entity;

import lombok.Data;

@Data
public class Result<T> {
    private int code;
    private String msg;
    private T data;
    private Boolean success;

    public Result() {
    }

    public Result(T data) {
        this.data = data;
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
