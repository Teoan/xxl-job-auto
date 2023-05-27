package com.teoan.job.auto.core.config;

import lombok.Data;

/**
 * @author Teoan
 * @since 2023/5/25 21:43
 */
@Data
public class AdminProperties {

    /**
     *  用户名
     */
    String username;

    /**
     * 密码
     */
    String password;

    /**
     * 调度中心地址
     */
    String addresses;
}
