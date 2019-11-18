package com.heinhtetoo.myclass.data.vos;

import java.util.HashMap;

public class ClassroomVO {

    private String classroomId;

    private String classroomName;

    private HashMap<String, StudentVO> studentVOList;

    private HashMap<String, AssignmentVO> assignmentVOList;

    private HashMap<String, TutorialVO> tutorialVOList;

    public ClassroomVO() {
    }

    public ClassroomVO(String classroomId, String classroomName, HashMap<String, StudentVO> studentVOList, HashMap<String, AssignmentVO> assignmentVOList, HashMap<String, TutorialVO> tutorialVOList) {
        this.classroomId = classroomId;
        this.classroomName = classroomName;
        this.studentVOList = studentVOList;
        this.assignmentVOList = assignmentVOList;
        this.tutorialVOList = tutorialVOList;
    }

    public String getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(String classroomId) {
        this.classroomId = classroomId;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public HashMap<String, StudentVO> getStudentVOList() {
        if (studentVOList == null) {
            studentVOList = new HashMap<>();
        }
        return studentVOList;
    }

    public void setStudentVOList(HashMap<String, StudentVO> studentVOList) {
        this.studentVOList = studentVOList;
    }

    public HashMap<String, AssignmentVO> getAssignmentVOList() {
        if (assignmentVOList == null) {
            assignmentVOList = new HashMap<>();
        }
        return assignmentVOList;
    }

    public void setAssignmentVOList(HashMap<String, AssignmentVO> assignmentVOList) {
        this.assignmentVOList = assignmentVOList;
    }

    public HashMap<String, TutorialVO> getTutorialVOList() {
        if (tutorialVOList == null) {
            tutorialVOList = new HashMap<>();
        }
        return tutorialVOList;
    }

    public void setTutorialVOList(HashMap<String, TutorialVO> tutorialVOList) {
        this.tutorialVOList = tutorialVOList;
    }

    public static ClassroomVO initClassroom(String name) {
        ClassroomVO classroom = new ClassroomVO();
        classroom.classroomName = name;
        classroom.studentVOList = new HashMap<>();
        classroom.assignmentVOList = new HashMap<>();
        classroom.tutorialVOList = new HashMap<>();

        classroom.classroomId = String.valueOf((int) (System.currentTimeMillis() / 1000));

        return classroom;
    }
}
