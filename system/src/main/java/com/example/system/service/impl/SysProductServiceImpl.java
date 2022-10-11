package com.example.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.base.domain.SysProduct;
import com.example.system.service.SysProductService;
import com.example.dao.mapper.SysProductMapper;
import org.springframework.stereotype.Service;

/**
* @author 16537
* @description 针对表【sys_product】的数据库操作Service实现
* @createDate 2022-10-11 10:49:28
*/
@Service
public class SysProductServiceImpl extends ServiceImpl<SysProductMapper, SysProduct>
    implements SysProductService{

}




