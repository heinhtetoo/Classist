package com.heinhtetoo.myclass.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
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
import com.heinhtetoo.myclass.adapters.RollCallRecordListAdapter;
import com.heinhtetoo.myclass.controllers.RollCallRecordItemController;
import com.heinhtetoo.myclass.data.models.ClassroomModel;
import com.heinhtetoo.myclass.data.vos.RecordVO;
import com.heinhtetoo.myclass.data.vos.StudentVO;
import com.heinhtetoo.myclass.events.DataEvents;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StudentDetailActivity extends AppCompatActivity implements RollCallRecordItemController {

    private static final String SD_ID = "classroomId";
    private static final String SD_STUDENT_ID = "studentId";

    private RollCallRecordListAdapter mRollCallRecordListAdapter;
    private RollCallRecordItemController mController;

    private StudentVO mStudent;
    private HashMap<String, Boolean> originalRecordList = new HashMap<>();
    private HashMap<String, Boolean> filteredRecordedList = new HashMap<>();
    private List<RecordVO> mRecordList = new ArrayList<>();

    private ClassroomModel mClassroomModel;

    public ProgressDialog mProgressDialog;

    private String classroomId;
    private String studentId;
    private String currentMonth;
    private String startMonth = "Jan 2019";

    ArrayList<String> availableMonthList = new ArrayList<>();

    @Bind(R.id.btn_left)
    ImageView btnLeft;

    @Bind(R.id.btn_right)
    ImageView btnRight;

    @Bind(R.id.rv_student_detail_roll_call_list)
    RecyclerView rvStudent;

    @Bind(R.id.tv_initial_name)
    TextView tvInitialName;

    @Bind(R.id.tv_student_detail_name)
    TextView tvStudentDetailName;

    @Bind(R.id.tv_student_detail_roll)
    TextView tvStudentDetailRoll;

    @Bind(R.id.tv_month_title)
    TextView tvMonthTitle;

    @Bind(R.id.tv_student_detail_percent)
    TextView tvStudentRollCallPercent;

    @Bind(R.id.tv_no_record)
    TextView tvNoRecords;

    public static Intent newIntent(Context context, String id, String studentId) {
        Intent intent = new Intent(context, StudentDetailActivity.class);
        intent.putExtra(SD_ID, id);
        intent.putExtra(SD_STUDENT_ID, studentId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);
        ButterKnife.bind(this, this);

        classroomId = getIntent().getStringExtra(SD_ID);
        studentId = getIntent().getStringExtra(SD_STUDENT_ID);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("MMM yyyy");

        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        Date c = Calendar.getInstance().getTime();

        currentMonth = df.format(c);
        tvMonthTitle.setText(currentMonth);

        try {
            startCalendar.setTime(df.parse(startMonth));
            endCalendar.setTime(c);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        do {
            availableMonthList.add(df.format(startCalendar.getTime()));
            startCalendar.add(Calendar.MONTH, 1);
        } while (startCalendar.before(endCalendar));

        mController = (RollCallRecordItemController) this;
        mClassroomModel = ClassroomModel.getInstance();

        setupRollCallRecordRecyclerView();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        showProgress("Fetching Data...");
        mClassroomModel.loadClassrooms();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        mRollCallRecordListAdapter.clearData();
    }

    @OnClick(R.id.ic_back_student_detail)
    public void onClickBack() {
        onBackPressed();
    }

    @OnClick(R.id.btn_left)
    public void onClickLeft() {
        filteredRecordedList.clear();

        for (int i = 0; i < availableMonthList.size(); i++) {
            if (currentMonth.equals(availableMonthList.get(i))) {
                if (i != 0) {
                    currentMonth = availableMonthList.get(i - 1);

                    prepareDataForAdapter();

                    tvMonthTitle.setText(currentMonth);
                    btnRight.setClickable(true);
                    btnRight.setColorFilter(this.getResources().getColor(R.color.colorAccent));
                    if (i - 1 == 0) {
                        btnLeft.setClickable(false);
                        btnLeft.setColorFilter(this.getResources().getColor(R.color.disabled_text));
                    } else {
                        btnLeft.setClickable(true);
                        btnLeft.setColorFilter(this.getResources().getColor(R.color.colorAccent));
                    }
                }
            }
        }
    }

    @OnClick(R.id.btn_right)
    public void onClickRight() {
        filteredRecordedList.clear();

        for (int i = availableMonthList.size() - 1; i > -1; i--) {
            if (currentMonth.equals(availableMonthList.get(i))) {
                if (i != availableMonthList.size() - 1) {
                    currentMonth = availableMonthList.get(i + 1);

                    prepareDataForAdapter();

                    tvMonthTitle.setText(currentMonth);
                    btnLeft.setClickable(true);
                    btnLeft.setColorFilter(this.getResources().getColor(R.color.colorAccent));
                    if (i + 1 == availableMonthList.size() - 1) {
                        btnRight.setClickable(false);
                        btnRight.setColorFilter(this.getResources().getColor(R.color.disabled_text));
                    } else {
                        btnRight.setClickable(true);
                        btnRight.setColorFilter(this.getResources().getColor(R.color.colorAccent));
                    }
                }
            }
        }
    }

    private void setupRollCallRecordRecyclerView() {
        mRollCallRecordListAdapter = new RollCallRecordListAdapter(this, mController);
        rvStudent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvStudent.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        rvStudent.setAdapter(mRollCallRecordListAdapter);
    }

    public boolean isRollCallRecordListEmpty() {
        return mRollCallRecordListAdapter == null || mRollCallRecordListAdapter.getItemCount() == 0;
    }

    public void prepareDataForAdapter() {
        ArrayList<String> keyList = new ArrayList<>(originalRecordList.keySet());
        ArrayList<String> formattedKeyList = new ArrayList<>();

        for (int i = 0; i < keyList.size(); i++) {
            String[] splitString = keyList.get(i).split("-");
            formattedKeyList.add(splitString[1] + " " + splitString[2]);
        }

        for (int i = 0; i < formattedKeyList.size(); i++) {
            if (currentMonth.equals(formattedKeyList.get(i))) {
                filteredRecordedList.put(keyList.get(i), originalRecordList.get(keyList.get(i)));
            }
        }

        ArrayList filteredKeyList = new ArrayList<>(filteredRecordedList.keySet());
        ArrayList<Boolean> filteredValueList = new ArrayList<>(filteredRecordedList.values());

        int trueCount = 0;
        int percent;
        if (filteredValueList.isEmpty()) {
            trueCount = -1;
        } else {
            for (int i = 0; i < filteredValueList.size(); i++) {
                if (filteredValueList.get(i)) {
                    trueCount = trueCount + 1;
                }
            }
        }
        if (trueCount == -1) {
            tvStudentRollCallPercent.setTextColor(this.getResources().getColor(R.color.secondary_text));
            tvStudentRollCallPercent.setText("No Records ");
        } else {
            percent = (trueCount * 100) / filteredValueList.size();
            if (percent > 74) {
                tvStudentRollCallPercent.setTextColor(Color.parseColor("#00C853"));
            } else {
                tvStudentRollCallPercent.setTextColor(Color.parseColor("#757575"));
            }
            tvStudentRollCallPercent.setText(percent + "% ");
        }

        mRecordList.clear();
        mRollCallRecordListAdapter.clearData();
        for (int i = 0; i < filteredKeyList.size(); i++) {
            mRecordList.add(new RecordVO(filteredKeyList.get(i).toString(), filteredValueList.get(i)));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tvInitialName.setFocusedByDefault(true);
        }
        tvInitialName.requestFocus();
        displayRollCallRecordList(mRecordList, false);
    }

    public void displayStudentData() {
        hideProgress();
        String[] splitName = mStudent.getStudentName().split(" ");
        String initialName = " ";
        for (int i = 0; i < splitName.length; i++) {
            if (i<2) {
                initialName += String.valueOf(splitName[i].charAt(0));
            }
        }
        initialName += " ";
        tvInitialName.setText(initialName);
        tvStudentDetailName.setText(mStudent.getStudentName());
        tvStudentDetailRoll.setText(mStudent.getStudentRoll());
    }

    public void displayRollCallRecordList(List<RecordVO> recordList, boolean isToAppend) {
        if (recordList.isEmpty()) {
            tvNoRecords.setVisibility(View.VISIBLE);
        } else {
            tvNoRecords.setVisibility(View.GONE);
        }
        if (isToAppend) {
            mRollCallRecordListAdapter.appendNewData(recordList);
        } else {
            mRollCallRecordListAdapter.setNewData(recordList);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnClassroomListLoaded(DataEvents.ClassroomListLoadedEvent event) {
        mStudent = event.getClassroomList().get(classroomId).getStudentVOList().get(studentId);
        displayStudentData();
        originalRecordList = new HashMap<>(event.getClassroomList().get(classroomId).getStudentVOList().get(studentId).getStudentAttendanceRecord());
        if (originalRecordList.isEmpty()) {
            tvNoRecords.setVisibility(View.VISIBLE);
        } else {
            tvNoRecords.setVisibility(View.GONE);
            prepareDataForAdapter();
        }
    }

    @Override
    public void onClickRecord(View view, RecordVO record) {

    }

    public void showProgress(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(StudentDetailActivity.this);
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
