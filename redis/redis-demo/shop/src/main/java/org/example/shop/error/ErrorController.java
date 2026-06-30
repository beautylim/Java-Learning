package org.example.shop.error;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.transaction.TransactionException;
import org.example.common.ErrorResponse;
import org.example.shop.exception.ShopException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorController {

    @ExceptionHandler(ShopException.class)
    public Map<String, Object> handleShopException(ShopException e) {
        log.error("受检异常", e);

        Map<String, Object> response = new HashMap<>();
        response.put("code", e.getStatus());
        response.put("message", e.getMessage());
        return response;
    }

    /**
     * 处理事务相关异常
     */
    @ExceptionHandler(TransactionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleTransactionException(TransactionException e) {
        log.error("事务异常，已回滚", e);

        Map<String, Object> response = new HashMap<>();
        response.put("code", 500);
        response.put("message", "事务处理失败");
        return response;
    }

    /**
     * 兜底处理所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleException(Exception e) {
        log.error("未知异常", e);

        Map<String, Object> response = new HashMap<>();
        response.put("code", 500);
        response.put("message", e.getMessage());
        return response;
    }
}
