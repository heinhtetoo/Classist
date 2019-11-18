package com.heinhtetoo.myclass.data.vos;

public class ScheduleVO {
    private DayVO mondaySchedule;

    private DayVO tuesdaySchedule;

    private DayVO wednesdaySchedule;

    private DayVO thursdaySchedule;

    private DayVO fridaySchedule;

    public ScheduleVO() {
    }

    public ScheduleVO(DayVO mondaySchedule, DayVO tuesdaySchedule, DayVO wednesdaySchedule, DayVO thursdaySchedule, DayVO fridaySchedule) {
        this.mondaySchedule = mondaySchedule;
        this.tuesdaySchedule = tuesdaySchedule;
        this.wednesdaySchedule = wednesdaySchedule;
        this.thursdaySchedule = thursdaySchedule;
        this.fridaySchedule = fridaySchedule;
    }

    public DayVO getMondaySchedule() {
        return mondaySchedule;
    }

    public void setMondaySchedule(DayVO mondaySchedule) {
        this.mondaySchedule = mondaySchedule;
    }

    public DayVO getTuesdaySchedule() {
        return tuesdaySchedule;
    }

    public void setTuesdaySchedule(DayVO tuesdaySchedule) {
        this.tuesdaySchedule = tuesdaySchedule;
    }

    public DayVO getWednesdaySchedule() {
        return wednesdaySchedule;
    }

    public void setWednesdaySchedule(DayVO wednesdaySchedule) {
        this.wednesdaySchedule = wednesdaySchedule;
    }

    public DayVO getThursdaySchedule() {
        return thursdaySchedule;
    }

    public void setThursdaySchedule(DayVO thursdaySchedule) {
        this.thursdaySchedule = thursdaySchedule;
    }

    public DayVO getFridaySchedule() {
        return fridaySchedule;
    }

    public void setFridaySchedule(DayVO fridaySchedule) {
        this.fridaySchedule = fridaySchedule;
    }
}
