package com.asset.management.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ResultSet {
    private int code;
    private String msg;
    private Object data;
    private Boolean success;
    private String token;
    private Long time;
}
