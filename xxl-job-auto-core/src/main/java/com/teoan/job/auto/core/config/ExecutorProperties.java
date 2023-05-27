package com.teoan.job.auto.core.config;

import lombok.Data;

/**
 * @author Teoan
 * @since 2023/5/25 21:46
 */
@Data
public class ExecutorProperties {

    /**
     * 在addressType为1的情况下，手动录入执行器地址列表，多地址逗号分隔
     */
    String addressList;

    /**
     * 执行器地址类型
     */
    Integer addressType = 0;

    /**
     * 执行器名称
     */
    String title;

    /**
     * 应用名称
     */
    String appname;
}
