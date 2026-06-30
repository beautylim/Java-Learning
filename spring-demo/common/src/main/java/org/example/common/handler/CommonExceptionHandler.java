package org.example.common.handler;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.example.common.error.ResultError;
import org.example.common.exception.BizException;
import org.example.common.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class CommonExceptionHandler {

    @Resource
    private MessageService messageService;

    @ExceptionHandler(BizException.class)
    public Mono<ResponseEntity<ResultError>> handleBizException(BizException e) {
        String messageId = e.getMessageEnum().getMessageId();

        return Mono.just(ResponseEntity
                .status(messageService.getHttpCode(messageId))
                .body(messageService.convert(messageId)));
    }

//    // 2. 捕获参数异常
//    @ExceptionHandler(IllegalArgumentException.class)
//    public Mono<ResponseEntity<ApiResponse<?>>> handleParamError(IllegalArgumentException e) {
//        ApiResponse<?> resp = ApiResponse.fail(
//                BizCodeEnum.PARAM_ERROR.getCode(),
//                e.getMessage(),
//                BizCodeEnum.PARAM_ERROR.getRemediation()
//        );
//        return Mono.just(ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body(resp));
//    }
//
//    // 3. 兜底：全局未知异常
//    @ExceptionHandler(Exception.class)
//    public Mono<ResponseEntity<ApiResponse<?>>> handleGlobalError(Exception e) {
//        ApiResponse<?> resp = ApiResponse.fail(
//                BizCodeEnum.SERVER_ERROR.getCode(),
//                BizCodeEnum.SERVER_ERROR.getMessage(),
//                BizCodeEnum.SERVER_ERROR.getRemediation()
//        );
//        return Mono.just(ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(resp));
//    }

}
