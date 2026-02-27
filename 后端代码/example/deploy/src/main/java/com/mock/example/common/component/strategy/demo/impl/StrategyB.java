package com.mock.example.common.component.strategy.demo.impl;

import com.mock.example.common.component.strategy.demo.enums.StrategyInterfaceEnum;
import org.springframework.stereotype.Component;

/**
 * 策略B实现
 *
 * @author: Mock
 * @date: 2022-09-13 21:38:39
 */
@Component
public class StrategyB implements StrategyInterface{

    @Override
    public StrategyInterfaceEnum getStrategyType() {
        return StrategyInterfaceEnum.INTERFACE_B;
    }

    @Override
    public void method() {
       System.out.println("执行策略B~");
    }
}

  