package com.heinhtetoo.myclass.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.heinhtetoo.myclass.R;
import com.heinhtetoo.myclass.controllers.StudentItemController;
import com.heinhtetoo.myclass.data.vos.StudentVO;
import com.heinhtetoo.myclass.views.viewholders.StudentVH;

public class StudentListAdapter extends BaseRecyclerAdapter<StudentVH, StudentVO> {

    private StudentItemController mStudentItemController;

    public StudentListAdapter(Context context, StudentItemController mStudentItemController) {
        super(context);
        this.mStudentItemController = mStudentItemController;
    }

    @NonNull
    @Override
    public StudentVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_student, parent, false);
        return new StudentVH(view, mStudentItemController);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentVH holder, int position) {
        holder.bind(mData.get(position));
    }
}
