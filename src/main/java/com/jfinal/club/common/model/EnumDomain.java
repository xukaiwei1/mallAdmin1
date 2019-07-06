package com.jfinal.club.common.model;

public class EnumDomain {
    private  Integer code;
    private String type;
    private  String  name;

    public EnumDomain(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public EnumDomain(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
