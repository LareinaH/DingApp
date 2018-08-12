package com.admin.ac.ding.controller;

import com.admin.ac.ding.constants.Constants;
import com.admin.ac.ding.model.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * BaseController
 * @author lareina_h
 * @version 1.0
 * @date 2018/5/10
 */
public class BaseController {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@ExceptionHandler
	@ResponseBody
	public RestResponse handelException(Exception ex) {
		logger.error("exception", ex);

		if(ex instanceof MissingServletRequestParameterException){
			return RestResponse.getFailedResponse(Constants.RcError, "参数错误",null);
		}else {
			// TODO 不暴露服务器错误消息
			return RestResponse.getFailedResponse(Constants.RcError, "服务器出小差了",null);
		}
	}

}
