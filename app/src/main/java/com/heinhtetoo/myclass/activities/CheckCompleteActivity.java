package com.heinhtetoo.myclass.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.heinhtetoo.myclass.R;
import com.heinhtetoo.myclass.adapters.CheckCompleteListAdapter;
import com.heinhtetoo.myclass.controllers.CheckCompleteItemController;
import com.heinhtetoo.myclass.data.models.ClassroomModel;
import com.heinhtetoo.myclass.data.vos.StudentVO;
import com.heinhtetoo.myclass.events.DataEvents;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckCompleteActivity extends AppCompatActivity implements CheckCompleteItemController {

    private static final String CC_ID = "classroomId";
    private static final String CC_ITEM_ID = "itemId";
    private static final String CC_ITEM_NAME = "itemName";
    private static final String CC_ITEM_TYPE = "itemType";

    private CheckCompleteListAdapter mCheckCompleteListAdapter;
    private CheckCompleteItemController mController;

    private List<StudentVO> mStudentList = new ArrayList<>();

    private ClassroomModel mClassroomModel;

    private String classroomId;
    private String checkItemId;
    private String checkItemName;
    private int checkItemType;

    @Bind(R.id.ic_back_check)
    ImageView icBack;

    @Bind(R.id.rv_check_list)
    RecyclerView rvCheck;

    @Bind(R.id.title_check)
    TextView tvTitle;

    @Bind(R.id.tv_no_check)
    TextView tvNoCheck;

    public static Intent newIntent(Context context, String classroomId, String checkId, String checkName, int type) {
        Intent intent = new Intent(context, CheckCompleteActivity.class);
        intent.putExtra(CC_ID, classroomId);
        intent.putExtra(CC_ITEM_ID, checkId);
        intent.putExtra(CC_ITEM_NAME, checkName);
        intent.putExtra(CC_ITEM_TYPE, type);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_complete);
        ButterKnife.bind(this, this);

        classroomId = getIntent().getStringExtra(CC_ID);
        checkItemId = getIntent().getStringExtra(CC_ITEM_ID);
        checkItemName = getIntent().getStringExtra(CC_ITEM_NAME);
        checkItemType = getIntent().getIntExtra(CC_ITEM_TYPE, 0);

        mController = (CheckCompleteItemController) this;
        mClassroomModel = ClassroomModel.getInstance();

        tvTitle.setText(checkItemName);

        setupCheckCompleteRecyclerView();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        mClassroomModel.loadClassrooms();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        mCheckCompleteListAdapter.clearData();
    }

    @OnClick(R.id.ic_back_check)
    public void onClickBack() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        ClassroomModel.getInstance().loadClassrooms();
        super.onBackPressed();
    }

    private void setupCheckCompleteRecyclerView() {
        mCheckCompleteListAdapter = new CheckCompleteListAdapter(this, mController,classroomId, checkItemId, checkItemType);
        rvCheck.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvCheck.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        rvCheck.setAdapter(mCheckCompleteListAdapter);
    }

    public boolean isStudentListEmpty() {
        return mCheckCompleteListAdapter == null || mCheckCompleteListAdapter.getItemCount() == 0;
    }

    public void displayCheckItemList(List<StudentVO> studentList, boolean isToAppend) {
        if (isToAppend) {
            mCheckCompleteListAdapter.appendNewData(studentList);
        } else {
            mCheckCompleteListAdapter.setNewData(studentList);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnClassroomListLoaded(DataEvents.ClassroomListLoadedEvent event) {
        mStudentList = new ArrayList<>(event.getClassroomList().get(classroomId).getStudentVOList().values());
        if (mStudentList.isEmpty()) {
            tvNoCheck.setVisibility(View.VISIBLE);
        } else {
            tvNoCheck.setVisibility(View.GONE);
            displayCheckItemList(mStudentList, false);
        }
    }

    @Override
    public void onClickCheckItem(View view, StudentVO studentVO) {

    }
}
