package com.heinhtetoo.myclass.controllers;

import android.view.View;

import com.heinhtetoo.myclass.data.vos.AssignmentVO;

public interface AssignmentItemController extends BaseController {
    void onClickAssignment(View view, AssignmentVO assignmentVO);
}
