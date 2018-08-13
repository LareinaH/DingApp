package com.admin.ac.ding.controller;

import com.admin.ac.ding.constants.Constants;
import com.admin.ac.ding.exception.DingServiceException;
import com.admin.ac.ding.model.RestResponse;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.ExecutionException;

/**
 * BaseController
 * @author lareina_h
 * @version 1.0
 * @date 2018/5/10
 */
public class BaseController {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${debugMode}")
    Boolean debugMode;

	@ExceptionHandler
	@ResponseBody
	public RestResponse handelException(Exception ex) {
		logger.error("exception occured", ex);

		if (debugMode) {
            return RestResponse.getFailedResponse(Constants.RcError, ex.getMessage(),null);
        }

		if (ex instanceof ApiException
				|| ex instanceof ExecutionException
				|| ex instanceof DingServiceException) {
			return RestResponse.getFailedResponse(Constants.RcError, "钉钉接口异常",null);
		} else if(ex instanceof MissingServletRequestParameterException){
			return RestResponse.getFailedResponse(Constants.RcError, "参数错误",null);
		} else {
			return RestResponse.getFailedResponse(Constants.RcError, "服务器出小差了",null);
		}
	}

}
