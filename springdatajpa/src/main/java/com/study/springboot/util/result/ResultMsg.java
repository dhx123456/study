package com.study.springboot.util.result;

import java.io.Serializable;

/**
 * Created by inhoo on 2017/5/11.
 */
public class ResultMsg implements Serializable {

    private int resultCode;
    private String resultMsg;
    private Object resultData;

    private Exception exception;

    public ResultMsg() {
    }

    public ResultMsg(ResultStatusCode resultCode, String resultMsg, Object resultData) {
        this.resultCode = resultCode.getResultCode();
        this.resultData = resultData;
        this.resultMsg = resultMsg;
    }

    public ResultMsg(ResultStatusCode resultCode, String resultMsg, Object resultData, Exception e) {
        this.resultCode = resultCode.getResultCode();
        this.resultData = resultData;
        this.resultMsg = resultMsg;
        this.exception = e;
    }


    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public Object getResultData() {
        return resultData;
    }

    public void setResultData(Object resultData) {
        this.resultData = resultData;
    }


    public static void main(String[] args) {

    }
}
