package com.heinhtetoo.myclass.controllers;

import android.view.View;

import com.heinhtetoo.myclass.data.vos.ClassroomVO;

public interface ClassroomItemController extends BaseController {
    void onClickClassroom(View view, ClassroomVO classroomVO);
}
