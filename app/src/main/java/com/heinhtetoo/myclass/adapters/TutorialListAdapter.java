package com.heinhtetoo.myclass.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.heinhtetoo.myclass.R;
import com.heinhtetoo.myclass.controllers.TutorialItemController;
import com.heinhtetoo.myclass.data.vos.TutorialVO;
import com.heinhtetoo.myclass.views.viewholders.TutorialVH;

public class TutorialListAdapter extends BaseRecyclerAdapter<TutorialVH, TutorialVO> {

    private TutorialItemController mTutorialItemController;

    public TutorialListAdapter(Context context, TutorialItemController mTutorialItemController) {
        super(context);
        this.mTutorialItemController = mTutorialItemController;
    }

    @NonNull
    @Override
    public TutorialVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_assignment, parent, false);
        return new TutorialVH(view, mTutorialItemController);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorialVH holder, int position) {
        holder.bind(mData.get(position));
    }
}
