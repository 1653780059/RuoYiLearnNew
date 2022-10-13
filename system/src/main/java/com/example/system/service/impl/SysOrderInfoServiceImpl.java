package com.example.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.base.domain.SysOrderInfo;
import com.example.base.domain.SysProduct;
import com.example.common.config.LearnConfig;
import com.example.common.enums.OrderStatus;
import com.example.common.utils.OrderNoUtils;
import com.example.farmwork.factory.AsyncFactory;
import com.example.farmwork.factory.AsyncManager;
import com.example.farmwork.utils.SecurityUtils;
import com.example.system.service.SysOrderInfoService;
import com.example.dao.mapper.SysOrderInfoMapper;
import com.example.system.service.SysProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
* @author 16537
* @description 针对表【sys_order_info】的数据库操作Service实现
* @createDate 2022-10-11 10:41:08
*/
@Service
@Slf4j
@AllArgsConstructor
public class SysOrderInfoServiceImpl extends ServiceImpl<SysOrderInfoMapper, SysOrderInfo>
    implements SysOrderInfoService{
    private SysProductService productService;
    private LearnConfig learnConfig;
    @Override
    public SysOrderInfo newOrderInfoAndSave(Long productId, Integer count) {
        SysProduct product = productService.getOne(new QueryWrapper<SysProduct>().eq("id", productId));
        SysOrderInfo orderInfo = new SysOrderInfo();
        orderInfo.setOrderNo(OrderNoUtils.getOrderNo());
        orderInfo.setOrderStatus(OrderStatus.NOTPAY.getType());
        orderInfo.setProductId(product.getId());
        orderInfo.setTitle(product.getTitle());
        orderInfo.setTotalFee(product.getPrice()*count);
        orderInfo.setCreateTime(new Date());
        orderInfo.setUpdateTime(new Date());
        orderInfo.setCount(count);
        orderInfo.setUserId(SecurityUtils.getLoginUser().getId().longValue());
        AsyncManager.getAsyncManager().execute(new AsyncFactory().sysOrderInfoSaveTask(orderInfo));
        return orderInfo;
    }

    @Override
    public List<SysOrderInfo> getTimeOutOrders() {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE,learnConfig.getOrderTimeout());
        Date time = instance.getTime();
        LambdaQueryWrapper<SysOrderInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysOrderInfo::getOrderStatus,OrderStatus.NOTPAY.getType());
        queryWrapper.le(SysOrderInfo::getCreateTime,time);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public void changeOrderStatus(String orderNo, String state) {
        log.info("订单状态设置为==》"+state);
        LambdaQueryWrapper<SysOrderInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysOrderInfo::getOrderNo,orderNo);

        SysOrderInfo orderInfo = new SysOrderInfo();
        orderInfo.setOrderStatus(state);
        baseMapper.update(orderInfo,queryWrapper);
    }
}




