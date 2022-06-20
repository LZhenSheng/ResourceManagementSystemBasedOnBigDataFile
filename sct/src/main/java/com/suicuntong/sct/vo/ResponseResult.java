package com.suicuntong.sct.vo;

import lombok.Data;

@Data
public class ResponseResult {
    private String code;
    private String message;
    private String data;

    public ResponseResult() {
    }

    public ResponseResult(String code, String message) {
        this.code = code;
        this.message = message;
    }
    public String getCode() { return code; }
    public void setCode(String s) {
        code = s;
    }

    public String getMessage() { return message; }
    public void setMessage(String s) {
        message = s;
    }
}