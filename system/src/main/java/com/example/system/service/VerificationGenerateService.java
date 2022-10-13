package com.example.system.service;

import com.example.common.result.Result;

/**
 * @Classname VerificationGenerateService
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/21 13:53
 * @Created by 16537
 */
public interface VerificationGenerateService {
    /**
     * 生成验证码
     * @return 返回BASE64加密的验证码图片信息，并返回验证码答案对应的uuid
     */
    Result verificationGenerate();
}
