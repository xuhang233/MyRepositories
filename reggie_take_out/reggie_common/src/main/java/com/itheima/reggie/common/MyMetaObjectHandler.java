package com.itheima.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {
        //参数1,包含了添加的对象
        //参数2,要自动填充的属性名
        //参数3,要自动添加的值的类型
        //参数4,要自动填充的值

        //自动填充创建时间
        this.setFieldValByName("createTime", LocalDateTime.now(),metaObject);
        this.setFieldValByName("updateTime", LocalDateTime.now(),metaObject);
        this.setFieldValByName("createUser", BaseContext.get(),metaObject);
        this.setFieldValByName("updateUser", BaseContext.get(),metaObject);


    }

    @Override
    public void updateFill(MetaObject metaObject) {
        //自动填充修改时间
        this.setFieldValByName("updateTime", LocalDateTime.now(),metaObject);
        this.setFieldValByName("updateUser", BaseContext.get(),metaObject);

    }
}
