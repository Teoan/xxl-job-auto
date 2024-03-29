package com.teoan.job.auto.core.service.impl;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.teoan.job.auto.core.config.XxlJobAutoConfigProperties;
import com.teoan.job.auto.core.service.JobLoginService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Teoan
 * @description xxl-job任务登陆操作实现类
 * @since 2023/04/07 14:49
 */
@Service
public class JobLoginServiceImpl implements JobLoginService {

    @Resource
    XxlJobAutoConfigProperties properties;

    private String adminAddresses;


    private String username;

    private String password;


    @PostConstruct
    private void init(){
        this.adminAddresses = properties.getAdmin().getAddresses();
        this.username = properties.getAdmin().getUsername();
        this.password = properties.getAdmin().getPassword();
    }



    /**
     * cookie标识
     */
    private final static String COOKIE_KEY = "XXL_JOB_LOGIN_IDENTITY";
    private final Map<String, String> loginCookie = new HashMap<>(8);

    @Override
    public void login() {
        String url = adminAddresses + "/login";
        HttpResponse response = HttpRequest.post(url)
                .form("userName", username)
                .form("password", password)
                .execute();
        List<HttpCookie> cookies = response.getCookies();
        Optional<HttpCookie> cookieOpt = cookies.stream()
                .filter(cookie -> cookie.getName().equals(COOKIE_KEY)).findFirst();
        if (!cookieOpt.isPresent())
            throw new RuntimeException("get xxl-job cookie error!");

        String value = cookieOpt.get().getValue();
        loginCookie.put(COOKIE_KEY, value);
    }

    @Override
    public String getCookie() {
        for (int i = 0; i < 3; i++) {
            String cookieStr = loginCookie.get(COOKIE_KEY);
            if (cookieStr != null) {
                return COOKIE_KEY + "=" + cookieStr;
            }
            login();
        }
        throw new RuntimeException("get xxl-job cookie error!");
    }

}
