package com.heinhtetoo.myclass.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.heinhtetoo.myclass.R;
import com.heinhtetoo.myclass.controllers.AssignmentItemController;
import com.heinhtetoo.myclass.data.vos.AssignmentVO;
import com.heinhtetoo.myclass.views.viewholders.AssignmentVH;

public class AssignmentListAdapter extends BaseRecyclerAdapter<AssignmentVH, AssignmentVO> {

    private AssignmentItemController mAssignmentItemController;

    public AssignmentListAdapter(Context context, AssignmentItemController mAssignmentItemController) {
        super(context);
        this.mAssignmentItemController = mAssignmentItemController;
    }

    @NonNull
    @Override
    public AssignmentVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_assignment, parent, false);
        return new AssignmentVH(view, mAssignmentItemController);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentVH holder, int position) {
        holder.bind(mData.get(position));
    }
}
