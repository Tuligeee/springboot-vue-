package com.mock.example.common.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 导入进度上下文工具类
 */
public class ImportProgressContext {
    
    // 存储任务进度 (taskId -> percentage 0-100)
    private static final Map<String, Integer> progressMap = new ConcurrentHashMap<>();
    
    // 存储任务最终消息 (taskId -> message)
    private static final Map<String, String> resultMap = new ConcurrentHashMap<>();

    // 存储任务错误列表 (taskId -> [errorMsg1, errorMsg2...])
    private static final Map<String, java.util.List<String>> errorListMap = new ConcurrentHashMap<>();

    public static void setProgress(String taskId, int progress) {
        progressMap.put(taskId, progress);
    }

    public static Integer getProgress(String taskId) {
        return progressMap.getOrDefault(taskId, 0);
    }

    public static void setResult(String taskId, String message) {
        resultMap.put(taskId, message);
        progressMap.put(taskId, 100); // 标记完成
    }

    public static String getResult(String taskId) {
        return resultMap.get(taskId);
    }

    public static void addError(String taskId, String errorMsg) {
        errorListMap.computeIfAbsent(taskId, k -> new java.util.concurrent.CopyOnWriteArrayList<>()).add(errorMsg);
    }

    public static java.util.List<String> getErrorList(String taskId) {
        return errorListMap.get(taskId);
    }

    public static void clear(String taskId) {
        progressMap.remove(taskId);
        resultMap.remove(taskId);
        errorListMap.remove(taskId);
    }
}
