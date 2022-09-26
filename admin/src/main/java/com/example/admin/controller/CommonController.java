package com.example.admin.controller;

import com.example.common.result.Result;
import com.example.system.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 16537
 * @Classname CommonController
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/26 10:32
 */
@RestController
@RequestMapping("/common")
public class CommonController {
    @Autowired
    CommonService commonService;

    /**
     * 文件上传
     * @param file 文件对象
     * @return
     */
    @PreAuthorize("@a.hasPer('sys:menu:delete')")
    @PostMapping("/upload")
    public Result upload(MultipartFile file){
        return commonService.upload(file);

    }
}
