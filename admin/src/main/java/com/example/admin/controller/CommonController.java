package com.example.admin.controller;

import com.example.common.result.Result;
import com.example.system.service.CommonService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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
@AllArgsConstructor
public class CommonController {

    private CommonService commonService;

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
