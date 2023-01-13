package com.atguigu.common.exception;

import com.atguigu.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/5 17:13
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        log.error(e.getMessage());
        return Result.fail(e);
    }

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public Result customException(CustomException e) {
        log.error(e.getMessage());
        return Result.fail(e.getMessage());
    }

}
