package com.heinhtetoo.myclass.controllers;

import android.view.View;

import com.heinhtetoo.myclass.data.vos.StudentVO;

public interface StudentItemController extends BaseController {
    void onClickStudent(View view, StudentVO studentVO);
}
