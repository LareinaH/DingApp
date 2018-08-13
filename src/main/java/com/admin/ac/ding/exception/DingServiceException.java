package com.admin.ac.ding.exception;

import com.dingtalk.api.DingTalkResponse;

public class DingServiceException extends Exception {

    private static final long serialVersionUID = 8984080500696717926L;

    public DingServiceException(String exceptionTitle, DingTalkResponse dingTalkResponse) {
        super(new StringBuffer(exceptionTitle).append(":")
                .append(dingTalkResponse.getErrmsg())
                .append("(").append(dingTalkResponse.getErrcode()).append(")").toString());
    }

    public DingServiceException(String message) {
        super(message);
    }

    public DingServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DingServiceException(Throwable cause) {
        super(cause);
    }

    public DingServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
