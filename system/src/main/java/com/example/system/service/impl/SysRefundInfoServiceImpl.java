package com.example.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.base.domain.SysOrderInfo;
import com.example.base.domain.SysRefundInfo;
import com.example.common.enums.wx.WxRefundStatus;
import com.example.common.utils.OrderNoUtils;
import com.example.system.service.SysOrderInfoService;
import com.example.system.service.SysRefundInfoService;
import com.example.dao.mapper.SysRefundInfoMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
* @author 16537
* @description 针对表【sys_refund_info】的数据库操作Service实现
* @createDate 2022-10-11 10:50:22
*/
@Service
@AllArgsConstructor
public class SysRefundInfoServiceImpl extends ServiceImpl<SysRefundInfoMapper, SysRefundInfo>
    implements SysRefundInfoService{
    private SysOrderInfoService orderInfoService;
    @Override
    public void newRefundInfoServiceAndSave(String orderNo, String reason) {
        SysOrderInfo orderInfo = orderInfoService.getOne(new LambdaQueryWrapper<SysOrderInfo>().eq(SysOrderInfo::getOrderNo, orderNo));
        SysRefundInfo sysRefundInfo = new SysRefundInfo();
        sysRefundInfo.setRefundNo(OrderNoUtils.getRefundNo());
        sysRefundInfo.setOrderNo(orderNo);
        sysRefundInfo.setReason(reason);
        sysRefundInfo.setTotalFee(orderInfo.getTotalFee());
        baseMapper.insert(sysRefundInfo);
    }

    @Override
    public List<SysRefundInfo> getRefundProcessing() {
        LambdaQueryWrapper<SysRefundInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysRefundInfo::getRefundStatus,WxRefundStatus.PROCESSING);
        return baseMapper.selectList(lambdaQueryWrapper);

    }
}




