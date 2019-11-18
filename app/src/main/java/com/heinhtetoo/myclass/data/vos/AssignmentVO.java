package com.heinhtetoo.myclass.data.vos;

public class AssignmentVO {

    private String assignmentId;

    private String assignmentName;

    private String assignmentModuleCode;

    private String assignmentCreatedDate;

    public AssignmentVO() {
    }

    public AssignmentVO(String assignmentId, String assignmentName, String assignmentModuleCode, String assignmentCreatedDate) {
        this.assignmentId = assignmentId;
        this.assignmentName = assignmentName;
        this.assignmentModuleCode = assignmentModuleCode;
        this.assignmentCreatedDate = assignmentCreatedDate;
    }

    public String getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public String getAssignmentModuleCode() {
        return assignmentModuleCode;
    }

    public void setAssignmentModuleCode(String assignmentModuleCode) {
        this.assignmentModuleCode = assignmentModuleCode;
    }

    public String getAssignmentCreatedDate() {
        return assignmentCreatedDate;
    }

    public void setAssignmentCreatedDate(String assignmentCreatedDate) {
        this.assignmentCreatedDate = assignmentCreatedDate;
    }

    public static AssignmentVO initAssignment(String name, String code, String date) {
        AssignmentVO assignment = new AssignmentVO();
        assignment.assignmentName = name;
        assignment.assignmentModuleCode = code;
        assignment.assignmentCreatedDate = date;

        assignment.assignmentId = String.valueOf((int)(System.currentTimeMillis() / 1000));

        return assignment;
    }
}
