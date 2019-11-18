package com.heinhtetoo.myclass.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.heinhtetoo.myclass.R;
import com.heinhtetoo.myclass.controllers.RollCallRecordItemController;
import com.heinhtetoo.myclass.data.vos.RecordVO;
import com.heinhtetoo.myclass.views.viewholders.RollCallRecordVH;

public class RollCallRecordListAdapter extends BaseRecyclerAdapter<RollCallRecordVH, RecordVO> {

    private RollCallRecordItemController mRollCallRecordItemController;

    public RollCallRecordListAdapter(Context context, RollCallRecordItemController mRollCallRecordItemController) {
        super(context);
        this.mRollCallRecordItemController = mRollCallRecordItemController;
    }

    @NonNull
    @Override
    public RollCallRecordVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_roll_call_record, parent, false);
        return new RollCallRecordVH(view, mRollCallRecordItemController);
    }

    @Override
    public void onBindViewHolder(@NonNull RollCallRecordVH holder, int position) {
        holder.bind(mData.get(position));
    }
}
