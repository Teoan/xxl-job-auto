package com.teoan.job.auto.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @author Teoan
 * @since 2023/5/25 21:30
 */
@Configuration
@ConfigurationProperties(prefix = XxlJobAutoConfigProperties.PREFIX)
@Data
public class XxlJobAutoConfigProperties {

    public static final String PREFIX = "xxl.job";

    @NestedConfigurationProperty
    AdminProperties admin;

    @NestedConfigurationProperty
    ExecutorProperties executor;

}
