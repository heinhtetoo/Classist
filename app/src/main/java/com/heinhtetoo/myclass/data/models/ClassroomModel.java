package com.heinhtetoo.myclass.data.models;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.heinhtetoo.myclass.data.vos.AssignmentVO;
import com.heinhtetoo.myclass.data.vos.ClassroomVO;
import com.heinhtetoo.myclass.data.vos.StudentVO;
import com.heinhtetoo.myclass.data.vos.TutorialVO;
import com.heinhtetoo.myclass.events.DataEvents;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClassroomModel {

    private static final String CLASS_RECORDS = "class_records";
    private static final String CLASSROOM = "classrooms";
    private static final String PRERECORDED_LIST = "prerecorded_student_list";
    private static final String STUDENT_LIST = "studentVOList";
    private static final String ASSIGNMENT_LIST = "assignmentVOList";
    private static final String TUTORIAL_LIST = "tutorialVOList";
    private static final String STUDENT_ASSIGNMENT_RECORD = "studentAssignmentRecord";
    private static final String STUDENT_TUTORIAL_RECORD = "studentTutorialRecord";

    private static ClassroomModel objInstance;

    public List<ClassroomVO> classroomList;

    private DatabaseReference mDatabaseReference;
    private DatabaseReference mClassroomDr;

    public static ClassroomModel getInstance() {
        if (objInstance == null) {
            objInstance = new ClassroomModel();
        }
        return objInstance;
    }

    private ClassroomModel() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        classroomList = new ArrayList<>();
    }

    public void addClassroom(String name) {
        ClassroomVO classroom = ClassroomVO.initClassroom(name);
        mClassroomDr.child(String.valueOf(classroom.getClassroomId())).setValue(classroom);
    }

    public void addStudentByClassroomId(String classroomId, String roll, String name, String phone, HashMap<String, Boolean> rollCall) {
        StudentVO student = StudentVO.initStudent(roll, name, phone);
        ArrayList<String> oldRollCallList = new ArrayList<>(rollCall.keySet());
        for (int i = 0; i < oldRollCallList.size(); i++) {
            student.getStudentAttendanceRecord().put(oldRollCallList.get(i), false);
        }
        mClassroomDr.child(classroomId).child(STUDENT_LIST).child(student.getStudentId()).setValue(student);
    }

    public void addStudentByClassroomIdAndStudentId(String classroomId,String id, String roll, String name, String phone, HashMap<String, Boolean> rollCall) {
        StudentVO student = StudentVO.initStudentWithId(id, roll, name, phone);
        ArrayList<String> oldRollCallList = new ArrayList<>(rollCall.keySet());
        for (int i = 0; i < oldRollCallList.size(); i++) {
            student.getStudentAttendanceRecord().put(oldRollCallList.get(i), false);
        }
        mClassroomDr.child(classroomId).child(STUDENT_LIST).child(student.getStudentId()).setValue(student);
    }

    public String addAssignmentByClassroomId(String classroomId, String name, String code, String date) {
        AssignmentVO assignment = AssignmentVO.initAssignment(name, code, date);
        mClassroomDr.child(classroomId).child(ASSIGNMENT_LIST).child(assignment.getAssignmentId()).setValue(assignment);
        return assignment.getAssignmentId();
    }

    public String addTutorialByClassroomId(String classroomId, String name, String code, String date) {
        TutorialVO tutorial = TutorialVO.initTutorial(name, code, date);
        mClassroomDr.child(classroomId).child(TUTORIAL_LIST).child(tutorial.getTutorialId()).setValue(tutorial);
        return tutorial.getTutorialId();
    }

    public void updateStudentByIdAndClassroomId(String classroomId, String studentId, StudentVO student) {
        mClassroomDr.child(classroomId).child(STUDENT_LIST).child(studentId).setValue(student);
    }

    public void updateStudentAssignmentByIdAndAssignmentIdAndClassroomId(String classroomId, String studentId,
                                                                         String assignmentId, boolean check) {
        mClassroomDr.child(classroomId).child(STUDENT_LIST).child(studentId).child(STUDENT_ASSIGNMENT_RECORD)
                .child(assignmentId).setValue(check);
    }

    public void updateStudentTutorialByIdAndTutorialIdAndClassroomId(String classroomId, String studentId,
                                                                     String tutorialId, boolean check) {
        mClassroomDr.child(classroomId).child(STUDENT_LIST).child(studentId).child(STUDENT_TUTORIAL_RECORD)
                .child(tutorialId).setValue(check);
    }

    public void loadPrerecordedStudentList() {
        mClassroomDr = mDatabaseReference.child(PRERECORDED_LIST);
        final HashMap<String, ClassroomVO> classroomVOS = new HashMap<>();
        mClassroomDr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ClassroomVO classroom = snapshot.getValue(ClassroomVO.class);
                        classroomVOS.put(classroom.getClassroomId(), classroom);
                    }

                    DataEvents.PrerecordedStudentListLoadedEvent event = new DataEvents.PrerecordedStudentListLoadedEvent(classroomVOS);
                    EventBus.getDefault().post(event);

                    classroomList = new ArrayList<>(classroomVOS.values());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    public void changeDatabaseReferenceToClassroom() {
        mClassroomDr = mDatabaseReference.child(CLASS_RECORDS).child(UserModel.getInstance().getUser()
                .getAccountId()).child(CLASSROOM);
    }

    public void changeDatabaseReferenceToPrerecordedList() {
        mClassroomDr = mDatabaseReference.child(PRERECORDED_LIST);
    }

    public void loadClassrooms() {
        mClassroomDr = mDatabaseReference.child(CLASS_RECORDS)
                .child(UserModel.getInstance().getUser().getAccountId()).child(CLASSROOM);
        final HashMap<String, ClassroomVO> classroomVOS = new HashMap<>();
        mClassroomDr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ClassroomVO classroom = snapshot.getValue(ClassroomVO.class);
                        classroomVOS.put(classroom.getClassroomId(), classroom);
                    }

                    DataEvents.ClassroomListLoadedEvent event = new DataEvents.ClassroomListLoadedEvent(classroomVOS);
                    EventBus.getDefault().post(event);

                    classroomList = new ArrayList<>(classroomVOS.values());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    public List<ClassroomVO> getClassroomList() {
        if (classroomList == null) {
            classroomList = new ArrayList<>();
        }
        return classroomList;
    }

    public ClassroomVO getClassroomById(String id) {
        ClassroomVO classroom = new ClassroomVO();
        for (ClassroomVO classroomVO : classroomList) {
            if (classroomVO.getClassroomId() == id) {
                classroom = classroomVO;
            }
        }
        return classroom;
    }
}
