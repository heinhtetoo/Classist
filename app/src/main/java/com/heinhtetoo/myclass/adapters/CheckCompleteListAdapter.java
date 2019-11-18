package com.heinhtetoo.myclass.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.heinhtetoo.myclass.R;
import com.heinhtetoo.myclass.controllers.CheckCompleteItemController;
import com.heinhtetoo.myclass.data.vos.StudentVO;
import com.heinhtetoo.myclass.views.viewholders.CheckCompleteVH;

public class CheckCompleteListAdapter extends BaseRecyclerAdapter<CheckCompleteVH, StudentVO> {

    private CheckCompleteItemController mCheckCompleteItemController;
    private String classroomId;
    private String checkItemId;
    private int checkItemType;

    public CheckCompleteListAdapter(Context context, CheckCompleteItemController mCheckCompleteItemController,String classroomId, String checkItemId, int checkItemType) {
        super(context);
        this.mCheckCompleteItemController = mCheckCompleteItemController;
        this.classroomId = classroomId;
        this.checkItemId = checkItemId;
        this.checkItemType = checkItemType;
    }

    @NonNull
    @Override
    public CheckCompleteVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_check, parent, false);
        return new CheckCompleteVH(view, mCheckCompleteItemController,classroomId, checkItemId, checkItemType);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckCompleteVH holder, int position) {
        holder.bind(mData.get(position));
    }
}
