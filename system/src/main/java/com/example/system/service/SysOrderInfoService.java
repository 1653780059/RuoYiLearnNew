package com.example.system.service;

import com.example.base.domain.SysOrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 16537
* @description 针对表【sys_order_info】的数据库操作Service
* @createDate 2022-10-11 10:41:08
*/
public interface SysOrderInfoService extends IService<SysOrderInfo> {

    SysOrderInfo newOrderInfo(String productId, String count);
}
