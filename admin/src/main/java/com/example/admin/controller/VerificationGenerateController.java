package com.example.admin.controller;

import com.example.common.result.Result;
import com.example.system.service.VerificationGenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 16537
 * @Classname VerificationGenerateController
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/21 13:44
 * @Created by 16537
 */
@RestController
@RequestMapping("/sys")
public class VerificationGenerateController {
    @Autowired
    VerificationGenerateService verificationGenerateService;
    @PreAuthorize("@a.hasPer('sys:menu:delete')")
    @GetMapping("/verification")
    public Result verificationGenerate(){
        return verificationGenerateService.verificationGenerate();
    }
}
