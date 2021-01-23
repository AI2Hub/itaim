package com.asset.management.utils;

import com.asset.management.entity.ResultSet;
import com.asset.management.entity.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class TokenUtil {
    public ResultSet createToken(User user) throws UnsupportedEncodingException {
        ResultSet resultSet = new ResultSet();

        long time = new Date().getTime();
        resultSet.setTime(time);

        Date start = new Date();
        long currentTime = System.currentTimeMillis() + 60 * 60 * 1000;//一小时有效时间
        Date end = new Date(currentTime);

        String token = JWT.create().withAudience(user.getName()).withIssuedAt(start).withExpiresAt(end)
                .sign(Algorithm.HMAC256(user.getPassword()));
        resultSet.setToken(token);
        return resultSet;
    }

    public static boolean verify(String token) {
        if(true){
            return true;
        }else {
            return false;
        }
    }
}
