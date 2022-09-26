package com.example.farmwork.utils;

import com.example.common.result.PageDomain;
import com.example.common.utils.ServletUtils;
import io.swagger.models.auth.In;

/**
 * @author 16537
 * @Classname TableSupport
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/26 9:24
 */

public class TableSupport {
    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 排序列
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static final String IS_ASC = "isAsc";

    /**
     * 分页参数合理化
     */
    public static final String REASONABLE = "reasonable";

    public static PageDomain getPageDomain() {
        PageDomain pageDomain = new PageDomain();
        pageDomain.setPageNum(Integer.valueOf(ServletUtils.getParam(PAGE_NUM,"1")));
        pageDomain.setPageSize(Integer.valueOf(ServletUtils.getParam(PAGE_SIZE,"10")));
        pageDomain.setIsAsc(ServletUtils.getParam(IS_ASC));
        pageDomain.setOrderByColumn(ServletUtils.getParam(ORDER_BY_COLUMN));
        pageDomain.setReasonable(Boolean.valueOf(ServletUtils.getParam(REASONABLE)));
        return pageDomain;
    }
}
