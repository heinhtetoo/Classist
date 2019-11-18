package com.heinhtetoo.myclass.data.vos;

public class ModuleVO {

    private String code;

    private String name;

    public ModuleVO() {
    }

    public ModuleVO(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
