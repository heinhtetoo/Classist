package com.heinhtetoo.myclass.controllers;

import android.view.View;

import com.heinhtetoo.myclass.data.vos.RecordVO;

public interface RollCallRecordItemController extends BaseController {
    void onClickRecord(View view, RecordVO record);
}
