package com.heinhtetoo.myclass.views.viewholders;

import android.view.View;
import android.widget.TextView;

import com.heinhtetoo.myclass.R;
import com.heinhtetoo.myclass.controllers.TutorialItemController;
import com.heinhtetoo.myclass.data.vos.TutorialVO;

import butterknife.Bind;

public class TutorialVH extends BaseViewHolder<TutorialVO> {

    private TutorialVO mTutorial;
    private TutorialItemController mTutorialItemController;

    @Bind(R.id.tv_assign_tuto_name)
    TextView tvName;

    @Bind(R.id.tv_assig_tuto_module)
    TextView tvModule;

    @Bind(R.id.tv_assign_tuto_date)
    TextView tvDate;

    public TutorialVH(View itemView, TutorialItemController mTutorialItemController) {
        super(itemView);
        this.mTutorialItemController = mTutorialItemController;
    }

    @Override
    public void bind(TutorialVO data) {
        mTutorial = data;
        tvName.setText(mTutorial.getTutorialName());
        tvModule.setText(mTutorial.getTutorialModuleCode());
        tvDate.setText(mTutorial.getTutorialCreatedDate() + " ");
    }

    @Override
    public void onClick(View view) {
        mTutorialItemController.onClickTutorial(view, mTutorial);
    }
}
