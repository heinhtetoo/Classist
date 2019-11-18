package com.heinhtetoo.myclass.views.viewholders;

import android.view.View;
import android.widget.TextView;

import com.heinhtetoo.myclass.R;
import com.heinhtetoo.myclass.controllers.ClassroomItemController;
import com.heinhtetoo.myclass.data.vos.ClassroomVO;

import butterknife.Bind;

public class ClassroomVH extends BaseViewHolder<ClassroomVO> {

    private ClassroomVO mClassroom;
    private ClassroomItemController mClassroomItemController;

    @Bind(R.id.tv_classroom_name)
    TextView tvClassroom;

    @Bind(R.id.tv_student_count_classroom_item)
    TextView tvStudentCount;

    public ClassroomVH(View itemView, ClassroomItemController mClassroomItemController) {
        super(itemView);
        this.mClassroomItemController = mClassroomItemController;
    }

    @Override
    public void bind(ClassroomVO data) {
        mClassroom = data;
        tvClassroom.setText(mClassroom.getClassroomName());
        tvStudentCount.setText(String.valueOf(mClassroom.getStudentVOList().size()));
    }

    @Override
    public void onClick(View view) {
        mClassroomItemController.onClickClassroom(view, mClassroom);
    }
}
