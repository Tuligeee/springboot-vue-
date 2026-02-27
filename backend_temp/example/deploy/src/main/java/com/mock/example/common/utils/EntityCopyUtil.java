package com.mock.example.common.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import com.mock.example.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;

/**
 * 对象属性copy工具
 *
 * @author: Mock
 * @date: 2025-01-10 19:44:34
 */
@Slf4j
public class EntityCopyUtil {

    /**
     * 对象属性拷贝
     *
     * @param targetClazz 拷贝对象class类型
     * @param source      属性来源对象
     * @param <T>         拷贝对象泛型
     * @return 拷贝完属性后对象
     */
    public static <T> T copyEntity(Class<T> targetClazz, Object source) {
        Object instance;
        try {
            instance = ReflectUtil.newInstance(targetClazz);
        } catch (Exception e) {
            log.error("[copyEntity] 反射异常", e);
            throw new BizException("[EntityCopyUtil.copyEntity] error");
        }

        BeanUtil.copyProperties(source, instance);

        return (T) instance;
    }
}

  