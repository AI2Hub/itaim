package com.asset.management.entity;

import lombok.Data;

import java.util.Date;
import java.util.Random;

@Data
public class ResultSet {
    private int code;
    private String msg;
    private Object data;
    private Boolean success;
    private String token;
    private String tokenToOa;
    private Long time;
    private String random;
}
