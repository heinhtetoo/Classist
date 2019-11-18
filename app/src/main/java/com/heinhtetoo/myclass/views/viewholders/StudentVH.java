package com.heinhtetoo.myclass.views.viewholders;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.heinhtetoo.myclass.R;
import com.heinhtetoo.myclass.controllers.StudentItemController;
import com.heinhtetoo.myclass.data.vos.StudentVO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;

public class StudentVH extends BaseViewHolder<StudentVO> {

    private StudentVO mStudent;
    private StudentItemController mStudentItemController;

    private ArrayList<String> thisMonthKeySet;

    @Bind(R.id.tv_student_roll)
    TextView tvStudentRoll;

    @Bind(R.id.tv_student_name)
    TextView tvStudentName;

    @Bind(R.id.tv_attendance_title)
    TextView getTvAttendanceTitle;

    @Bind(R.id.tv_attendance_roll_call)
    TextView tvAttendance;

    public StudentVH(View itemView, StudentItemController mStudentItemController) {
        super(itemView);
        this.mStudentItemController = mStudentItemController;
    }

    @Override
    public void bind(StudentVO data) {
        mStudent = data;
        tvStudentRoll.setText(mStudent.getStudentRoll());
        tvStudentName.setText(mStudent.getStudentName());

        int trueCount = 0;
        int percent;
        if (mStudent.getStudentAttendanceRecord().isEmpty()) {
            trueCount = -1;
        } else {
            ArrayList<String> keySet = new ArrayList<>(mStudent.getStudentAttendanceRecord().keySet());
            thisMonthKeySet = new ArrayList<>();

            Date c = Calendar.getInstance().getTime();

            @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("MMM");
            String formattedMonth = df.format(c);
            getTvAttendanceTitle.setText(formattedMonth + " Attendance");

            for (int i = 0; i < keySet.size(); i++) {
                String[] splitKey = keySet.get(i).split("-");
                if (formattedMonth.equals(splitKey[1])) {
                    thisMonthKeySet.add(keySet.get(i));
                }
            }

            for (int i = 0; i < thisMonthKeySet.size(); i++) {
                if (mStudent.getStudentAttendanceRecord().get(thisMonthKeySet.get(i))) {
                    trueCount = trueCount + 1;
                }
            }
        }
        if (trueCount == -1) {
            tvAttendance.setTextColor(Color.parseColor("#757575"));
            tvAttendance.setText("No Records ");
        } else {
            percent = (trueCount*100) / thisMonthKeySet.size();
            if (percent > 74) {
                tvAttendance.setTextColor(Color.parseColor("#00C853"));
            } else {
                tvAttendance.setTextColor(Color.parseColor("#757575"));
            }
            tvAttendance.setText(percent + "% ");
        }
    }

    @Override
    public void onClick(View view) {
        mStudentItemController.onClickStudent(view, mStudent);
    }
}
