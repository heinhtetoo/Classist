package com.heinhtetoo.myclass.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.heinhtetoo.myclass.R;
import com.heinhtetoo.myclass.controllers.ClassroomItemController;
import com.heinhtetoo.myclass.data.vos.ClassroomVO;
import com.heinhtetoo.myclass.views.viewholders.ClassroomVH;

public class ClassroomListAdapter extends BaseRecyclerAdapter<ClassroomVH, ClassroomVO> {

    private ClassroomItemController mClassroomItemController;

    public ClassroomListAdapter(Context context, ClassroomItemController mClassroomItemController) {
        super(context);
        this.mClassroomItemController = mClassroomItemController;
    }

    @NonNull
    @Override
    public ClassroomVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_classroom, parent, false);
        return new ClassroomVH(view, mClassroomItemController);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassroomVH holder, int position) {
        holder.bind(mData.get(position));
    }
}
