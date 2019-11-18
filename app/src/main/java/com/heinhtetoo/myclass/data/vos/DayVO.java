package com.heinhtetoo.myclass.data.vos;

import java.util.List;

public class DayVO {

    private int DayId;

    private List<ModuleVO> moduleVOList;

    public DayVO() {
    }

    public DayVO(int dayId, List<ModuleVO> moduleVOList) {
        DayId = dayId;
        this.moduleVOList = moduleVOList;
    }

    public int getDayId() {
        return DayId;
    }

    public void setDayId(int dayId) {
        DayId = dayId;
    }

    public List<ModuleVO> getModuleVOList() {
        return moduleVOList;
    }

    public void setModuleVOList(List<ModuleVO> moduleVOList) {
        this.moduleVOList = moduleVOList;
    }
}
