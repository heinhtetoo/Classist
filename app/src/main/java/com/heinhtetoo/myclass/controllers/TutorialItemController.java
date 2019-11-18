package com.heinhtetoo.myclass.controllers;

import android.view.View;

import com.heinhtetoo.myclass.data.vos.TutorialVO;

public interface TutorialItemController extends BaseController {
    void onClickTutorial(View view, TutorialVO assignmentVO);
}
