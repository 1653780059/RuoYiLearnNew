package com.example.system.service.impl;

import com.example.common.constants.FileConstants;
import com.example.common.result.Result;
import com.example.common.utils.FileUtils;
import com.example.system.service.CommonService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author 16537
 * @Classname CommonServiceImpl
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/26 10:34
 */
@Service
public class CommonServiceImpl implements CommonService {
    @Override
    public Result upload(MultipartFile file) {
        String path = FileUtils.fileUpload(file);

        return new Result().ok(path);
    }
}
