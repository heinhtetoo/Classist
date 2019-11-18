package com.heinhtetoo.myclass.events;

import com.heinhtetoo.myclass.data.vos.ClassroomVO;
import com.heinhtetoo.myclass.data.vos.UserVO;

import java.util.HashMap;

/**
 * Created by Hein Htet Oo on 11/28/2017.
 */

public class DataEvents {

    public static class UserDataLoadedEvent {
        private UserVO userVO;

        public UserDataLoadedEvent(UserVO userVO) {
            this.userVO = userVO;
        }

        public UserVO getUserVO() {
            return userVO;
        }
    }

    public static class ClassroomListLoadedEvent {
        private HashMap<String, ClassroomVO> classroomList;

        public ClassroomListLoadedEvent(HashMap<String, ClassroomVO> classroomList) {
            this.classroomList = classroomList;
        }

        public HashMap<String, ClassroomVO> getClassroomList() {
            return classroomList;
        }
    }

    public static class PrerecordedStudentListLoadedEvent {
        private HashMap<String, ClassroomVO> classroomList;

        public PrerecordedStudentListLoadedEvent(HashMap<String, ClassroomVO> classroomList) {
            this.classroomList = classroomList;
        }

        public HashMap<String, ClassroomVO> getClassroomList() {
            return classroomList;
        }
    }

}
