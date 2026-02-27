package com.mock.example.common.component.strategy.demo.factory;

import com.mock.example.common.component.strategy.AbstractStrategyFactory;
import com.mock.example.common.component.strategy.demo.enums.StrategyInterfaceEnum;
import com.mock.example.common.component.strategy.demo.impl.StrategyInterface;
import org.springframework.stereotype.Component;

/**
 * 策略实现工厂
 *
 * @author: Mock
 * @date: 2022-09-13 21:43:48
 */
@Component
public class StrategyFactory extends AbstractStrategyFactory<StrategyInterface, StrategyInterfaceEnum> {

}

  