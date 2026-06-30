package org.example.auth.controller;

import org.example.auth.exception.AuthException;
import org.example.auth.model.dto.LoginRequest;
import org.example.auth.model.dto.TelCode;
import org.example.auth.model.entity.SysUser;
import org.example.auth.service.LoginService;
import org.example.auth.service.LogoutService;
import org.example.auth.service.SysUserService;
import org.example.auth.service.TelCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private LogoutService logOutService;

    @Autowired
    private TelCodeService telCodeService;

    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws AuthException {
        String token = loginService.login(loginRequest);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/code")
    public ResponseEntity<TelCode> sendCode(@RequestParam("phoneNumber") String phoneNumber) throws AuthException {
        return ResponseEntity.status(HttpStatus.CREATED).body(TelCode.builder()
                .code(telCodeService.generateTelCode(phoneNumber)).build());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<SysUser> getUserById(@PathVariable Long id) throws AuthException {
        return ResponseEntity.ok(sysUserService.getUserById(id));
    }

    @GetMapping("/user/profile")
    public ResponseEntity<SysUser> getUserProfile(@RequestHeader HttpHeaders header) throws AuthException {
        return ResponseEntity.ok(sysUserService.getUserProfile());
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout() throws AuthException {
        logOutService.logout();
        return ResponseEntity.ok("logout success");
    }
}
