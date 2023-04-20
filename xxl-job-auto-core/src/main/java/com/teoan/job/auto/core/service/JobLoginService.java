package com.teoan.job.auto.core.service;

/**
 * @author: Teoan
 * @createTime: 2023/04/07 14:28
 * @description: xxl-job任务登陆操作服务类
 */
public interface JobLoginService {

    /**
     * 登陆
     */
    void login();

    /**
     * 获取Cookie
     * @return Cookie
     */
    String getCookie();
}
