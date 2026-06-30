package org.example.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class CaptchaController {

    @GetMapping("/captcha")
    public void captcha(HttpServletResponse response, HttpSession httpSession) throws IOException {

        //1. 生成验证码
        //定义图形验证码的长、宽、验证码字符数、干扰元素个数
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 20);

        //图形验证码写出，可以写出到文件，也可以写出到流
        captcha.write(response.getOutputStream());

        // 禁止缓存，防止验证码图片重复展示
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        //2.存入session
        httpSession.setAttribute("captcha", captcha.getCode());
        System.out.println(captcha.getCode());
    }
}
