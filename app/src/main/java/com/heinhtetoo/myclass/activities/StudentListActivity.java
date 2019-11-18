package com.heinhtetoo.myclass.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.heinhtetoo.myclass.R;
import com.heinhtetoo.myclass.adapters.StudentListAdapter;
import com.heinhtetoo.myclass.controllers.StudentItemController;
import com.heinhtetoo.myclass.data.models.ClassroomModel;
import com.heinhtetoo.myclass.data.vos.ClassroomVO;
import com.heinhtetoo.myclass.data.vos.StudentVO;
import com.heinhtetoo.myclass.events.DataEvents;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StudentListActivity extends AppCompatActivity implements StudentItemController {

    private static final String SL_ID = "classroomId";

    private StudentListAdapter mStudentListAdapter;
    private StudentItemController mController;

    private List<StudentVO> mStudentList = new ArrayList<>();
    private List<ClassroomVO> mPrerecordedClassroomList = new ArrayList<>();

    public ProgressDialog mProgressDialog;

    private ClassroomModel mClassroomModel;

    private String classroomId;

    TextInputEditText edtRoll;
    TextInputEditText edtName;
    TextInputEditText edtPhone;
    MaterialButton btnSave;
    MaterialButton btnCancel;

    Spinner spinnerClass;

    String spinnerResult;

    @Bind(R.id.btn_new_student)
    ImageButton btnAddStudent;

    @Bind(R.id.rv_student_list)
    RecyclerView rvStudent;

    @Bind(R.id.tv_no_student)
    TextView tvNoStudent;

    public static Intent newIntent(Context context, String id) {
        Intent intent = new Intent(context, StudentListActivity.class);
        intent.putExtra(SL_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        ButterKnife.bind(this, this);

        classroomId = getIntent().getStringExtra(SL_ID);

        mController = (StudentItemController) this;
        mClassroomModel = ClassroomModel.getInstance();

        setupStudentRecyclerView();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        mClassroomModel.loadPrerecordedStudentList();
        mClassroomModel.loadClassrooms();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        mStudentListAdapter.clearData();
    }

    @OnClick(R.id.ic_back_student_list)
    public void onClickBack() {
        onBackPressed();
    }

    @OnClick(R.id.btn_new_student)
    public void onClickAddNewStudent() {
        ClassroomModel.getInstance().changeDatabaseReferenceToClassroom();
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_new_student, null);

        edtRoll = view.findViewById(R.id.edt_student_roll);
        edtName = view.findViewById(R.id.edt_student_name);
        edtPhone = view.findViewById(R.id.edt_student_phone);
        btnSave = view.findViewById(R.id.btn_save_student);
        btnCancel = view.findViewById(R.id.btn_cancel_student);

        mBuilder.setView(view);
        final AlertDialog alertDialog = mBuilder.create();
        alertDialog.show();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean flag = false;
                if (edtRoll.getText().toString().length() == 0) {
                    edtRoll.setError("Required");
                } else if (edtName.getText().toString().length() == 0) {
                    edtName.setError("Required");
                } else if (edtPhone.getText().toString().length() == 0) {
                    edtPhone.setError("Required");
                } else {
                    HashMap<String, Boolean> rollCall = new HashMap<>();
                    if (!mStudentList.isEmpty()) {
                        rollCall = mStudentList.get(0).getStudentAttendanceRecord();
                    }
                    ClassroomModel.getInstance().addStudentByClassroomId(classroomId, edtRoll.getText().toString(),
                            edtName.getText().toString(), edtPhone.getText().toString(), rollCall);
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

    @OnClick(R.id.btn_import_student)
    public void onClickImportStudent() {
        if (!mPrerecordedClassroomList.isEmpty()) {
            ClassroomModel.getInstance().changeDatabaseReferenceToPrerecordedList();
            ClassroomModel.getInstance().loadPrerecordedStudentList();
            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            View view = getLayoutInflater().inflate(R.layout.dialog_import_student, null);

            spinnerClass = view.findViewById(R.id.spr_new_student);
            btnSave = view.findViewById(R.id.btn_save_student);
            btnCancel = view.findViewById(R.id.btn_cancel_student);

            final ArrayList<String> spinnerDataList = new ArrayList<>();
            for (int i = 0; i < mPrerecordedClassroomList.size(); i++) {
                spinnerDataList.add(mPrerecordedClassroomList.get(i).getClassroomName());
            }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, spinnerDataList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerClass.setAdapter(dataAdapter);

            spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinnerResult = mPrerecordedClassroomList.get(position).getClassroomId();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    spinnerResult = "";
                }
            });

            mBuilder.setView(view);
            final AlertDialog alertDialog = mBuilder.create();
            alertDialog.show();

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgress("Importing...");

                    HashMap<String, Boolean> rollCall = new HashMap<>();
                    if (!spinnerResult.equals("")) {
                        List<StudentVO> prerecordedList = new ArrayList<>(ClassroomModel.getInstance().getClassroomById(spinnerResult).getStudentVOList().values());
                        ClassroomModel.getInstance().changeDatabaseReferenceToClassroom();
                        for (int i = 0; i < prerecordedList.size(); i++) {
                            ClassroomModel.getInstance().addStudentByClassroomIdAndStudentId(classroomId, prerecordedList.get(i).getStudentId(),
                                    prerecordedList.get(i).getStudentRoll(),
                                    prerecordedList.get(i).getStudentName(), prerecordedList.get(i).getStudentPhone(), rollCall);
                            ClassroomModel.getInstance().loadClassrooms();
                        }
                    }
                    alertDialog.cancel();
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.cancel();
                }
            });
        } else {
            showProgress("Fetching...");
            ClassroomModel.getInstance().loadPrerecordedStudentList();
        }
    }

    private void setupStudentRecyclerView() {
        mStudentListAdapter = new StudentListAdapter(this, mController);
        rvStudent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvStudent.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        rvStudent.setAdapter(mStudentListAdapter);
    }

    public boolean isStudentListEmpty() {
        return mStudentListAdapter == null || mStudentListAdapter.getItemCount() == 0;
    }

    public void displayStudentList(List<StudentVO> studentList, boolean isToAppend) {
        if (isToAppend) {
            mStudentListAdapter.appendNewData(studentList);
        } else {
            mStudentListAdapter.setNewData(studentList);
        }
        hideProgress();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnClassroomListLoaded(DataEvents.ClassroomListLoadedEvent event) {
        mStudentList = new ArrayList<>(event.getClassroomList().get(classroomId).getStudentVOList().values());
        if (mStudentList.isEmpty()) {
            tvNoStudent.setVisibility(View.VISIBLE);
        } else {
            tvNoStudent.setVisibility(View.GONE);
            displayStudentList(mStudentList, false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnPrerecordedStudentListLoaded(DataEvents.PrerecordedStudentListLoadedEvent event) {
        hideProgress();
        mPrerecordedClassroomList = new ArrayList<>(event.getClassroomList().values());
    }

    @Override
    public void onClickStudent(View view, StudentVO studentVO) {
        Intent intent = StudentDetailActivity.newIntent(view.getContext(), classroomId, studentVO.getStudentId());
        startActivity(intent);
    }

    public void showProgress(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(StudentListActivity.this);
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
