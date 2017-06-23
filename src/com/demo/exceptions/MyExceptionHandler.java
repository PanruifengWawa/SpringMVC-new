package com.demo.exceptions;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.enums.ErrorCodeEnum;
import com.demo.utils.DataWrapper;

@ControllerAdvice
public class MyExceptionHandler {
	
	@ExceptionHandler(RuntimeException.class)
	@ResponseBody
    public DataWrapper<Void> exceptionProcess(HttpServletRequest request,
    		HttpServletResponse response,
    		RuntimeException ex) {  
		DataWrapper<Void> dataWrapper = new DataWrapper<Void>();
		dataWrapper.setErrorCode(ErrorCodeEnum.Error);
		
        return dataWrapper;
    }
	
	

}
