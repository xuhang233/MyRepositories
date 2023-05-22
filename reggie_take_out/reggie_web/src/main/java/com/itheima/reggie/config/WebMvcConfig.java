package com.itheima.reggie.config;

import com.itheima.reggie.common.JacksonObjectMapper;
import com.itheima.reggie.common.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

//表示该类是一个配置类
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")//拦截的路径
                .excludePathPatterns("/employee/login")//排除的路径
                .excludePathPatterns("/user/login")//移动端登录
                .excludePathPatterns("/user/sendMsg")//移动端验证码
                .excludePathPatterns("/backend/**")
                .excludePathPatterns("/front/**");
    }


    //扩展SpringMvc消息转换器
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //创建一个消息转换对象,MappingJackson2HttpMessageConverter
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //TODO 最核心的操作,不设置默认是ObjectMapper
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将消息转换器添加到集合中, 需要放在集合的最前面
        converters.add(0,messageConverter);
    }
}
