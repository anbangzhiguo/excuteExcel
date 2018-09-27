package com.excuteaddress.demo;

import java.io.Serializable;

public class AddressExcel implements Serializable{
    private String code;
    private String excutedata;
    private String requirement;
    private String suggestion;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getExcutedata() {
        return excutedata;
    }

    public void setExcutedata(String excutedata) {
        this.excutedata = excutedata;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }
}
