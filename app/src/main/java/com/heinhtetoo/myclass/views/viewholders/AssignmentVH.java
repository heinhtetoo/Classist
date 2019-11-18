package com.heinhtetoo.myclass.views.viewholders;

import android.view.View;
import android.widget.TextView;

import com.heinhtetoo.myclass.R;
import com.heinhtetoo.myclass.controllers.AssignmentItemController;
import com.heinhtetoo.myclass.data.vos.AssignmentVO;

import butterknife.Bind;

public class AssignmentVH extends BaseViewHolder<AssignmentVO> {

    private AssignmentVO mAssignment;
    private AssignmentItemController mAssignmentItemController;

    @Bind(R.id.tv_assign_tuto_name)
    TextView tvName;

    @Bind(R.id.tv_assig_tuto_module)
    TextView tvModule;

    @Bind(R.id.tv_assign_tuto_date)
    TextView tvDate;

    public AssignmentVH(View itemView, AssignmentItemController mAssignmentItemController) {
        super(itemView);
        this.mAssignmentItemController = mAssignmentItemController;
    }

    @Override
    public void bind(AssignmentVO data) {
        mAssignment = data;
        tvName.setText(mAssignment.getAssignmentName());
        tvModule.setText(mAssignment.getAssignmentModuleCode());
        tvDate.setText(mAssignment.getAssignmentCreatedDate() + " ");
    }

    @Override
    public void onClick(View view) {
        mAssignmentItemController.onClickAssignment(view, mAssignment);
    }
}
