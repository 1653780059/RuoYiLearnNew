package com.example.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 16537
 * @Classname LearnConfig
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/26 10:52
 */
@Component
@ConfigurationProperties(prefix = "learn")
@Data
public class LearnConfig {
    public  String resource;

    public  String getProfile(){
        return resource+"/profile";
    }
    public  String getUploadPath(){
        return getProfile()+"/upload";
    }
    public  String getResource(){
        return resource;
    }
}
