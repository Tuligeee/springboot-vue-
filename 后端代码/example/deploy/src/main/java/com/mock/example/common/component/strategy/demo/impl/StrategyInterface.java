package com.mock.example.common.component.strategy.demo.impl;


import com.mock.example.common.component.strategy.TypedStrategy;
import com.mock.example.common.component.strategy.demo.enums.StrategyInterfaceEnum;

/**
 * 策略实现接口
 *
 * @author: Mock
 * @date: 2022-09-13 21:36:48
 */
public interface StrategyInterface extends TypedStrategy<StrategyInterfaceEnum> {

    /**
     * 策略需实现方法
     */
    void method();
}

  