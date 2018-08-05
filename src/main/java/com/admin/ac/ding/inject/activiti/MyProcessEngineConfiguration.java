package com.admin.ac.ding.inject.activiti;

import org.activiti.engine.impl.persistence.StrongUuidGenerator;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyProcessEngineConfiguration implements ProcessEngineConfigurationConfigurer {

    @Override
    public void configure(SpringProcessEngineConfiguration processEngineConfiguration) {
        System.out.println("set SpringProcessEngineConfiguration extra");
        processEngineConfiguration.setIdGenerator(new StrongUuidGenerator());
    }
}
