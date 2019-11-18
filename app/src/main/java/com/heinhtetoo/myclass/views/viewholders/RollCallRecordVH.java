package com.heinhtetoo.myclass.views.viewholders;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.heinhtetoo.myclass.R;
import com.heinhtetoo.myclass.controllers.RollCallRecordItemController;
import com.heinhtetoo.myclass.data.vos.RecordVO;

import butterknife.Bind;

public class RollCallRecordVH extends BaseViewHolder<RecordVO> {

    private RecordVO mRecord;
    private RollCallRecordItemController mRollCallRecordItemController;

    @Bind(R.id.tv_roll_call_date)
    TextView tvDate;

    @Bind(R.id.tv_roll_call_record)
    TextView tvRecord;

    public RollCallRecordVH(View itemView, RollCallRecordItemController mRollCallRecordItemController) {
        super(itemView);
        this.mRollCallRecordItemController = mRollCallRecordItemController;
    }

    @Override
    public void bind(RecordVO data) {
        mRecord = data;
        tvDate.setText(mRecord.getRecordId());

        if (mRecord.getRecordValue() == null) {
            tvRecord.setText("No Record ");
        } else if (mRecord.getRecordValue()) {
            tvRecord.setText("Present");
            tvRecord.setTextColor(Color.parseColor("#00C853"));
        } else {
            tvRecord.setText("Absent");
            tvRecord.setTextColor(Color.parseColor("#D50000"));
        }
    }

    @Override
    public void onClick(View view) {
        mRollCallRecordItemController.onClickRecord(view, mRecord);
    }
}
