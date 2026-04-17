package com.mock.example.common.component.strategy;

/**
 * 策略模式基础接口
 *
 * @author: Mock
 * @date: 2022-09-13 21:29:03
 */
public interface TypedStrategy<T extends Enum<T>> {

    /**
     * 获取策略对应的枚举类型
     * <strong>每种策略模式只能对应一种具体的枚举值</strong>
     * <strong>不同的策略实现类，其返回的枚举值不应相同</strong>
     * {@link AbstractStrategyFactory#afterPropertiesSet()} 中主要对各种策略实现进行注册
     * 注册的机制暂时为 以方法返回枚举 type为key，枚举类型bean为值
     *
     *
     * @return 获取策略对应的枚举类型
     */
    T getStrategyType();

}

  