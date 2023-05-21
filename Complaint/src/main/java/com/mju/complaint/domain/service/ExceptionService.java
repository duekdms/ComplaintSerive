package com.mju.complaint.domain.service;

import com.mju.complaint.domain.model.Exception.ExceptionList;
import com.mju.complaint.domain.model.Exception.NonExceptionReportedQuestion;
import com.mju.complaint.domain.model.Exception.ServerRequestFailed;
import com.mju.complaint.domain.model.Result.CommonResult;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionService {

    private final ResponseService responseService;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult unknown(Exception e){
        log.error("unknown exception", e);
        return responseService.getFailResult(ExceptionList.UNKNOWN.getCode(), ExceptionList.UNKNOWN.getMessage());
    }
    @ExceptionHandler({ServerRequestFailed.class})
    protected CommonResult handleCustom(ServerRequestFailed e) {
        log.error("Server Request Failed", e);
        ExceptionList exceptionList = e.getExceptionList();
        return responseService.getFailResult(exceptionList.getCode(), exceptionList.getMessage());
    }

    @ExceptionHandler({NonExceptionReportedQuestion.class})
    protected CommonResult handleCustom(NonExceptionReportedQuestion e) {
        log.error("Non Exception Question", e);
        ExceptionList exceptionList = e.getExceptionList();
        return responseService.getFailResult(exceptionList.getCode(), exceptionList.getMessage());
    }

    @ExceptionHandler(FeignException.class)
    public CommonResult handleFeignException(FeignException e) {
        log.error("feign exception", e);
        return responseService.getFailResult(ExceptionList.NON_EXCEPTION_INDEX.getCode(), ExceptionList.NON_EXCEPTION_INDEX.getMessage());
    }

}
