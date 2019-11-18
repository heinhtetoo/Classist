package com.heinhtetoo.myclass.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.heinhtetoo.myclass.R;
import com.heinhtetoo.myclass.data.models.ClassroomModel;
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

public class RollCallActivity extends AppCompatActivity {

    private static final String RC_ID = "classroomId";

    private List<StudentVO> mStudentList = new ArrayList<>();

    private List<Boolean>  mRollCallList = new ArrayList<>();

    public ProgressDialog mProgressDialog;

    private ClassroomModel mClassroomModel;

    private String classroomId;

    private int count = -1;

    private boolean readyToSave = false;

    @Bind(R.id.tv_roll_roll_call)
    TextView tvRoll;

    @Bind(R.id.tv_name_roll_call)
    TextView tvName;

    @Bind(R.id.btn_cancel_roll_call)
    MaterialButton btnCancel;

    @Bind(R.id.btn_yes_roll_call)
    MaterialButton btnYes;

    @Bind(R.id.btn_no_roll_call)
    MaterialButton btnNo;

    @Bind(R.id.btn_done_roll_call)
    MaterialButton btnDone;

    @Bind(R.id.cv_roll_call)
    MaterialCardView cvRollCall;

    @Bind(R.id.tv_done_title)
    TextView tvDoneTitle;

    @Bind(R.id.cv_icon_done)
    MaterialCardView cvIconDone;

    @Bind(R.id.ll_done)
    LinearLayout llDone;

    public static Intent newIntent(Context context, String id) {
        Intent intent = new Intent(context, RollCallActivity.class);
        intent.putExtra(RC_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_call);
        ButterKnife.bind(this, this);

        classroomId = getIntent().getStringExtra(RC_ID);

        mClassroomModel = ClassroomModel.getInstance();
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
    }

    @OnClick(R.id.ic_back_roll_call)
    public void onClickBack() {
        onBackPressed();
    }

    @OnClick(R.id.btn_cancel_roll_call)
    public void onClickCancel() {
        onBackPressed();
    }

    @OnClick(R.id.btn_yes_roll_call)
    public void onClickYes() {
        if (count==mStudentList.size()-1) {
            completeAllStudent();
        }
        mRollCallList.add(true);
        nextStudent();
    }

    @OnClick(R.id.btn_no_roll_call)
    public void onClickNo() {
        if (count==mStudentList.size()-1) {
            completeAllStudent();
        }
        mRollCallList.add(false);
        nextStudent();
    }

    @OnClick(R.id.btn_done_roll_call)
    public void onClickDone() {
        if (!mStudentList.isEmpty()) {
            Date c = Calendar.getInstance().getTime();

            @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(c);

            showProgress("Saving...");
            for (int i = 0 ; i < mStudentList.size(); i++) {
                mStudentList.get(i).getStudentAttendanceRecord()
                        .put(formattedDate, mRollCallList.get(i));
                ClassroomModel.getInstance().updateStudentByIdAndClassroomId(classroomId, mStudentList.get(i).getStudentId()
                        , mStudentList.get(i));
            }
        }
        hideProgress();
        readyToSave = false;
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (readyToSave) {
            onClickDone();
        } else {
            super.onBackPressed();
            cvRollCall.setVisibility(View.GONE);
            llDone.setVisibility(View.GONE);
        }
    }

    public void nextStudent() {
        count++;
        if (count < mStudentList.size()) {
            tvRoll.setText(" " + mStudentList.get(count).getStudentRoll() + " ");
            tvName.setText(mStudentList.get(count).getStudentName());
        }
        else {

        }
    }

    public void emptyStudent() {
        btnCancel.setVisibility(View.GONE);
        cvRollCall.setVisibility(View.GONE);
        tvDoneTitle.setText(R.string.no_student);
        tvDoneTitle.setTextColor(Color.parseColor("#808080"));
        cvIconDone.setVisibility(View.INVISIBLE);
        llDone.setVisibility(View.VISIBLE);
    }

    public void completeAllStudent() {
        readyToSave = true;
        btnCancel.setVisibility(View.GONE);
        cvRollCall.setVisibility(View.GONE);
        tvDoneTitle.setTextColor(Color.parseColor("#D81B60"));
        tvDoneTitle.setText(R.string.roll_call_complete);
        cvIconDone.setVisibility(View.VISIBLE);
        llDone.setVisibility(View.VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnClassroomListLoaded(DataEvents.ClassroomListLoadedEvent event) {
        mStudentList = new ArrayList<>(event.getClassroomList().get(classroomId).getStudentVOList().values());
        if (mStudentList.isEmpty()) {
            emptyStudent();
        } else {
            nextStudent();
        }
    }

    public void showProgress(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(RollCallActivity.this);
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
