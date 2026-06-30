package org.example.filter;

import ch.qos.logback.core.util.StringUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class CaptchaFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if ("/auth/login".equals(request.getRequestURI()) && "POST".equals(request.getMethod())) {
            // 你前端 input name = captchaCode
            String captchaCode = request.getParameter("captchaCode");
            Object sessionCaptcha = request.getSession().getAttribute("captcha");

            System.out.println("前端传入验证码：" + captchaCode);
            System.out.println("Session存储验证码：" + sessionCaptcha);

            if (captchaCode == null || captchaCode.isBlank()) {
                response.sendError(400, "验证码不能为空");
                return;
            }
            if (sessionCaptcha == null) {
                response.sendError(400, "验证码已过期，请刷新");
                return;
            }
            if (!captchaCode.equalsIgnoreCase(sessionCaptcha.toString())) {
                response.sendError(401, "验证码错误");
                return;
            }

            // 校验通过，删除session里的验证码
            request.getSession().removeAttribute("captcha");
        }

        // 其他请求直接放行
        filterChain.doFilter(request, response);

    }
}
