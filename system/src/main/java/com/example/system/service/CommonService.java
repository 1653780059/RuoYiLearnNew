package com.example.system.service;

import com.example.common.result.Result;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 16537
 * @Classname CommonService
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/26 10:34
 */
public interface CommonService {
    Result upload(MultipartFile file);
}
