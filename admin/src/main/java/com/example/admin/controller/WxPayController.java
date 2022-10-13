package com.example.admin.controller;

import com.example.base.domain.SysOrderInfo;
import com.example.base.domain.SysRefundInfo;
import com.example.common.result.Result;
import com.example.system.service.WxPayService;
import com.wechat.pay.contrib.apache.httpclient.exception.ParseException;
import com.wechat.pay.contrib.apache.httpclient.exception.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

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

public class WxPayController extends BaseController {
    private WxPayService wxPayService;
    @PreAuthorize("@a.hasPer('sys:menu:delete')")
    @PostMapping("/pay")
    public Result nativePay(@RequestBody SysOrderInfo orderInfo) throws Exception {
        return wxPayService.nativePay(orderInfo.getProductId(),orderInfo.getCount());
    }
    @PostMapping("/pay/notify")
    public String nativePayNotify(HttpServletRequest request , HttpServletResponse response) throws ValidationException, ParseException {
        return wxPayService.nativePayNotify(request,response);
    }
    @PreAuthorize("@a.hasPer('sys:menu:delete')")
    @PostMapping("/pay/channel")
    public Result channelOrder(@RequestBody String orderNo) throws IOException {
        wxPayService.channelOrder(orderNo);
        return Result.success("订单取消成功");
    }
    @PreAuthorize("@a.hasPer('sys:menu:delete')")
    @PostMapping("/pay/refund/info")
    public Result refundOrderInfo(@RequestBody SysRefundInfo refundInfo){
        return wxPayService.refundOrderInfo(refundInfo.getOrderNo(),refundInfo.getReason());
    }
    @PreAuthorize("@a.hasPer('sys:menu:delete')")
    @PostMapping("/pay/refund/confirm")
    public Result refundConfirm(@RequestBody SysRefundInfo refundInfo) throws IOException {
        return wxPayService.refundConfirm(refundInfo.getRefundNo(),refundInfo.getRefund());

    }
    @PostMapping("/pay/refund/notify")
    public String refundNotify(HttpServletRequest request,HttpServletResponse response) throws ValidationException, ParseException {
        return wxPayService.refundNotify(request,response);
    }
    @PostMapping("/pay/bill/{date}/{type}")
    public Result getBill(@PathVariable String date, @PathVariable String type) throws IOException {
        return wxPayService.getBill(date,type);
    }

}
