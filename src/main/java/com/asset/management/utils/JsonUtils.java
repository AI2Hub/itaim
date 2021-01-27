package com.asset.management.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.commons.lang3.StringUtils;
import org.apache.maven.surefire.shade.org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);
    private  static ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> T serializable(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public static <T> T serializable(String json, TypeReference reference) {

        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            return (T) mapper.readValue(json, reference);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return  null;
        }

    }

    public static String deserializer(Object json) {
        if (json == null) {
            return null;
        }
        try {
            return mapper.writeValueAsString(json);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
            return null;
        }

    }
}
