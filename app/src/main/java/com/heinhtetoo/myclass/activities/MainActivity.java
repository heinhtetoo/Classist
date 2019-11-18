package com.heinhtetoo.myclass.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.heinhtetoo.myclass.R;
import com.heinhtetoo.myclass.data.models.ClassroomModel;
import com.heinhtetoo.myclass.data.models.UserModel;
import com.heinhtetoo.myclass.data.vos.ClassroomVO;
import com.heinhtetoo.myclass.data.vos.ModuleVO;
import com.heinhtetoo.myclass.data.vos.UserVO;
import com.heinhtetoo.myclass.events.DataEvents;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private UserModel mUserModel;
    private ClassroomModel mClassroomModel;

    private List<ClassroomVO> mClassroomList = new ArrayList<>();

    public ProgressDialog mProgressDialog;
    public Dialog dialog;

    boolean isAttached = false;

    List<ModuleVO> moduleList = new ArrayList<>();
    String nowModuleCode = "No Class";
    String nowModuleName = "Free Time";
    String nextModuleCode = "No Class";

    @Bind(R.id.rl_main)
    RelativeLayout rlMain;

    @Bind(R.id.cv_profile)
    CircleImageView cvProfile;

    @Bind(R.id.tv_greeting)
    TextView tvGreeting;

    @Bind(R.id.text_module_no_now)
    TextView tvNowModuleCode;

    @Bind(R.id.text_module_name_now)
    TextView tvNowModuleName;

    @Bind(R.id.text_module_no_next)
    TextView tvNextModuleCode;

    @Bind(R.id.tv_assignment_no)
    TextView tvAssignmentNo;

    @Bind(R.id.tv_tutorial_no)
    TextView tvTutorialNo;

    @Bind(R.id.tv_student_no)
    TextView tvStudentNo;

    @Bind(R.id.ll_assignment)
    LinearLayout llAssignment;

    @Bind(R.id.ll_roll_call)
    LinearLayout llRollCall;

    @Bind(R.id.ll_tutorial)
    LinearLayout llTutorial;

    @Bind(R.id.ll_student_list)
    LinearLayout llStudentList;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this, this);

        mUserModel = UserModel.getInstance();
        mClassroomModel = ClassroomModel.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        showProgress(getString(R.string.file_loading));
        EventBus.getDefault().register(this);

        boolean flag = UserModel.getInstance().getSignInStateFromPref(this);
        String userId = UserModel.getInstance().getAccountIdFromPref(this);
        if (userId != null) {
            UserModel.getInstance().loadUserFromPref(userId);
        }

        if (flag) {
            /*if (mUserModel.isUserSignedIn()) {
                mUserModel.syncUserInfo(mUserModel.getAccountIdFromPref(this), new UserModel.SyncUserInfoDelegate() {
                    @Override
                    public void syncUserInfo(UserVO user) {
                        updateData(user);
                        ClassroomModel.getInstance().loadClassrooms();
                    }
                });
            }*/
            mUserModel.syncUserInfo(mUserModel.getAccountIdFromPref(this), new UserModel.SyncUserInfoDelegate() {
                @Override
                public void syncUserInfo(UserVO user) {
                    updateData(user);
                    ClassroomModel.getInstance().loadClassrooms();
                }
            });
        } else {
            hideProgress();
            Intent intent = UserProfileActivity.newIntent(this, true);
            startActivity(intent);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onAttachedToWindow() {
        isAttached = true;
    }

    @Override
    public void onDetachedFromWindow() {
        isAttached = false;
    }

    @OnClick(R.id.cv_profile)
    public void onClickImageViewProfile(View view) {
        Intent intent = UserProfileActivity.newIntent(view.getContext(), false);
        startActivity(intent);
    }

    @OnClick(R.id.ll_assignment)
    public void onClickAssignment(View view) {
        Intent intent = ClassroomActivity.newIntent(view.getContext(), 1);
        startActivity(intent);
    }

    @OnClick(R.id.ll_tutorial)
    public void onClickTutorial(View view) {
        Intent intent = ClassroomActivity.newIntent(view.getContext(), 2);
        startActivity(intent);
    }

    @OnClick(R.id.ll_student_list)
    public void onClickStudentList(View view) {
        Intent intent = ClassroomActivity.newIntent(view.getContext(), 3);
        startActivity(intent);
    }

    @OnClick(R.id.ll_roll_call)
    public void onClickRollCall(View view) {
        Intent intent = ClassroomActivity.newIntent(view.getContext(), 4);
        startActivity(intent);
    }

    private void updateData(UserVO mUser) {

        getScheduleData(mUser);

        String username = "Hello, User";

        if (mUser.getDisplayName() != null && !mUser.getDisplayName().isEmpty()) {
            String name[] = mUser.getDisplayName().split(" ");
            String greeting = "Hello,";
            for (int i = 0; i < name.length - 1; i++) {
                greeting += " " + name[i];
            }
            username = greeting;
        }

        if (isAttached) {
            Glide.with(MainActivity.this)
                    .load(mUser.getPhotoUrl())
                    .thumbnail(
                            Glide.with(MainActivity.this)
                                    .load(mUser.getPhotoUrl()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(cvProfile);
        }

        tvGreeting.setText(username);
        tvNowModuleCode.setText(nowModuleCode);
        tvNowModuleName.setText(nowModuleName);
        tvNextModuleCode.setText(nextModuleCode);

        if (mUser.getAssignmentNo() > 100) {
            tvAssignmentNo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
        } else {
            tvAssignmentNo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
        }
        if (mUser.getTutorialNo() > 100) {
            tvTutorialNo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
        } else {
            tvTutorialNo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
        }
        tvAssignmentNo.setText(String.valueOf(mUser.getAssignmentNo()));
        tvTutorialNo.setText(String.valueOf(mUser.getTutorialNo()));
        tvStudentNo.setText(String.valueOf(mUser.getStudentNo()));

        rlMain.setVisibility(View.VISIBLE);

        hideProgress();
    }

    public void getScheduleData(UserVO mUser) {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
        String dayOfTheWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        String[] currentTime = mdformat.format(calendar.getTime()).split(":");
        String tomorrowFirstModuleCode = "No Class";

        int currentHr = Integer.parseInt(currentTime[0]);
        int currentMin = Integer.parseInt(currentTime[1]);

        if (dayOfTheWeek.equals("Monday")) {
            moduleList = mUser.getSchedule().getMondaySchedule().getModuleVOList();
            tomorrowFirstModuleCode = mUser.getSchedule().getTuesdaySchedule().getModuleVOList().get(0).getCode();
        } else if (dayOfTheWeek.equals("Tuesday")) {
            moduleList = mUser.getSchedule().getTuesdaySchedule().getModuleVOList();
            tomorrowFirstModuleCode = mUser.getSchedule().getWednesdaySchedule().getModuleVOList().get(0).getCode();
        } else if (dayOfTheWeek.equals("Wednesday")) {
            moduleList = mUser.getSchedule().getWednesdaySchedule().getModuleVOList();
            tomorrowFirstModuleCode = mUser.getSchedule().getThursdaySchedule().getModuleVOList().get(0).getCode();
        } else if (dayOfTheWeek.equals("Thursday")) {
            moduleList = mUser.getSchedule().getThursdaySchedule().getModuleVOList();
            tomorrowFirstModuleCode = mUser.getSchedule().getFridaySchedule().getModuleVOList().get(0).getCode();
        } else if (dayOfTheWeek.equals("Friday")) {
            moduleList = mUser.getSchedule().getFridaySchedule().getModuleVOList();
            tomorrowFirstModuleCode = "Saturday ";
        } else if (dayOfTheWeek.equals("Saturday")) {
            moduleList = new ArrayList<>();
            nowModuleCode = "Saturday ";
            nowModuleName = "Off-day ";
            nextModuleCode = "Sunday ";
        } else if (dayOfTheWeek.equals("Sunday")) {
            moduleList = new ArrayList<>();
            nowModuleCode = "Sunday ";
            nowModuleName = "Off-day ";
            nextModuleCode = mUser.getSchedule().getMondaySchedule().getModuleVOList().get(0).getCode();
        }

        if (!moduleList.isEmpty()) {
            if (currentHr < 9) {
                nowModuleCode = "No Class";
                nowModuleName = "Free Time";
                nextModuleCode = moduleList.get(0).getCode();
            } else if (currentHr == 9 && currentMin < 56) {
                nowModuleCode = moduleList.get(0).getCode();
                nowModuleName = moduleList.get(0).getName();
                nextModuleCode = moduleList.get(1).getCode();
            } else if (currentHr == 10 && currentMin < 56) {
                nowModuleCode = moduleList.get(1).getCode();
                nowModuleName = moduleList.get(1).getName();
                nextModuleCode = moduleList.get(2).getCode();
            } else if (currentHr == 11 && currentMin < 56) {
                nowModuleCode = moduleList.get(2).getCode();
                nowModuleName = moduleList.get(2).getName();
                nextModuleCode = moduleList.get(3).getCode();
            } else if ((currentHr == 11) || (currentHr == 12 && currentMin < 30)) {
                nowModuleCode = moduleList.get(3).getCode();
                nowModuleName = moduleList.get(3).getName();
                nextModuleCode = moduleList.get(4).getCode();
            } else if (currentHr == 12) {
                nowModuleCode = moduleList.get(4).getCode();
                nowModuleName = moduleList.get(4).getName();
                nextModuleCode = moduleList.get(5).getCode();
            } else if ((currentHr == 13 && currentMin < 29) || (currentHr == 14 && currentMin < 26)) {
                nowModuleCode = moduleList.get(5).getCode();
                nowModuleName = moduleList.get(5).getName();
                nextModuleCode = moduleList.get(6).getCode();
            } else if ((currentHr == 14 && currentMin < 29) || (currentHr == 15 && currentMin < 29)) {
                nowModuleCode = moduleList.get(6).getCode();
                nowModuleName = moduleList.get(6).getName();
                nextModuleCode = "No Class";
            } else {
                nowModuleCode = "No Class";
                nowModuleName = "Free Time";
                nextModuleCode = tomorrowFirstModuleCode;
            }
        }
    }

    public void showProgress(String msg) {
        /*if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setMessage(msg);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();*/

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_loading);

        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);

        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(gifImageView);

        Glide.with(MainActivity.this)
                .load(R.drawable.loading_progress)
                .thumbnail(
                        Glide.with(this)
                                .load(R.drawable.loading_progress))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageViewTarget);

        dialog.show();
    }

    public void hideProgress() {
        /*if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }*/

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnUserDataLoaded(DataEvents.UserDataLoadedEvent event) {
        hideProgress();
        updateData(event.getUserVO());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnClassroomDataLoaded(DataEvents.ClassroomListLoadedEvent event) {
        mClassroomList = new ArrayList<>(event.getClassroomList().values());
        int assignment = 0;
        int tutorial = 0;
        int student = 0;

        for (int i = 0; i < mClassroomList.size(); i++) {
            assignment += mClassroomList.get(i).getAssignmentVOList().size();
            tutorial += mClassroomList.get(i).getTutorialVOList().size();
            student += mClassroomList.get(i).getStudentVOList().size();
        }
        mUserModel.updateClassroomData(assignment, tutorial, student);
    }

}
