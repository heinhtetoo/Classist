package com.heinhtetoo.myclass.controllers;

import android.view.View;

import com.heinhtetoo.myclass.data.vos.StudentVO;

public interface CheckCompleteItemController extends BaseController {
    void onClickCheckItem(View view, StudentVO studentVO);
}
