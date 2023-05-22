package com.itheima.reggie.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    //将异常的范围扩大
    @ExceptionHandler(SQLException.class)
    public R Handler(SQLException exception){
        exception.printStackTrace();
        //判断sql报错是哪种异常
        if (exception.getMessage().startsWith("Duplicate entry")){
            return R.error(exception.getMessage().split(" ")[2]+"已存在");
        }
        if (exception.getMessage().startsWith("Data truncation: Data too long for column")){
            return R.error(exception.getMessage().split(" ")[7]+"数据长度异常");
        }
        return R.error("数据异常");
    }

    //捕获自定义异常
    @ExceptionHandler(CustomerException.class)
    public R<String> exception2(CustomerException customerException){
        //判断是否报错
        String message = customerException.getMessage();
        return R.error(message);
    }
}
