package com.example.system.service;

import com.example.base.domain.SysOrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 16537
* @description 针对表【sys_order_info】的数据库操作Service
* @createDate 2022-10-11 10:41:08
*/
public interface SysOrderInfoService extends IService<SysOrderInfo> {
    /**
     * 新建订单对象，并保存到数据库
     * @param productId 商品id
     * @param count 购买数量
     * @return 订单对象
     */
    SysOrderInfo newOrderInfoAndSave(Long productId, Integer count);

    /**
     *获取超时未支付订单
     * @return 超时未支付订单列表
     */
    List<SysOrderInfo> getTimeOutOrders();

    /**
     * 修改订单状态
     * @param orderNo 订单编号
     * @param state 需要修改成为的订单状态
     */
    void changeOrderStatus(String orderNo, String state);
}
