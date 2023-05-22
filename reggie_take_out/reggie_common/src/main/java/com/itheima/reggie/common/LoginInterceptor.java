package com.itheima.reggie.common;


import com.alibaba.fastjson.JSON;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 配置拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断用户是否登录,看session中没有employee 就登录放行
        Long employee = (Long)request.getSession().getAttribute("employee");
        Long userId = (Long)request.getSession().getAttribute("user");
        //表示用户没有登录,告诉前端,本次请求的访问未登录,被拒绝处理,响应code=0 msg="NOLOGIN"
        if (employee!=null){
            BaseContext.set(employee);
            return true;
        }
        if (userId!=null){
            //员工登录成功的情况下,需要将员工Id存入本地线程中ThreadLocal
            BaseContext.set(userId);
            return true;
        }
        String NOLOGIN = JSON.toJSONString(R.error("NOTLOGIN"));
        response.getWriter().write(NOLOGIN);
        return false;
    }
}
