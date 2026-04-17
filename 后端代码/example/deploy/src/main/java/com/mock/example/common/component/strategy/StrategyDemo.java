package com.mock.example.common.component.strategy;

import com.mock.example.common.component.strategy.demo.enums.StrategyInterfaceEnum;
import com.mock.example.common.component.strategy.demo.factory.StrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * demo
 * <p>
 * 策略模式使用:
 * 1.定义枚举 {@link StrategyInterfaceEnum}
 * 2.定义策略接口 {@link com.bjw.web.component.strategy.impl.StrategyInterface}
 * 3.定义策略接口实现类 {@link com.bjw.web.component.strategy.impl.StrategyA}
 * 4.定义策略工厂 {@link StrategyFactory}
 *
 * @author: Mock
 * @date: 2022-09-13 21:48:07
 */
@Component
@RequiredArgsConstructor
public class StrategyDemo {

    private final StrategyFactory strategyFactory;

    public void execute() {
        //执行策略A
        strategyFactory.getStrategy(StrategyInterfaceEnum.INTERFACE_A).method();
        //执行策略B
        strategyFactory.getStrategy(StrategyInterfaceEnum.INTERFACE_B).method();
    }

}

  