package com.heinhtetoo.myclass.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.heinhtetoo.myclass.R;
import com.heinhtetoo.myclass.adapters.ClassroomListAdapter;
import com.heinhtetoo.myclass.controllers.ClassroomItemController;
import com.heinhtetoo.myclass.data.models.ClassroomModel;
import com.heinhtetoo.myclass.data.vos.ClassroomVO;
import com.heinhtetoo.myclass.events.DataEvents;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClassroomActivity extends AppCompatActivity implements ClassroomItemController {

    private static final String CR_ID = "classroomId";

    private ClassroomListAdapter mClassroomListAdapter;
    private ClassroomItemController mController;

    private List<ClassroomVO> mClassroomList = new ArrayList<>();

    private ClassroomModel mClassroomModel;

    private int screenType = 0;

    public ProgressDialog mProgressDialog;

    TextInputEditText edtName;
    MaterialButton btnSave;
    MaterialButton btnCancel;

    @Bind(R.id.btn_new_classroom)
    MaterialButton btnAddClassroom;

    @Bind(R.id.rv_classroom)
    RecyclerView rvClassroom;

    public static Intent newIntent(Context context, int id) {
        Intent intent = new Intent(context, ClassroomActivity.class);
        intent.putExtra(CR_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom);
        ButterKnife.bind(this, this);

        screenType = getIntent().getIntExtra(CR_ID, 0);

        mController = (ClassroomItemController) this;
        mClassroomModel = ClassroomModel.getInstance();

        if (screenType != 3) {
            btnAddClassroom.setVisibility(View.GONE);
        }

        setupClassroomRecyclerView();
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
        mClassroomListAdapter.clearData();
    }

    @OnClick(R.id.ic_back_classroom)
    public void onClickBack() {
        onBackPressed();
    }

    @OnClick(R.id.btn_new_classroom)
    public void onClickAddNewClassroom() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_new_classroom, null);

        edtName = view.findViewById(R.id.edt_classroom_name);
        btnSave = view.findViewById(R.id.btn_save_classroom);
        btnCancel = view.findViewById(R.id.btn_cancel_classroom);

        mBuilder.setView(view);
        final AlertDialog alertDialog = mBuilder.create();
        alertDialog.show();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = false;
                if (edtName.getText().toString().length() == 0) {
                    edtName.setError("Required");
                } else {
                    showProgress("Refreshing...");
                    ClassroomModel.getInstance().addClassroom(edtName.getText().toString());
                    ClassroomModel.getInstance().loadClassrooms();
                    flag = true;
                }
                if (flag) {
                    alertDialog.cancel();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
    }

    private void setupClassroomRecyclerView() {
        mClassroomListAdapter = new ClassroomListAdapter(this, mController);
        rvClassroom.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        rvClassroom.setAdapter(mClassroomListAdapter);
    }

    public boolean isClassroomListEmpty() {
        return mClassroomListAdapter == null || mClassroomListAdapter.getItemCount() == 0;
    }

    public void displayClassroomList(List<ClassroomVO> classroomList, boolean isToAppend) {
        if (isToAppend) {
            mClassroomListAdapter.appendNewData(classroomList);
        } else {
            mClassroomListAdapter.setNewData(classroomList);
        }
        hideProgress();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnClassroomListLoaded(DataEvents.ClassroomListLoadedEvent event) {
        mClassroomList = new ArrayList<>(event.getClassroomList().values());
        displayClassroomList(mClassroomList, false);
    }

    @Override
    public void onClickClassroom(View view, ClassroomVO classroomVO) {
        Intent intent = new Intent();
        switch (screenType) {
            case 1:
                intent = AssignmentActivity.newIntent(this, classroomVO.getClassroomId());
                break;
            case 2:
                intent = TutorialActivity.newIntent(this, classroomVO.getClassroomId());
                break;
            case 3:
                intent = StudentListActivity.newIntent(this, classroomVO.getClassroomId());
                break;
            case 4:
                intent = RollCallActivity.newIntent(this, classroomVO.getClassroomId());
                break;
        }

        startActivity(intent);
    }

    public void showProgress(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(ClassroomActivity.this);
            mProgressDialog.setMessage(msg);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
