package com.heinhtetoo.myclass.data.vos;

import java.util.HashMap;

public class StudentVO {

    private String studentId;

    private String studentRoll;

    private String studentName;

    private String studentPhone;

    private HashMap<String, Boolean> studentAttendanceRecord;

    private HashMap<String, Boolean> studentAssignmentRecord;

    private HashMap<String, Boolean> studentTutorialRecord;

    public StudentVO() {
    }

    public StudentVO(String studentId, String studentRoll, String studentName, String studentPhone, HashMap<String, Boolean> studentAttendanceRecord, HashMap<String, Boolean> studentAssignmentRecord, HashMap<String, Boolean> studentTutorialRecord) {
        this.studentId = studentId;
        this.studentRoll = studentRoll;
        this.studentName = studentName;
        this.studentPhone = studentPhone;
        this.studentAttendanceRecord = studentAttendanceRecord;
        this.studentAssignmentRecord = studentAssignmentRecord;
        this.studentTutorialRecord = studentTutorialRecord;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentRoll() {
        return studentRoll;
    }

    public void setStudentRoll(String studentRoll) {
        this.studentRoll = studentRoll;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentPhone() {
        return studentPhone;
    }

    public void setStudentPhone(String studentPhone) {
        this.studentPhone = studentPhone;
    }

    public HashMap<String, Boolean> getStudentAttendanceRecord() {
        if (studentAttendanceRecord == null) {
            studentAttendanceRecord = new HashMap<>();
        }
        return studentAttendanceRecord;
    }

    public void setStudentAttendanceRecord(HashMap<String, Boolean> studentAttendanceRecord) {
        this.studentAttendanceRecord = studentAttendanceRecord;
    }

    public HashMap<String, Boolean> getStudentAssignmentRecord() {
        if (studentAssignmentRecord == null) {
            studentAssignmentRecord = new HashMap<>();
        }
        return studentAssignmentRecord;
    }

    public void setStudentAssignmentRecord(HashMap<String, Boolean> studentAssignmentRecord) {
        this.studentAssignmentRecord = studentAssignmentRecord;
    }

    public HashMap<String, Boolean> getStudentTutorialRecord() {
        if (studentTutorialRecord == null) {
            studentTutorialRecord = new HashMap<>();
        }
        return studentTutorialRecord;
    }

    public void setStudentTutorialRecord(HashMap<String, Boolean> studentTutorialRecord) {
        this.studentTutorialRecord = studentTutorialRecord;
    }

    public static StudentVO initStudent(String roll, String name, String phone) {
        StudentVO student = new StudentVO();
        student.studentRoll = roll;
        student.studentName = name;
        student.studentPhone = phone;
        student.studentAttendanceRecord = new HashMap<>();
        student.studentAssignmentRecord = new HashMap<>();
        student.studentTutorialRecord = new HashMap<>();

        student.studentId = String.valueOf((int)(System.currentTimeMillis() / 1000));

        return student;
    }

    public static StudentVO initStudentWithId(String id, String roll, String name, String phone) {
        StudentVO student = new StudentVO();
        student.studentId = id+roll;
        student.studentRoll = roll;
        student.studentName = name;
        student.studentPhone = phone;
        student.studentAttendanceRecord = new HashMap<>();
        student.studentAssignmentRecord = new HashMap<>();
        student.studentTutorialRecord = new HashMap<>();

        return student;
    }
}
