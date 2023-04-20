package com.teoan.job.auto.samples;

import com.teoan.job.auto.annotation.EnableXxlJobAuto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableXxlJobAuto
public class XxlJobAutoApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(XxlJobAutoApplication.class,args);
    }
}
