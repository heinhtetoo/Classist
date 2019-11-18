package com.heinhtetoo.myclass.data.vos;

public class UserVO {

    private String accountId;

    private String displayName;

    private String email;

    private String photoUrl;

    private String phone;

    private String gender;

    private String dateOfBirth;

    private String coverPhoto;

    private Integer assignmentNo;

    private int tutorialNo;

    private int studentNo;

    private boolean isScheduleUpdated;

    private ScheduleVO schedule;
    public UserVO() {
    }

    public UserVO(String accountId, String displayName, String email, String photoUrl, String phone, String gender, String dateOfBirth, String coverPhoto, Integer assignmentNo, int tutorialNo, int studentNo, boolean isScheduleUpdated, ScheduleVO schedule) {
        this.accountId = accountId;
        this.displayName = displayName;
        this.email = email;
        this.photoUrl = photoUrl;
        this.phone = phone;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.coverPhoto = coverPhoto;
        this.assignmentNo = assignmentNo;
        this.tutorialNo = tutorialNo;
        this.studentNo = studentNo;
        this.isScheduleUpdated = isScheduleUpdated;
        this.schedule = schedule;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getDisplayName() {
        if (displayName != null) {
            return displayName;
        } else {
            return "User";
        }
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        if (email != null) {
            return email;
        } else {
            return "Unavailable";
        }
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getPhone() {
        if (phone != null) {
            return phone;
        } else {
            return "Unavailable";
        }
    }

    public String getGender() {
        if (gender != null) {
            return gender;
        } else {
            return "unavailable";
        }
    }

    public String getDateOfBirth() {
        if (dateOfBirth != null) {
            return dateOfBirth;
        } else {
            return "unavailable";
        }
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public int getAssignmentNo() {
        return assignmentNo;
    }

    public void setAssignmentNo(int assignmentNo) {
        this.assignmentNo = assignmentNo;
    }

    public int getTutorialNo() {
        return tutorialNo;
    }

    public void setTutorialNo(int tutorialNo) {
        this.tutorialNo = tutorialNo;
    }

    public int getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(int studentNo) {
        this.studentNo = studentNo;
    }

    public boolean isScheduleUpdated() {
        return isScheduleUpdated;
    }

    public void setScheduleUpdated(boolean scheduleUpdated) {
        isScheduleUpdated = scheduleUpdated;
    }

    public ScheduleVO getSchedule() {
        return schedule;
    }

    public void setSchedule(ScheduleVO schedule) {
        this.schedule = schedule;
    }
}
