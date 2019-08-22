package com.santex.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.santex.enums.ErrorCodes;
import com.santex.exceptions.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpStatus;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;

@Slf4j
public class MapperUtils {

    private static ObjectMapper defaultMapper;

    static {
        defaultMapper = new ObjectMapper();
        defaultMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        defaultMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        defaultMapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
        defaultMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    public static String toJsonString(Object object) {
        return toJsonString(defaultMapper, object);
    }

    public static String toJsonString(@Nonnull ObjectMapper customMapper, Object object) {
        try {
            return customMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error(String.format("[message: Invalid json string. Cannot convert object to string] [method: toJsonNode] [class: MapperUtils] [called from: %s] [object: %s]",
                    Thread.currentThread().getStackTrace()[2].toString(), object.toString()));
            throw new ApiException(ErrorCodes.internal_error.toString(), "internal_error", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
        }
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        return toObject(defaultMapper, json, clazz);
    }

    public static <T> T toObject(@Nonnull ObjectMapper customMapper, String json, Class<T> clazz) {
        try {
            return customMapper.readValue(json, clazz);
        } catch (IOException e) {
            logger.error(String.format("[message: Invalid json string. Cannot convert jsonString to object class] [method: toObject] [class: MapperUtils] [called from: %s] [object: %s]",
                    Thread.currentThread().getStackTrace()[2].toString(), json));
            throw new ApiException(ErrorCodes.internal_error.toString(), "Error parse to object class", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
        }
    }

    public static <T> List<T> toObjectList(String json, Class<T> clazz) {
        return toObjectList(defaultMapper, json, clazz);
    }

    public static <T> List<T> toObjectList(@Nonnull ObjectMapper customMapper, String json, Class<T> clazz) {
        try {
            return customMapper.readValue(json, new TypeReference<List<T>>(){});
        } catch (IOException e) {
            logger.error(String.format("[message: Invalid json string. Cannot convert string to list] [method: toList] [class: MapperUtils] [called from: %s] [object: %s]",
                    Thread.currentThread().getStackTrace()[2].toString(), json));
            throw new ApiException(ErrorCodes.internal_error.toString(), "Error parse to list", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
        }
    }
}