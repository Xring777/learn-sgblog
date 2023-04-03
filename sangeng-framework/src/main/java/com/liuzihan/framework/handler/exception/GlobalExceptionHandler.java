package com.liuzihan.framework.handler.exception;

import com.liuzihan.framework.domain.ResponseResult;
import com.liuzihan.framework.enums.AppHttpCodeEnum;
import com.liuzihan.framework.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = SystemException.class)
    public ResponseResult systemExceptionHandler(SystemException e){
        //打印异常信息
        log.error("出现了异常！",e);
        //从异常对象中获取信息封装返回
        return ResponseResult.errorResult(e.getCode(),e.getMsg());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseResult defaultExceptionHandler(Exception e){
        // 打印异常信息
        log.error("出现了异常！",e);
        // 从异常对象中获取信息封装返回
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),AppHttpCodeEnum.SYSTEM_ERROR.getMsg());
    }

}
