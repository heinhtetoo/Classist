package com.heinhtetoo.myclass.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.heinhtetoo.myclass.adapters.AssignmentListAdapter;
import com.heinhtetoo.myclass.controllers.AssignmentItemController;
import com.heinhtetoo.myclass.data.models.ClassroomModel;
import com.heinhtetoo.myclass.data.vos.AssignmentVO;
import com.heinhtetoo.myclass.data.vos.StudentVO;
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

public class AssignmentActivity extends AppCompatActivity implements AssignmentItemController {

    public static final int ASSIGNMENT_TYPE = 1;

    private static final String AL_ID = "classroomId";

    private AssignmentListAdapter mAssignmentListAdapter;
    private AssignmentItemController mController;

    private List<StudentVO> mStudentList = new ArrayList<>();

    private List<AssignmentVO> mAssignmentList = new ArrayList<>();

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

    @Bind(R.id.ic_back_assignment)
    ImageView ivBack;

    @Bind(R.id.btn_new_assignment)
    MaterialButton btnAddAssignment;

    @Bind(R.id.rv_assignment_list)
    RecyclerView rvAssignment;

    @Bind(R.id.tv_no_assignment)
    TextView tvNoAssignment;

    public static Intent newIntent(Context context, String id) {
        Intent intent = new Intent(context, AssignmentActivity.class);
        intent.putExtra(AL_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        ButterKnife.bind(this, this);

        classroomId = getIntent().getStringExtra(AL_ID);

        mController = (AssignmentItemController) this;
        mClassroomModel = ClassroomModel.getInstance();

        setupAssignmentRecyclerView();
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
        mAssignmentListAdapter.clearData();
    }

    @OnClick(R.id.ic_back_assignment)
    public void onClickBack() {
        onBackPressed();
    }

    @OnClick(R.id.btn_new_assignment)
    public void onClickAddNewAssignment() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_new_assign_tuto, null);

        tvTitle = view.findViewById(R.id.tv_dialog_new_assign_tuto);
        tilName = view.findViewById(R.id.til_assign_tuto_name);
        tilCode = view.findViewById(R.id.til_assign_tuto_code);
        edtName = view.findViewById(R.id.edt_assign_tuto_name);
        edtCode = view.findViewById(R.id.edt_assign_tuto_code);
        btnSave = view.findViewById(R.id.btn_save_assignment);
        btnCancel = view.findViewById(R.id.btn_cancel_assignment);

        tvTitle.setText("New Assignment");
        tilName.setHint("Assignment Name");
        tilCode.setHint("Module Code");

        mBuilder.setView(view);
        final AlertDialog alertDialog = mBuilder.create();
        alertDialog.show();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = false;
                final String newAssignmentId;
                if (edtName.getText().toString().length() == 0) {
                    edtName.setError("Required");
                } else if (edtCode.getText().toString().length() == 0) {
                    edtCode.setError("Required");
                } else {
                    showProgress("Refreshing...");
                    Date c = Calendar.getInstance().getTime();

                    @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String formattedDate = df.format(c);

                    newAssignmentId = ClassroomModel.getInstance().addAssignmentByClassroomId(classroomId, edtName.getText().toString(),
                            edtCode.getText().toString(), formattedDate);
                    for (int i = 0; i < mStudentList.size(); i++) {
                        ClassroomModel.getInstance().updateStudentAssignmentByIdAndAssignmentIdAndClassroomId(classroomId,
                                mStudentList.get(i).getStudentId(), newAssignmentId, false);
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

    private void setupAssignmentRecyclerView() {
        mAssignmentListAdapter = new AssignmentListAdapter(this, mController);
        rvAssignment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvAssignment.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        rvAssignment.setAdapter(mAssignmentListAdapter);
    }

    public boolean isAssignmentListEmpty() {
        return mAssignmentListAdapter == null || mAssignmentListAdapter.getItemCount() == 0;
    }

    public void displayAssignmentList(List<AssignmentVO> assignmentList, boolean isToAppend) {
        if (isToAppend) {
            mAssignmentListAdapter.appendNewData(assignmentList);
        } else {
            mAssignmentListAdapter.setNewData(assignmentList);
        }
        hideProgress();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnClassroomListLoaded(DataEvents.ClassroomListLoadedEvent event) {
        mStudentList = new ArrayList<>(event.getClassroomList().get(classroomId).getStudentVOList().values());
        mAssignmentList = new ArrayList<>(event.getClassroomList().get(classroomId).getAssignmentVOList().values());
        if (mAssignmentList.isEmpty()) {
            tvNoAssignment.setVisibility(View.VISIBLE);
        } else {
            tvNoAssignment.setVisibility(View.GONE);
            displayAssignmentList(mAssignmentList, false);
        }
    }

    @Override
    public void onClickAssignment(View view, AssignmentVO assignmentVO) {
        Intent intent = CheckCompleteActivity.newIntent(this, classroomId,
                assignmentVO.getAssignmentId(), assignmentVO.getAssignmentName(), ASSIGNMENT_TYPE);
        startActivity(intent);
    }

    public void showProgress(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(AssignmentActivity.this);
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
