package com.mock.example.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

/**
 * 基于ObjectMapper实现的json工具
 *
 * @author: Mock
 * @date: 2022-10-17 23:07:40
 */
@Slf4j
public final class JsonUtil {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final ObjectMapper om = new ObjectMapper();

    static {
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        om.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        om.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        om.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false);
        om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));
        om.registerModule(javaTimeModule);
        om.setTimeZone(TimeZone.getDefault());
    }

    public static JavaType makeJavaType(Class<?> parametrized, Class<?>... parameterClasses) {
        return om.getTypeFactory().constructParametricType(parametrized, parameterClasses);
    }

    public static JavaType makeJavaType(Class<?> rawType, JavaType... parameterTypes) {
        return om.getTypeFactory().constructParametricType(rawType, parameterTypes);
    }

    public static String toString(Object value) {
        if (Objects.isNull(value)) {
            return null;
        }
        if (value instanceof String) {
            return (String) value;
        }
        return toJSONString(value);
    }

    public static String toJSONString(Object value) {
        String result = null;
        try {
            result = om.writeValueAsString(value);
        } catch (IOException e) {
            log.error("Jackson ObjectMapper writeValueAsString exception : ", e);
        }
        return result;
    }

    public static String toPrettyString(Object value) {
        String result = null;
        try {
            result = om.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (IOException e) {
            log.error("Jackson ObjectMapper writeValueAsString exception : ", e);
        }
        return result;
    }

    public static JsonNode fromObject(Object value) {
        JsonNode result = null;
        try {
            if (Objects.nonNull(value) && (value instanceof String)) {
                result = parseObject((String) value);
            } else {
                result = om.valueToTree(value);
            }
        } catch (IllegalArgumentException e) {
            log.error("Jackson ObjectMapper valueToTree exception : ", e);
        }
        return result;
    }

    public static JsonNode parseObject(String content) {
        JsonNode result = null;
        try {
            result = om.readTree(content);
        } catch (IOException e) {
            log.error("Jackson ObjectMapper readTree exception : ", e);
        }
        return result;
    }

    public static <T> T toJavaObject(TreeNode node, Class<T> clazz) {
        T result = null;
        try {
            result = om.treeToValue(node, clazz);
        } catch (IOException e) {
            log.error("Jackson ObjectMapper treeToValue exception : ", e);
        }
        return result;
    }

    public static <T> T toJavaObject(String content, Class<T> clazz) {
        T result = null;
        try {
            result = om.readValue(content, clazz);
        } catch (IOException e) {
            log.error("Jackson ObjectMapper readValue exception : ", e);
        }
        return result;
    }

    public static <T> T toJavaObject(String content, JavaType javaType) {
        T result = null;
        try {
            result = om.readValue(content, javaType);
        } catch (IOException e) {
            log.error("Jackson ObjectMapper readValue exception : ", e);
        }
        return result;
    }

    public static <T> T toJavaObject(String content, TypeReference<T> typeReference) {
        T result = null;
        try {
            result = om.readValue(content, typeReference);
        } catch (IOException e) {
            log.error("Jackson ObjectMapper readValue exception : ", e);
        }
        return result;
    }

    public static <T> T toJavaObject(String content, ParameterizedType parameterizedType) {
        return toJavaObject(content, new TypeReference<T>() {
            public Type getType() {
                return parameterizedType;
            }
        });
    }

    public static <E> List<E> toJavaList(String content, Class<E> clazz) {
        return toJavaObject(content, makeJavaType(List.class, clazz));
    }

    public static List<Object> toJavaList(String content) {
        return toJavaObject(content, new TypeReference<List<Object>>() {
        });
    }

    public static <V> Map<String, V> toJavaMap(String content, Class<V> clazz) {
        return toJavaObject(content, makeJavaType(Map.class, String.class, clazz));
    }

    public static Map<String, Object> toJavaMap(String content) {
        return toJavaObject(content, new TypeReference<Map<String, Object>>() {
        });
    }

    public static void main(String[] args) {

    }

}