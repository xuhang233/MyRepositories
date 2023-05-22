package com.itheima.reggie.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 地址簿
 */
@Data
public class AddressBook implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;    //用户id
    private String consignee;    //收货人
    private String phone;    //手机号
    private String sex;    //性别 0 女 1 男
    private String provinceCode;    //省级区划编号
    private String provinceName;    //省级名称
    private String cityCode;    //市级区划编号
    private String cityName;    //市级名称
    private String districtCode; //区级区划编号
    private String districtName;    //区级名称
    private String detail;    //详细地址
    private String label;    //标签
    private Integer isDefault;    //是否默认 0 否 1是

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;    //创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;    //更新时间
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;    //创建人
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;    //修改人
}
