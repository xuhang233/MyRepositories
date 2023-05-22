package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import com.itheima.reggie.pojo.User;
import com.itheima.reggie.service.UserService;
import com.itheima.reggie.utils.SendSms;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 移动端用户管理
 */
@RestController
@RequestMapping("user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;


    /**
     *  //发送验证码
     * @param user
     * @param session
     * @return
     */
    @PostMapping("sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //
        String phone = user.getPhone();
        //
        String code = RandomStringUtils.random(4, false, true);
        //
//        SendSms.Send(phone,code);
        log.info("验证码是:{}",code);
        session.setAttribute(phone,code);
        return R.success("发送成功");
    }



    /**
     * //验证码登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("login")
    public R<User> login(@RequestBody Map<String,String> map,HttpSession session){
        String phone = map.get("phone");
        //
        String code1 = map.get("code");
        String code2 = (String)session.getAttribute(phone);
        //
        if (!StringUtils.equals(code1,code2)){
            //登录失败
            return R.error("验证码错误");
        }
        //
        //
        User user = userService.lambdaQuery()
                .eq(User::getPhone, phone)
                .one();
        if (user == null){
            //
            user = new User();
            user.setPhone(phone);
            user.setStatus(1);
            userService.save(user);
        }
        //
        session.setAttribute("user",user.getId());
        return R.success(user);
    }



    /**
     * //客户端退出登录
     * @return
     */
    @PostMapping("loginout")
    public R<String> loginout(){
        return R.success("退出成功");
    }

}
