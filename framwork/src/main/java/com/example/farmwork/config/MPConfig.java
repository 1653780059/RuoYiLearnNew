package com.example.farmwork.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author 16537
 * @Classname MPConfig
 * @Description 字段值自动注入
 * @Version 1.0.0
 * @Date 2022/9/22 11:07
 */
@Configuration
public class MPConfig implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createtime", Date.class, new Date());
        this.strictInsertFill(metaObject, "updatetime", Date.class, new Date());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "updatetime", Date.class, new Date());
    }
}
