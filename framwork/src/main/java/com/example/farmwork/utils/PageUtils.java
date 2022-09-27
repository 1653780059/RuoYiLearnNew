package com.example.farmwork.utils;

import com.example.common.result.PageDomain;
import com.github.pagehelper.PageHelper;

/**
 * @author 16537
 * @Classname PageUtils
 * @Description 分页工具
 * @Version 1.0.0
 * @Date 2022/9/26 9:10
 */
public class PageUtils extends PageHelper {

    public static void startPage() {
        PageDomain pageDomain = TableSupport.getPageDomain();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        String isAsc = pageDomain.getIsAsc();
        String orderByColumn = pageDomain.getOrderByColumn();
        Boolean reasonable = pageDomain.getReasonable();
        PageHelper.startPage(pageNum,pageSize,orderByColumn).setReasonable(true);
    }

    public static void clearPage()
    {
        PageHelper.clearPage();
    }
}
