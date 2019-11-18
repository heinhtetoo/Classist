package com.heinhtetoo.myclass.views.viewholders;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.heinhtetoo.myclass.R;
import com.heinhtetoo.myclass.controllers.CheckCompleteItemController;
import com.heinhtetoo.myclass.data.models.ClassroomModel;
import com.heinhtetoo.myclass.data.vos.StudentVO;

import butterknife.Bind;
import butterknife.OnClick;

public class CheckCompleteVH extends BaseViewHolder<StudentVO> {

    private StudentVO mStudent;
    private CheckCompleteItemController mCheckCompleteItemController;
    private String classroomId;
    private String checkItemId;
    private int checkItemType;

    @Bind(R.id.tv_check_roll)
    TextView tvCheckRoll;

    @Bind(R.id.tv_check_name)
    TextView tvCheckName;

    @Bind(R.id.btn_complete_check)
    MaterialButton btnCompleteCheck;

    public CheckCompleteVH(View itemView, CheckCompleteItemController mCheckCompleteItemController, String classroomId, String checkItemId, int checkItemType) {
        super(itemView);
        this.mCheckCompleteItemController = mCheckCompleteItemController;
        this.classroomId = classroomId;
        this.checkItemId = checkItemId;
        this.checkItemType = checkItemType;
    }

    @Override
    public void bind(StudentVO data) {
        mStudent = data;
        tvCheckRoll.setText(mStudent.getStudentRoll());
        tvCheckName.setText(mStudent.getStudentName());

        switch (checkItemType) {
            case 1: {
                if (mStudent.getStudentAssignmentRecord().get(checkItemId) == null) {
                    excludedCheck();
                } else if (mStudent.getStudentAssignmentRecord().get(checkItemId)) {
                    completeCheck();
                } else {
                    incompleteCheck();
                }
                break;
            }
            case 2: {
                if (mStudent.getStudentTutorialRecord().get(checkItemId) == null) {
                    excludedCheck();
                } else if (mStudent.getStudentTutorialRecord().get(checkItemId)) {
                    completeCheck();
                } else {
                    incompleteCheck();
                }
                break;
            }
        }
    }

    @OnClick(R.id.btn_complete_check)
    public void onClickCompleteCheck() {
        switch (checkItemType) {
            case 1: {
                if (mStudent.getStudentAssignmentRecord().get(checkItemId)) {
                    mStudent.getStudentAssignmentRecord().put(checkItemId, false);
                    ClassroomModel.getInstance().updateStudentAssignmentByIdAndAssignmentIdAndClassroomId(classroomId, mStudent.getStudentId(),
                            checkItemId, false);
                    incompleteCheck();
                } else {
                    mStudent.getStudentAssignmentRecord().put(checkItemId, true);
                    ClassroomModel.getInstance().updateStudentAssignmentByIdAndAssignmentIdAndClassroomId(classroomId, mStudent.getStudentId(),
                            checkItemId, true);
                    completeCheck();
                }
                break;
            }
            case 2: {
                if (mStudent.getStudentTutorialRecord().get(checkItemId)) {
                    mStudent.getStudentTutorialRecord().put(checkItemId, false);
                    ClassroomModel.getInstance().updateStudentTutorialByIdAndTutorialIdAndClassroomId(classroomId, mStudent.getStudentId(),
                            checkItemId, false);
                    incompleteCheck();
                } else {
                    mStudent.getStudentTutorialRecord().put(checkItemId, true);
                    ClassroomModel.getInstance().updateStudentTutorialByIdAndTutorialIdAndClassroomId(classroomId, mStudent.getStudentId(),
                            checkItemId, true);
                    completeCheck();
                }
                break;
            }
        }
    }

    public void excludedCheck() {
        btnCompleteCheck.setText("Excluded");
        btnCompleteCheck.setEnabled(false);
        btnCompleteCheck.setClickable(false);
        btnCompleteCheck.setTextColor(Color.parseColor("#808080"));
    }

    public void incompleteCheck() {
        btnCompleteCheck.setText("Incomplete");
        btnCompleteCheck.setEnabled(true);
        btnCompleteCheck.setClickable(true);
        btnCompleteCheck.setTextColor(Color.parseColor("#D50000"));
    }

    public void completeCheck() {
        btnCompleteCheck.setText("Complete");
        btnCompleteCheck.setEnabled(true);
        btnCompleteCheck.setClickable(true);
        btnCompleteCheck.setTextColor(Color.parseColor("#00C853"));
    }

    @Override
    public void onClick(View view) {
        mCheckCompleteItemController.onClickCheckItem(view, mStudent);
    }
}
