package com.teoan.job.auto.samples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class XxlJobAutoApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(XxlJobAutoApplication.class,args);
    }
}
