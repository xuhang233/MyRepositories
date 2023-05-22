package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * 处理文件上传下载
 */
@RestController
@RequestMapping("common")
public class CommonController {

    @Value("${reggie.path}")
    private String BASE_PATH;


    /**
     * 上传图片
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("upload")
    public R<String> upload(MultipartFile file) throws IOException {
        //判断文件是否存在
        File dirFile = new File(BASE_PATH);
        if (!dirFile.exists()) {
            //说明该目录不存在,并自动创建
            dirFile.mkdirs();
        }
        //生产文件名称的唯一uuid
        String uuid = UUID.randomUUID().toString();
        //截取原始文件名称的后缀
        String substring = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String uuidName = uuid + substring;
        //将文件上传到指定位置
        file.transferTo(new File(BASE_PATH + uuidName));
        //响应文件名称的数据
        return R.success(uuidName);
    }


    /**
     * 下载图片
     *
     * @param name
     * @param response
     * @throws IOException
     */
    @GetMapping("download")
    public void download(String name, HttpServletResponse response) throws IOException {
        //准备输入流
        FileInputStream is = new FileInputStream(BASE_PATH + name);
        //输出流,response
        ServletOutputStream os = response.getOutputStream();
        //输入流和输入流对接,输入流:服务器的文件,输出流:response
        IOUtils.copy(is, os);
        is.close();
    }
}
