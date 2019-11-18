package com.heinhtetoo.myclass.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.heinhtetoo.myclass.R;
import com.heinhtetoo.myclass.adapters.TutorialListAdapter;
import com.heinhtetoo.myclass.controllers.TutorialItemController;
import com.heinhtetoo.myclass.data.models.ClassroomModel;
import com.heinhtetoo.myclass.data.vos.StudentVO;
import com.heinhtetoo.myclass.data.vos.TutorialVO;
import com.heinhtetoo.myclass.events.DataEvents;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TutorialActivity extends AppCompatActivity implements TutorialItemController {

    public static final int TUTORIAL_TYPE = 2;

    private static final String TL_ID = "classroomId";

    private TutorialListAdapter mTutorialListAdapter;
    private TutorialItemController mController;

    private List<StudentVO> mStudentList = new ArrayList<>();

    private List<TutorialVO> mTutorialList = new ArrayList<>();

    private ClassroomModel mClassroomModel;

    private String classroomId;

    public ProgressDialog mProgressDialog;

    TextView tvTitle;
    TextInputLayout tilName;
    TextInputLayout tilCode;
    TextInputEditText edtName;
    TextInputEditText edtCode;
    MaterialButton btnSave;
    MaterialButton btnCancel;

    @Bind(R.id.btn_new_tutorial)
    MaterialButton btnAddTutorial;

    @Bind(R.id.rv_tutorial_list)
    RecyclerView rvTutorial;

    @Bind(R.id.tv_no_tutorial)
    TextView tvNoTutorial;

    public static Intent newIntent(Context context, String id) {
        Intent intent = new Intent(context, TutorialActivity.class);
        intent.putExtra(TL_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        ButterKnife.bind(this, this);

        classroomId = getIntent().getStringExtra(TL_ID);

        mController = (TutorialItemController) this;
        mClassroomModel = ClassroomModel.getInstance();

        setupTutorialRecyclerView();
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
        mTutorialListAdapter.clearData();
    }

    @OnClick(R.id.ic_back_tutorial)
    public void onClickBack() {
        onBackPressed();
    }

    @OnClick(R.id.btn_new_tutorial)
    public void onClickAddNewTutorial() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_new_assign_tuto, null);

        tvTitle = view.findViewById(R.id.tv_dialog_new_assign_tuto);
        tilName = view.findViewById(R.id.til_assign_tuto_name);
        tilCode = view.findViewById(R.id.til_assign_tuto_code);
        edtName = view.findViewById(R.id.edt_assign_tuto_name);
        edtCode = view.findViewById(R.id.edt_assign_tuto_code);
        btnSave = view.findViewById(R.id.btn_save_assignment);
        btnCancel = view.findViewById(R.id.btn_cancel_assignment);

        tvTitle.setText("New Tutorial");
        tilName.setHint("Tutorial Name");
        tilCode.setHint("Module Code");

        mBuilder.setView(view);
        final AlertDialog alertDialog = mBuilder.create();
        alertDialog.show();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = false;
                final String newTutorialId;
                if (edtName.getText().toString().length() == 0) {
                    edtName.setError("Required");
                } else if (edtCode.getText().toString().length() == 0) {
                    edtCode.setError("Required");
                } else {
                    showProgress("Refreshing...");
                    Date c = Calendar.getInstance().getTime();

                    @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String formattedDate = df.format(c);

                    newTutorialId = ClassroomModel.getInstance().addTutorialByClassroomId(classroomId, edtName.getText().toString(),
                            edtCode.getText().toString(), formattedDate);
                    for (int i = 0; i < mStudentList.size(); i++) {
                        ClassroomModel.getInstance().updateStudentTutorialByIdAndTutorialIdAndClassroomId(classroomId,
                                mStudentList.get(i).getStudentId(), newTutorialId, false);
                    }
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

    private void setupTutorialRecyclerView() {
        mTutorialListAdapter = new TutorialListAdapter(this, mController);
        rvTutorial.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvTutorial.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        rvTutorial.setAdapter(mTutorialListAdapter);
    }

    public boolean isTutorialListEmpty() {
        return mTutorialListAdapter == null || mTutorialListAdapter.getItemCount() == 0;
    }

    public void displayTutorialList(List<TutorialVO> tutorialList, boolean isToAppend) {
        if (isToAppend) {
            mTutorialListAdapter.appendNewData(tutorialList);
        } else {
            mTutorialListAdapter.setNewData(tutorialList);
        }
        hideProgress();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnClassroomListLoaded(DataEvents.ClassroomListLoadedEvent event) {
        mStudentList = new ArrayList<>(event.getClassroomList().get(classroomId).getStudentVOList().values());
        mTutorialList = new ArrayList<>(event.getClassroomList().get(classroomId).getTutorialVOList().values());
        if (mTutorialList.isEmpty()) {
            tvNoTutorial.setVisibility(View.VISIBLE);
        } else {
            tvNoTutorial.setVisibility(View.GONE);
            displayTutorialList(mTutorialList, false);
        }
    }

    @Override
    public void onClickTutorial(View view, TutorialVO tutorialVO) {
        Intent intent = CheckCompleteActivity.newIntent(this, classroomId,
                tutorialVO.getTutorialId(), tutorialVO.getTutorialName(), TUTORIAL_TYPE);
        startActivity(intent);
    }

    public void showProgress(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(TutorialActivity.this);
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
