package com.example.admin.controller;

import com.example.common.result.Result;
import com.example.system.service.WxPayService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author 16537
 * @Classname WxPayController
 * @Description
 * @Version 1.0.0
 * @Date 2022/10/11 14:01
 */

@RestController
@RequestMapping("/sys/native")
@AllArgsConstructor

public class WxPayController {
    private WxPayService wxPayService;
    @PreAuthorize("@a.hasPer('sys:menu:delete')")
    @PostMapping("/pay/{productId}/{count}")
    public Result nativePay(@PathVariable String productId, @PathVariable String count) throws Exception {
        return wxPayService.nativePay(productId,count);
    }
}
