package com.heinhtetoo.myclass.data.vos;

import java.util.ArrayList;
import java.util.List;

public class TutorialVO {

    private String tutorialId;

    private String tutorialName;

    private String tutorialModuleCode;

    private String tutorialCreatedDate;

    private List<String> tutorialStudentIdList;

    public TutorialVO() {
    }

    public TutorialVO(String tutorialId, String tutorialName, String tutorialModuleCode, String tutorialCreatedDate, List<String> tutorialStudentIdList) {
        this.tutorialId = tutorialId;
        this.tutorialName = tutorialName;
        this.tutorialModuleCode = tutorialModuleCode;
        this.tutorialCreatedDate = tutorialCreatedDate;
        this.tutorialStudentIdList = tutorialStudentIdList;
    }

    public String getTutorialId() {
        return tutorialId;
    }

    public void setTutorialId(String tutorialId) {
        this.tutorialId = tutorialId;
    }

    public String getTutorialName() {
        return tutorialName;
    }

    public void setTutorialName(String tutorialName) {
        this.tutorialName = tutorialName;
    }

    public List<String> getTutorialStudentIdList() {
        if (tutorialStudentIdList == null) {
            tutorialStudentIdList = new ArrayList<>();
        }
        return tutorialStudentIdList;
    }

    public void setTutorialStudentIdList(List<String> tutorialStudentIdList) {
        this.tutorialStudentIdList = tutorialStudentIdList;
    }

    public String getTutorialModuleCode() {
        return tutorialModuleCode;
    }

    public void setTutorialModuleCode(String tutorialModuleCode) {
        this.tutorialModuleCode = tutorialModuleCode;
    }

    public String getTutorialCreatedDate() {
        return tutorialCreatedDate;
    }

    public void setTutorialCreatedDate(String tutorialCreatedDate) {
        this.tutorialCreatedDate = tutorialCreatedDate;
    }

    public static TutorialVO initTutorial(String name, String code, String date) {
        TutorialVO tutorial = new TutorialVO();
        tutorial.tutorialName = name;
        tutorial.tutorialModuleCode = code;
        tutorial.tutorialCreatedDate = date;
        tutorial.tutorialStudentIdList = new ArrayList<>();

        tutorial.tutorialId = String.valueOf((int)(System.currentTimeMillis() / 1000));

        return tutorial;
    }
}
