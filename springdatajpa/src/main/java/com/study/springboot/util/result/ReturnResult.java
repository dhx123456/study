package com.study.springboot.util.result;

/**
 * 方法执行成功后，返回的工具类
 */
public class ReturnResult {
    private ResultMsg resultMsg = new ResultMsg();

    public ResultMsg returnSuccess(Object object) {
        resultMsg.setResultCode(ResultStatusCode.OK.getResultCode());
        resultMsg.setResultData(object);
        resultMsg.setResultMsg("SUCCESS");
        return resultMsg;
    }

    public ResultMsg returnSuccess(Object object, String msg) {
        resultMsg.setResultCode(ResultStatusCode.OK.getResultCode());
        resultMsg.setResultData(object);
        resultMsg.setResultMsg(msg);
        return resultMsg;
    }


    public ResultMsg returnError(Object object) {
        resultMsg.setResultCode(ResultStatusCode.ERROR.getResultCode());
        resultMsg.setResultData(object);
        resultMsg.setResultMsg(object.toString());
        return resultMsg;
    }

    public ResultMsg returnError(Object object, String errorMsg, Exception e) {
        resultMsg.setResultCode(ResultStatusCode.ERROR.getResultCode());
        resultMsg.setResultData(object);
        resultMsg.setResultMsg(errorMsg);
        return resultMsg;
    }


}
