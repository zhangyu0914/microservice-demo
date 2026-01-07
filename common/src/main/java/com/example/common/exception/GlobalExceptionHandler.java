package com.example.common.exception;


import com.example.common.model.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;
import java.util.concurrent.CompletionException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 自定义异常处理
     */
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        log.info(e.getMessage(), e);
        return Result.error( e.getCode(),e.getMessage()) ;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<?> handleInvalidHttpMessageException(HttpMessageNotReadableException exception, HttpServletRequest request) {
        log.info("请求地址'{}', 请求参数不匹配，请检查数据类型：", request.getRequestURI(), exception);
        return Result.error("你的输入有误，请重新输入！");
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                      HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        return Result.error(e.getMessage());
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常.", requestURI, e);
        return Result.error("网络异常，请联系管理员！");
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public Result <?> handleException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", requestURI, e);
        return Result.error("网络异常，请联系管理员！");
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public Result <?> handleBindException(BindException e) {
        log.info(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return Result.error(message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.info(e.getMessage(), e);
        String message = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return Result.error(message);
    }

    /**
     * 断言异常处理
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<?> handleIllegalArgumentException(IllegalArgumentException e) {
        log.info("发生业务异常！原因是：{}", e.getMessage());
        return Result.error( e.getMessage());
    }

    /**
     * 处理空指针的异常
     */
    @ExceptionHandler(NullPointerException.class)
    public Result<?> handleNullPointerException(NullPointerException e) {
        log.error("发生空指针异常！原因是:", e);
        return Result.error( "服务端出现异常");
    }

    /**
     * 自定义异常处理
     */
    @ExceptionHandler(CompletionException.class)
    public Result<?> handleCompletionException(CompletionException e) {
        log.info(e.getMessage(), e);
        return Result.error(e.getMessage());
    }
}
