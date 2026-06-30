package org.example.shop.feign;

import org.example.auth.model.entity.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "authserver")
public interface SysUserFeignClient {
    @GetMapping("/auth/user/{id}")
    public ResponseEntity<SysUser> getUserById(@PathVariable Long id) ;
}
