package com.mock.example.common.component.strategy;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 策略工厂
 *
 * @author: Mock
 * @date: 2022-09-13 07:10:02
 */
public abstract class AbstractStrategyFactory<S extends TypedStrategy<T>, T extends Enum<T>> implements InitializingBean {

    /**
     * 策略类实现bean，通过spring自动管理注入
     */
    @Autowired
    private List<S> strategyCandidates;

    private Map<T, S> strategyCandidateMap;

    /**
     * 根据策略枚举类型，获取具体的策略
     *
     * @param strategyType 策略类型
     * @return 具体策略，<strong>结果可能为null</strong>
     */
    public S getStrategy(T strategyType) {
        return strategyCandidateMap.get(strategyType);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!CollectionUtils.isEmpty(strategyCandidates)) {
            Map<T, S> strategyMap = strategyCandidateMap = strategyCandidates.stream()
                    .filter(e -> e.getStrategyType() != null)
                    .collect(Collectors.toMap(TypedStrategy::getStrategyType, e -> e));
            strategyCandidateMap = Collections.unmodifiableMap(strategyMap);
        } else {
            strategyCandidateMap = Collections.emptyMap();
        }
    }
}