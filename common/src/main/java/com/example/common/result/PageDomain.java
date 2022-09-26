package com.example.common.result;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * @author 16537
 * @Classname PageDomain
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/26 9:11
 */
@Data
public class PageDomain {
    /** 当前记录起始索引 */
    private Integer pageNum;

    /** 每页显示记录数 */
    private Integer pageSize;

    /** 排序列 */
    private String orderByColumn;

    /** 排序的方向desc或者asc */
    private String isAsc = "asc";

    /** 分页参数合理化 */
    private Boolean reasonable = true;


}
