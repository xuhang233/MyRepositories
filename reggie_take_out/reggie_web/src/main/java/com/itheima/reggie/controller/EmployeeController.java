
package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.pojo.Employee;
import com.itheima.reggie.service.EmployeeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * 员工管理模块
 */

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    //1,根据用户名查询
    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee employee, HttpSession Session) {
        String username = employee.getUsername();
        String password = employee.getPassword();

        Employee e = employeeService.lambdaQuery()
                .eq(Employee::getUsername, username)
                .one();
        //说明账号不存在
        if (e == null) {
            return R.error("账户不存在");
        }
        //2.判断密码是否一致
        if (!StringUtils.equals(e.getPassword(), DigestUtils.md5DigestAsHex(password.getBytes()))) {
            //说明密码错误
            return R.error("密码错误");
        }
        //3,判断用用户状态
        if (e.getStatus() == 0) {
            //说明用户被禁用
            return R.error("账户已被禁用");
        }
        //4说明登录成功,将用户存入session中
        Session.setAttribute("employee", e.getId());
        return R.success(e);
    }


    /**
     * 退出登录
     *
     * @param session
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpSession session) {
        //
        session.invalidate();
        //
        return R.success("退出成功");
    }


    /**
     * 添加员工
     *
     * @param employee
     * @param session
     * @return
     */
    @PostMapping
    public R<String> add(@RequestBody Employee employee, HttpSession session) {
        //设置初始密码123456并进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
/*        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());*/
        //从session中获取employee 类型是long类型
//        long empId = (long) session.getAttribute("employee");
/*        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);*/
        //调用service的save方法
        employeeService.save(employee);
        return R.success("新增员工成功");
    }


    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("page")
    public R<Page> page(Integer page, Integer pageSize, String name) {
        //创建一个分页对象
        Page<Employee> page1 = new Page(page, pageSize);
        //分页查方法
        employeeService.lambdaQuery()
                .like(StringUtils.isNotBlank(name), Employee::getName, name)
                .orderByDesc(Employee::getUpdateTime)
                //分页查询
                .page(page1);
        //响应数据
        return R.success(page1);
    }


    /**
     * 状态修改
     *
     * @param employee
     * @param session
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Employee employee, HttpSession session) {
        //补全员工信息,修改时间和修改人的id
/*        long empId = (long) session.getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setCreateUser(empId);*/
        //调用service的修改方法
        employeeService.updateById(employee);
        //返回值
        return R.success("修改成功");
    }


    /**
     * 查询员工
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public R<Employee> findByid(@PathVariable long id) {
        //调用service层的查询方法
        Employee byId = employeeService.getById(id);
        //返回对象属性
        return R.success(byId);
    }

}






