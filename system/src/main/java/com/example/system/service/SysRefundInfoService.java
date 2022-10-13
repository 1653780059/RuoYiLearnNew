package com.example.system.service;

import com.example.base.domain.SysRefundInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 16537
* @description 针对表【sys_refund_info】的数据库操作Service
* @createDate 2022-10-11 10:50:22
*/
public interface SysRefundInfoService extends IService<SysRefundInfo> {

    /**
     * 新建退款单，并保存到数据库
     * @param orderNo 订单编号
     * @param reason 退款原因
     */
    void newRefundInfoServiceAndSave(String orderNo, String reason);

    /**
     * 获取退款中的退款信息
     * @return 退款中的退款记录
     */
    List<SysRefundInfo> getRefundProcessing();
}
