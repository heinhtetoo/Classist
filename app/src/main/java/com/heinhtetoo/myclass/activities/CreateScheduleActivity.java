package com.heinhtetoo.myclass.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.heinhtetoo.myclass.R;
import com.heinhtetoo.myclass.data.models.UserModel;
import com.heinhtetoo.myclass.data.vos.DayVO;
import com.heinhtetoo.myclass.data.vos.ModuleVO;
import com.heinhtetoo.myclass.data.vos.ScheduleVO;
import com.heinhtetoo.myclass.utils.MMFontUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateScheduleActivity extends AppCompatActivity {

    private int dayCount = 0;
    private String[] dayList = {"Monday ", "Tuesday ", "Wednesday ", "Thursday ", "Friday "};

    private ModuleVO emptyModule = new ModuleVO("No Data", "No Data");
    private ModuleVO lunchModule = new ModuleVO("Lunch Time", "Free Time");
    private ArrayList<ModuleVO> emptyModuleList;
    private ScheduleVO schedule;

    ArrayList<TextInputEditText> edtCodeList;
    ArrayList<TextInputEditText> edtNameList;

    private ProgressDialog mProgressDialog;

    @Bind(R.id.ic_back_schedule)
    ImageView ivBack;

    @Bind(R.id.tv_day)
    TextView tvDay;

    @Bind(R.id.edt_code_period_1)
    TextInputEditText edtCodePeriod1;

    @Bind(R.id.edt_code_period_2)
    TextInputEditText edtCodePeriod2;

    @Bind(R.id.edt_code_period_3)
    TextInputEditText edtCodePeriod3;

    @Bind(R.id.edt_code_period_4)
    TextInputEditText edtCodePeriod4;

    @Bind(R.id.edt_code_period_5)
    TextInputEditText edtCodePeriod5;

    @Bind(R.id.edt_code_period_6)
    TextInputEditText edtCodePeriod6;

    @Bind(R.id.edt_name_period_1)
    TextInputEditText edtNamePeriod1;

    @Bind(R.id.edt_name_period_2)
    TextInputEditText edtNamePeriod2;

    @Bind(R.id.edt_name_period_3)
    TextInputEditText edtNamePeriod3;

    @Bind(R.id.edt_name_period_4)
    TextInputEditText edtNamePeriod4;

    @Bind(R.id.edt_name_period_5)
    TextInputEditText edtNamePeriod5;

    @Bind(R.id.edt_name_period_6)
    TextInputEditText edtNamePeriod6;

    @Bind(R.id.btn_input_back)
    MaterialButton btnBack;

    @Bind(R.id.btn_input_next)
    MaterialButton btnNext;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, CreateScheduleActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_schedule);
        ButterKnife.bind(this, this);

        schedule = new ScheduleVO();

        emptyModuleList = new ArrayList<>();
        emptyModuleList.add(0, emptyModule);
        emptyModuleList.add(1, emptyModule);
        emptyModuleList.add(2, emptyModule);
        emptyModuleList.add(3, lunchModule);
        emptyModuleList.add(4, emptyModule);
        emptyModuleList.add(5, emptyModule);
        emptyModuleList.add(6, emptyModule);

        DayVO emptyMondayVO = new DayVO(0, emptyModuleList);
        DayVO emptyTuesdayVO = new DayVO(0, emptyModuleList);
        DayVO emptyWednesdayVO = new DayVO(0, emptyModuleList);
        DayVO emptyThursdayVO = new DayVO(0, emptyModuleList);
        DayVO emptyFridayVO = new DayVO(0, emptyModuleList);

        schedule.setMondaySchedule(emptyMondayVO);
        schedule.setTuesdaySchedule(emptyTuesdayVO);
        schedule.setWednesdaySchedule(emptyWednesdayVO);
        schedule.setThursdaySchedule(emptyThursdayVO);
        schedule.setFridaySchedule(emptyFridayVO);

        edtCodeList = new ArrayList<>();
        edtNameList = new ArrayList<>();

        edtCodeList.add(edtCodePeriod1);
        edtNameList.add(edtNamePeriod1);

        edtCodeList.add(edtCodePeriod2);
        edtNameList.add(edtNamePeriod2);

        edtCodeList.add(edtCodePeriod3);
        edtNameList.add(edtNamePeriod3);

        edtCodeList.add(edtCodePeriod4);
        edtNameList.add(edtNamePeriod4);

        edtCodeList.add(edtCodePeriod5);
        edtNameList.add(edtNamePeriod5);

        edtCodeList.add(edtCodePeriod6);
        edtNameList.add(edtNamePeriod6);
    }

    @OnClick(R.id.ic_back_schedule)
    public void onClickBackCreateSchedule() {
        new AlertDialog.Builder(this)
                .setTitle("Are you sure?")
                .setMessage("Your schedule will not be updated.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onBackPressed();
                    }
                })
                .setNegativeButton("Wait", null)
                .create()
                .show();
    }

    @OnClick(R.id.btn_input_back)
    public void onClickBack() {
        if (dayCount > 0) {
            dayCount--;
        }
        clearAll();

        if (dayCount == 0) btnBack.setVisibility(View.GONE);
        if (dayCount < 4) btnNext.setText("Next");
        tvDay.setText(dayList[dayCount]);
    }

    @OnClick(R.id.btn_input_next)
    public void onClickNext() {
        if (isAllFilled()) {

            ArrayList<ModuleVO> moduleVOArrayList = new ArrayList<>();
            moduleVOArrayList.add(0, new ModuleVO(edtCodePeriod1.getText().toString(), edtNamePeriod1.getText().toString()));
            moduleVOArrayList.add(1, new ModuleVO(edtCodePeriod2.getText().toString(), edtNamePeriod2.getText().toString()));
            moduleVOArrayList.add(2, new ModuleVO(edtCodePeriod3.getText().toString(), edtNamePeriod3.getText().toString()));
            moduleVOArrayList.add(3, lunchModule);
            moduleVOArrayList.add(4, new ModuleVO(edtCodePeriod4.getText().toString(), edtNamePeriod4.getText().toString()));
            moduleVOArrayList.add(5, new ModuleVO(edtCodePeriod5.getText().toString(), edtNamePeriod5.getText().toString()));
            moduleVOArrayList.add(6, new ModuleVO(edtCodePeriod6.getText().toString(), edtNamePeriod6.getText().toString()));

            switch (dayCount) {
                case 0:
                    schedule.setMondaySchedule(new DayVO(0, moduleVOArrayList));
                    Log.e("Monday 1 Entry", schedule.getMondaySchedule().getModuleVOList().get(0).getCode());
                case 1:
                    schedule.setTuesdaySchedule(new DayVO(1, moduleVOArrayList));
                case 2:
                    schedule.setWednesdaySchedule(new DayVO(2, moduleVOArrayList));
                case 3:
                    schedule.setThursdaySchedule(new DayVO(3, moduleVOArrayList));
                case 4:
                    schedule.setFridaySchedule(new DayVO(4, moduleVOArrayList));
            }

            dayCount++;

            btnBack.setVisibility(View.VISIBLE);
            if (dayCount == 4) {
                btnNext.setText("Done");
            } else if (dayCount > 4) {
                showConfirmSaveDialog();
            }

            if (dayCount <= 4) {
                tvDay.setText(dayList[dayCount]);
            }

            clearAll();
        }
    }

    private void showConfirmSaveDialog() {

        String confirmMsg = getResources().getString(R.string.confirm_input_schedule);

        new AlertDialog.Builder(this)
                .setTitle("Save?")
                .setMessage(confirmMsg)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UserModel.getInstance().updateSchedule(schedule);
                        onBackPressed();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void showProgressDialogInfinite(String msg) {
        mProgressDialog = new ProgressDialog(this);

        if (!MMFontUtils.isSupportUnicode()) {
            mProgressDialog.setMessage(Html.fromHtml(MMFontUtils.uni2zg(msg)));
        } else {
            mProgressDialog.setMessage(Html.fromHtml(msg));
        }

        if (!mProgressDialog.isShowing()) {
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void clearAll() {
        for (int i = 0; i < edtCodeList.size(); i++) {
            edtCodeList.get(i).setText("");
            edtNameList.get(i).setText("");
        }
    }

    public boolean isAllFilled() {

        if (edtCodePeriod1.getText().toString().length() == 0) {
            edtCodePeriod1.setError("Required");
            return false;
        } else if (edtNamePeriod1.getText().toString().length() == 0) {
            edtNamePeriod1.setError("Required");
            return false;

        } else if (edtCodePeriod2.getText().toString().length() == 0) {
            edtCodePeriod2.setError("Required");
            return false;
        } else if (edtNamePeriod2.getText().toString().length() == 0) {
            edtNamePeriod2.setError("Required");
            return false;

        } else if (edtCodePeriod3.getText().toString().length() == 0) {
            edtCodePeriod3.setError("Required");
            return false;
        } else if (edtNamePeriod3.getText().toString().length() == 0) {
            edtNamePeriod3.setError("Required");
            return false;

        } else if (edtCodePeriod4.getText().toString().length() == 0) {
            edtCodePeriod4.setError("Required");
            return false;
        } else if (edtNamePeriod4.getText().toString().length() == 0) {
            edtNamePeriod4.setError("Required");
            return false;

        } else if (edtCodePeriod5.getText().toString().length() == 0) {
            edtCodePeriod5.setError("Required");
            return false;
        } else if (edtNamePeriod5.getText().toString().length() == 0) {
            edtNamePeriod5.setError("Required");
            return false;

        } else if (edtCodePeriod6.getText().toString().length() == 0) {
            edtCodePeriod6.setError("Required");
            return false;
        } else if (edtNamePeriod6.getText().toString().length() == 0) {
            edtNamePeriod6.setError("Required");
            return false;

        } else {
            return true;
        }
    }
}
