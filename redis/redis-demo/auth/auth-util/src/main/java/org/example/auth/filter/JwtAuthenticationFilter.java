package org.example.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.auth.LoginUser;
import org.example.auth.context.TokenContext;
import org.example.auth.utils.JwtUtil;
import org.example.auth.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().endsWith("/login") || request.getServletPath().endsWith("/code") || request.getServletPath().contains("image")) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String authorizationHeader = request.getHeader("Authorization");
            String token = authorizationHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            Set<GrantedAuthority> authorities = redisUtil.getUser(username);
            if (authorities == null) {
                authorities = Set.of(new SimpleGrantedAuthority("shop:get"));
            }
            Long userId = jwtUtil.extractUserId(token);
            LoginUser loginUser = new LoginUser(userId, username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(loginUser, null, authorities));
            TokenContext.set(token);
            filterChain.doFilter(request, response);
        } finally {
            TokenContext.clear();
        }

    }
}

