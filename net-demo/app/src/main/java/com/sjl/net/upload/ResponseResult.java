package com.sjl.net.upload;

/**
 * TODO
 *
 * @author song
 * @version 1.0.0
 * @filename UploadResult.java
 * @time 2018-2-3 下午1:23:35
 * @copyright(C) 2018 song
 */
public class ResponseResult {
    private String resultCode;
    private String resultMsg;

    public ResponseResult(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    @Override
    public String toString() {
        return "ResponseResult{" +
                "resultCode='" + resultCode + '\'' +
                ", resultMsg='" + resultMsg + '\'' +
                '}';
    }
}
