package com.teoan.job.auto.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 自动装配类
 *
 * @author zhuangjy
 * @since 2023/04/18 10:18
 */
@Configuration
@ConditionalOnProperty(name = "xxl.job.enable",havingValue = "true")
@ComponentScan("com.teoan.job.auto.core")
@EnableScheduling
public class XxlJobAutoConfiguration {
}
